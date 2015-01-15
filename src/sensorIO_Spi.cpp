#include <jni.h>
#include <stdio.h>
#include <math.h>
#include "mraa.hpp"
#include "sensorIO_Spi.h"

JNIEXPORT jlong JNICALL Java_sensorIO_Spi_init
  (JNIEnv *env, jobject thisObj, jint bus) {
	mraa::Spi* spi = new mraa::Spi((int)bus);
	spi->bitPerWord(32);
	jlong pointer = (jlong)spi;
	return pointer;
}

JNIEXPORT jchar JNICALL Java_sensorIO_Spi_write__JC
  (JNIEnv *env, jobject thisObj, jlong pointer, jchar data) {
	mraa::Spi* spi = (mraa::Spi*)pointer;
	uint8_t dat = (uint8_t)data;
	char returnVal = spi->write(dat);
	return returnVal;
}

JNIEXPORT jchar JNICALL Java_sensorIO_Spi_write__JCI
  (JNIEnv *env, jobject thisObj, jlong pointer, jchar data, jint length) {
	mraa::Spi* spi = (mraa::Spi*)pointer;
	uint8_t* dat = (uint8_t*)data;
	int len = (int)length;
	jchar returnVal = (jchar)spi->write(dat, len);
	return returnVal;
}

JNIEXPORT void JNICALL Java_sensorIO_Spi_bitPerWord
  (JNIEnv *env, jobject thisObj, jlong pointer, jint bits) {
	mraa::Spi* spi = (mraa::Spi*)pointer;
	spi->bitPerWord(bits);
}
