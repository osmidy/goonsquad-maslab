#include <jni.h>
#include <stdio.h>
#include <unistd.h>
#include <stdint.h>
#include <signal.h>
#include <sys/time.h>

#include "mraa.hpp"
#include "robotparts_Gyroscope.h"

#define MS 1000

/**
 * Implements reading of heading from Gyroscope;
 * Makes use of staff code
 */

float readSensor(char* recv) {
	int init = 0;
	struct timeval tv;
	float rf;
	float total = 0;
	unsigned int recvVal = ((uint8_t) recv[3] & 0xFF);
	recvVal = (recvVal << 8) | ((uint8_t) recv[2] & 0xFF);
	recvVal = (recvVal << 8) | ((uint8_t) recv[1] & 0xFF);
	recvVal = (recvVal << 8) | ((uint8_t) recv[0] & 0xFF);
	short reading = (recvVal >> 10) & 0xffff;
//	if (init) {
//		unsigned long long ms = (unsigned long long) (tv.tv_sec) * 1000
//				+ (unsigned long long) (tv.tv_usec) / 1000;
//		gettimeofday(&tv, NULL);
//		ms -= (unsigned long long) (tv.tv_sec) * 1000
//				+ (unsigned long long) (tv.tv_usec) / 1000;
//		int msi = (int) ms;
//		float msf = (float) msi;
//		rf = (float) reading;
//        total += -0.001 * msf * (rf / 80.0);
//	} else {
//		init = 1;
//		gettimeofday(&tv, NULL);
//		usleep(10 * MS);
//		total = readSensor(recv);
//	}
	total = (float)reading;
	total /= 80.0;
	return total;
}

jdouble fixDrift(float drift) {
	// TODO: Adjust for driting
	float correct = drift;
	return (jdouble)correct;

}

JNIEXPORT jdouble JNICALL Java_robotparts_Gyroscope_getAngularVelocity(JNIEnv *env,
		jobject thisObj, jlong chipPointer, jlong spiPointer) {
	mraa::Gpio* chipSelect = (mraa::Gpio*) chipPointer;
	mraa::Spi* spi = (mraa::Spi*) spiPointer;
	chipSelect->write(1);
	spi->bitPerWord(32);
	char rxBuf[2];
	char writeBuf[4];
	unsigned int sensorRead = 0x20000000;
	writeBuf[0] = sensorRead & 0xff;
	writeBuf[1] = (sensorRead >> 8) & 0xff;
	writeBuf[2] = (sensorRead >> 16) & 0xff;
	writeBuf[3] = (sensorRead >> 24) & 0xff;
	float total = 0;
	struct timeval tv;
	chipSelect->write(0);
	char* recv = spi->write(writeBuf, 4);
	chipSelect->write(1);
	if (recv != NULL) {
		float driftedAngle = readSensor(recv);
		jdouble correctedAngle = fixDrift(driftedAngle);
		return correctedAngle;

	} else {
		return 0.03421;  // Indicating recv was null
	}
}
