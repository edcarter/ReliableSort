#include <stdio.h>
#include <stdlib.h>
#include <jni.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <fcntl.h>
#include "FileInsertionSort_InsertionSort.h"


/*
Read ints from a file specified by 'path' up to a maximum of max
and fills them into the caller allocated buf.

Returns number of ints read or -1 on failure.
*/
int ReadInts(jint* buf, const char* path, unsigned int max)
{
    int i, j, scanned;
    FILE *file;
    file = fopen(path, "r");
    if (file == NULL) {
        return -1;
    }
    for (j = 0; j < max; j++) {
        scanned = fscanf(file, "%d\n", &i);
        if (scanned == 0 || scanned == EOF) {
            break;
        }
        buf[j] = i;
    }
    if (file != NULL) {
        fclose(file);
    }
    if (!scanned || ferror(file)) {
        return -1;
    }
    return j;
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

JNIEXPORT jintArray JNICALL Java_FileInsertionSort_00024InsertionSort_sort
  (JNIEnv * env, jobject o, jstring javaString)
{
	jsize len;
	jint *nativeArray;
	jintArray result;
	const char *nativeString = (*env)->GetStringUTFChars(env, javaString, 0);
    len = CountLines(nativeString);
    if (len < 0)
        goto CLEANUP;
    nativeArray = malloc(len * sizeof(jint));
    if (nativeArray == NULL)
        goto CLEANUP;
    if (ReadInts(nativeArray, nativeString, len) < 0)
        goto CLEANUP;

    /* perform insertion sort now */
	for (int i = 1; i < len; i++) {
		int j = i;
		while (j > 0 && nativeArray[j-1] > nativeArray[j]) {
			jint tmp = nativeArray[j];
			nativeArray[j] = nativeArray[j-1];
			nativeArray[j-1] = tmp;
			j--;
		}
	}
	result = (*env)->NewIntArray(env, len);
	if (result != NULL)
	    (*env)->SetIntArrayRegion(env, result, 0, len, nativeArray);

    CLEANUP:
    if (nativeString != NULL)
        (*env)->ReleaseStringUTFChars(env, javaString, nativeString);
    if (result == NULL && nativeArray != NULL)
        free(nativeArray);
	return result;
}




