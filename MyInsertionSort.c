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
	jboolean *is_copy = 0;

	len = (*env)->GetArrayLength(env, buf);
	my_array = (jint *) (*env)->GetIntArrayElements(env, buf, is_copy);
	if (my_array == NULL) {
		printf("Cannot obtain array from JVM\n");
		exit(0);
	}
	for (int i = 0; i < len; i++) my_array[i] = len - i - 1;
	
	if (*is_copy == JNI_TRUE) {
		(*env)->ReleaseIntArrayElements(env, buf, my_array, (jint)0); 
	}
	return buf;
}

