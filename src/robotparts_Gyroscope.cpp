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

float readSensor(uint8_t* recv) {
	int init = 0;
	struct timeval tv;
	float rf;
	float total = 0;
	unsigned int recvVal = ((uint8_t) recv[3] & 0xFF);
	recvVal = (recvVal << 8) | ((uint8_t) recv[2] & 0xFF);
	recvVal = (recvVal << 8) | ((uint8_t) recv[1] & 0xFF);
	recvVal = (recvVal << 8) | ((uint8_t) recv[0] & 0xFF);
	short reading = (recvVal >> 10) & 0xffff;
	total = (float)reading;
	total /= 80.0;
	return total;
}

//jdouble fixDrift(float drift) {
//	// TODO: Adjust for driting
//	float correct = drift;
//	return (jdouble)correct;

//}

JNIEXPORT jdouble JNICALL Java_robotparts_Gyroscope_getAngularVelocity(JNIEnv *env,
		jobject thisObj, jlong chipPointer, jlong spiPointer) {
	mraa::Gpio* chipSelect = (mraa::Gpio*) chipPointer;
	mraa::Spi* spi = (mraa::Spi*) spiPointer;
	chipSelect->write(1);
	spi->bitPerWord(32);
	char rxBuf[2];
	uint8_t writeBuf[4];
	unsigned int sensorRead = 0x20000000;
	writeBuf[0] = sensorRead & 0xff;
	writeBuf[1] = (sensorRead >> 8) & 0xff;
	writeBuf[2] = (sensorRead >> 16) & 0xff;
	writeBuf[3] = (sensorRead >> 24) & 0xff;
	float total = 0;
	struct timeval tv;
	chipSelect->write(0);
	uint8_t* recv = spi->write(writeBuf, 4);
	chipSelect->write(1);
	if (recv != NULL) {
		float driftedAngle = readSensor(recv);
		//jdouble correctedAngle = fixDrift(driftedAngle);
		return driftedAngle;

	} else {
		return 0.03421;  // Indicating recv was null
	}
}
