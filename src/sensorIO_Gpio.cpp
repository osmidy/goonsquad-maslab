#include <jni.h>
#include <stdio.h>
#include <cstring>

#include "mraa.hpp"
#include "sensorIO_Gpio.h"

JNIEXPORT jlong JNICALL Java_sensorIO_Gpio_init
  (JNIEnv *env, jobject thisObj, jint pin, jstring direction) {
	mraa::Gpio* gpio = new mraa::Gpio((int)pin);
	jlong pointer = (jlong)gpio;
	const char *dir = env->GetStringUTFChars(direction, NULL);
	if (strcmp (dir, "out") == 0) {
		gpio->dir(mraa::DIR_OUT);
	}
	else {
		gpio->dir(mraa::DIR_IN);
	}
	gpio->write(1);
	return pointer;
}

JNIEXPORT jint JNICALL Java_sensorIO_Gpio_read
  (JNIEnv *env, jobject thisObj, jlong pointer) {
	mraa::Gpio* gpio = (mraa::Gpio*)pointer;
	jint reading = (jint)gpio->read();
	return reading;
}

JNIEXPORT void JNICALL Java_sensorIO_Gpio_write
  (JNIEnv *env, jobject thisObj, jlong pointer, jint value) {
	mraa::Gpio* gpio = (mraa::Gpio*)pointer;
	gpio->write((int)value);
}
