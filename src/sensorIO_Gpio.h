/* DO NOT EDIT THIS FILE - it is machine generated */
#include <jni.h>
/* Header for class sensorIO_Gpio */

#ifndef _Included_sensorIO_Gpio
#define _Included_sensorIO_Gpio
#ifdef __cplusplus
extern "C" {
#endif
/*
 * Class:     sensorIO_Gpio
 * Method:    init
 * Signature: (ILjava/lang/String;)J
 */
JNIEXPORT jlong JNICALL Java_sensorIO_Gpio_init
  (JNIEnv *, jobject, jint, jstring);

/*
 * Class:     sensorIO_Gpio
 * Method:    read
 * Signature: (J)I
 */
JNIEXPORT jint JNICALL Java_sensorIO_Gpio_read
  (JNIEnv *, jobject, jlong);

/*
 * Class:     sensorIO_Gpio
 * Method:    write
 * Signature: (JI)V
 */
JNIEXPORT void JNICALL Java_sensorIO_Gpio_write
  (JNIEnv *, jobject, jlong, jint);

#ifdef __cplusplus
}
#endif
#endif
