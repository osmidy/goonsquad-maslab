#include <jni.h>
#include <stdio.h>
#include "mraa.hpp"
#include "sensorIO_Pwm.h"

JNIEXPORT jlong JNICALL Java_sensorIO_Pwm_init
  (JNIEnv *env, jobject thisObj, jint pin) {
	mraa::Pwm pwm = new mraa::Pwm((int)pin);
	pwm.write(0.0);
	pwm.enable(true);
	jlong pointer = (jlong)&pwm;
	return pointer;
}

JNIEXPORT void JNICALL Java_sensorIO_Pwm_setSpeed
  (JNIEnv *env, jobject thisObj, jlong pointer, jdouble speed) {
	mraa::Pwm pwm = (mraa::Pwm)*pointer;
	float absSpeed = fabs((double)speed);
	pwm.write(absSpeed);
}

JNIEXPORT jdouble JNICALL Java_sensorIO_Pwm_getSpeed
  (JNIEnv *env, jobject pointer, jlong pointer) {
	mraa::Pwm pwm = (mraa::Pwm)*pointer;
	jdouble speed = (jdouble)pwm.read();
	return speed;
}

JNIEXPORT void JNICALL Java_sensorIO_Pwm_setEnableStatus
  (JNIEnv *env, jobject thisObj, jlong pointer, jboolean enable) {
	mraa::Pwm pwm = (mraa::Pwm)*pointer;
	bool boolean = (bool)enable;
	pwm.enable(boolean);
}

