#include <stdio.h>
#include <stdlib.h>
#include <jni.h>
#include "MyInsertionSort.h"

JNIEXPORT jintArray JNICALL Java_MyInsertionSort_insertionsort
  (JNIEnv *env , jobject o, jintArray buf)
{
	jsize len;
	jint *my_array;
	jint *result;

	len = (*env)->GetArrayLength(env, buf);
	my_array = (jint *) (*env)->GetIntArrayElements(env, buf, 0);
	if (my_array == NULL) {
		printf("Cannot obtain array from JVM\n");
		exit(0);
	}

	for (int i = 1; i < len; i++) {
		int j = i;
		while (j > 0 && my_array[j-1] > my_array[j]) {
			jint tmp = my_array[j];
			my_array[j] = my_array[j-1];
			my_array[j-1] = tmp;
			j--;
		}
	}
	
	(*env)->ReleaseIntArrayElements(env, buf, my_array, 0); 
	return buf;
}

