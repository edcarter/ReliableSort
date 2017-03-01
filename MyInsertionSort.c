#include <stdio.h>
#include <stdlib.h>
#include <jni.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <fcntl.h>
#include <time.h>
#include <string.h>
#include <errno.h>

#include "FileInsertionSort_InsertionSort.h"


/*
Read ints from a file specified by 'path' up to a maximum of max
and fills them into the caller allocated buf.

Returns number of ints read or -1 on failure.
*/
int ReadInts(jint* buf, const char* path, unsigned int max)
{
    int i, j, scanned, err = 0;
    FILE *file;
    file = fopen(path, "r");
    if (file == NULL)
        return -1;

    clearerr(file);
    for (j = 0; j < max; j++) {
        scanned = fscanf(file, "%d\n", &i);
        err = ferror(file);
        if (scanned == 0) err = 1;
        if (scanned == EOF || err) {
            break;
        }
        buf[j] = i;
    }

    if (file != NULL)
        fclose(file);

    return err == 1 ? -1 : j;
}

// write ints to file, return -1 on failure otherwise it was a success
int WriteInts(const jint* buf, const char* path, unsigned int length)
{
    FILE *file;
    unsigned int i;
    file = fopen(path, "w");
    if (file == NULL) return -1;
    for (i = 0; i < length; i++) {
        if (fprintf(file, "%d\n", buf[i]) == 0) break;
    }
    if (file != NULL) fclose(file);
    if (i == length -1 || length == 0) {
        return 0;
    } else {
        return -1;
    }
}

/*
count number of lines in a file located at 'path'

Returns -1 on error
*/
int CountLines(const char* path)
{
    FILE *file;
    char c;
    int count = 0;

    file = fopen(path, "r");
    if (file == NULL) {
        return -1;
    }
    while ((c = fgetc(file)) != EOF) {
        if (c == '\n') count++;
    }
    fclose(file);
    return count;
}

// estimate if a failure occurred.
// -1 will be returned if a simulated failure occurs, otherwise there was no failure
int EstimateFailure(int memoryAccesses, double failureChance)
{
    time_t seed;
    double myRand, hazard;

    hazard = failureChance * memoryAccesses;
    seed = time(NULL);
    srand((unsigned int) seed);
    myRand = ((double) rand()) / (double) RAND_MAX; // normalize our random number to between [0, 1]
    if (0.5 <= myRand && myRand <= 0.5 + hazard) {
        return -1;
    } else {
        return 0;
    }
}

// native insertion sort method. returns -1 on failure, otherwise success.
JNIEXPORT jint JNICALL Java_FileInsertionSort_00024InsertionSort_sort
  (JNIEnv * env, jobject o, jstring javaUnsortedPath, jstring javaSortedPath, jdouble pFailure)
{
	jsize len;
	jint *nativeArray;
	jint memoryAccesses;
	jint result = -1;
	const char *nativeUnsortedPath = (*env)->GetStringUTFChars(env, javaUnsortedPath, 0);
	const char *nativeSortedPath = (*env)->GetStringUTFChars(env, javaSortedPath, 0);

    errno = 0;

    len = CountLines(nativeUnsortedPath);
    if (len < 0)
        goto CLEANUP;

    nativeArray = malloc(len * sizeof(jint));
    if (nativeArray == NULL)
        goto CLEANUP;

    if (ReadInts(nativeArray, nativeUnsortedPath, len) == -1)
        goto CLEANUP;

    /* perform insertion sort now */
    memoryAccesses = 0;
	for (int i = 1; i < len; i++) {                            // 3 memory accesses
		int j = i;                                             // 2 memory accesses
		memoryAccesses += 5;                                   // 5 memory accesses per outer for loop

		while (j > 0 && nativeArray[j-1] > nativeArray[j]) {   // 3 memory accesses
			jint tmp = nativeArray[j];                         // 2 memory accesses
			nativeArray[j] = nativeArray[j-1];                 // 2 memory accesses
			nativeArray[j-1] = tmp;                            // 2 memory accesses
			j--;                                               // 1 memory access
			memoryAccesses += 10;                              // 10 memory accesses per inner while loop
		}
	}

    result = EstimateFailure(memoryAccesses, pFailure);
    if (result != -1) WriteInts(nativeArray, nativeSortedPath, len);

    CLEANUP:
    if (nativeUnsortedPath != NULL)
        (*env)->ReleaseStringUTFChars(env, javaUnsortedPath, nativeUnsortedPath);
    if (nativeSortedPath != NULL)
        (*env)->ReleaseStringUTFChars(env, javaSortedPath, nativeSortedPath);
    if (nativeArray != NULL)
        free(nativeArray);

    if (errno) {
        printf("errno: %s\n", strerror(errno));
        fflush(stdout);
    }

	return result;
}




