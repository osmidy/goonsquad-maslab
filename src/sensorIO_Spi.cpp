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

JNIEXPORT void JNICALL Java_sensorIO_Spi_bitPerWord
  (JNIEnv *env, jobject thisObj, jlong pointer, jint bits) {
	mraa::Spi* spi = (mraa::Spi*)pointer;
	spi->bitPerWord(bits);
}
