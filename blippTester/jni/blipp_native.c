#include "anjelloatoz_blippAR_BlippTesterActivity.h"
#include <string.h>
#include <jni.h>

JNIEXPORT jstring JNICALL Java_anjelloatoz_blippAR_BlippTesterActivity_invokeFirstNativeFunction(JNIEnv * env, jobject obj) {
		return (*env)->NewStringUTF(env, "First native function.");
}

JNIEXPORT jstring JNICALL Java_anjelloatoz_blippAR_BlippTesterActivity_invokeSecondNativeFunction
(JNIEnv * env, jobject obj){
		return (*env)->NewStringUTF(env, "Second native function.");
}

JNIEXPORT jstring JNICALL Java_anjelloatoz_blippAR_BlippTesterActivity_invokeThirdNativeFunction
(JNIEnv * env, jobject obj)
{
		return (*env)->NewStringUTF(env, "Third native function.");
}