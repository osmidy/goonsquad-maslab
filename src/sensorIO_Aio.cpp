#include <jni.h>
#include <stdio.h>
#include <csignal>
#include <iostream>

#include "mraa.hpp"
#include "sensorIO_Aio.h"

JNIEXPORT jlong JNICALL Java_sensorIO_Aio_init
  (JNIEnv *env, jobject thisObj, jint pin) {
	mraa::Aio* aio = new mraa::Aio((int)pin);
	jlong pointer = (jlong(aio));
	return pointer;
}

JNIEXPORT jint JNICALL Java_sensorIO_Aio_read
  (JNIEnv *env, jobject thisObj, jlong pointer) {
	mraa::Aio* aio = (mraa::Aio*)pointer;
	jint reading = (jint)aio->read();
	return reading;
}

JNIEXPORT void JNICALL Java_sensorIO_Aio_setBit
  (JNIEnv *env, jobject thisObj, jlong pointer, jint bits) {
	mraa::Aio* aio = (mraa::Aio*)pointer;
	aio->setBit(bits);
}

JNIEXPORT jint JNICALL Java_sensorIO_Aio_getBit
  (JNIEnv *env, jobject thisObj, jlong pointer) {
	mraa::Aio* aio = (mraa::Aio*)pointer;
	jint bit = (jint)aio->getBit();
	return bit;
}

