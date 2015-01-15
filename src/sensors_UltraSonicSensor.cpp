#include <jni.h>
#include <stdio.h>
#include <csignal>
#include <iostream>
#include <sys/time.h>

#include "mraa.hpp"
#include "sensors_UltraSonicSensor.h"

/**Implements ultrasonic distance measurements.  Makes use of staff code */

struct info {
	mraa::Gpio* echo;
	double distance;
	bool checking;
};

void echo_handler(void* args) {
	info data = (info)args;
	static struct timeval start;
	bool rising = data.echo->read() == 1;
	if (rising) {
		gettimeofday(&start, NULL);
	}
	else {
		struct timeval end;
		gettimeofday(&end, NULL);

		int diffSec = end.tv_sec - start.tv_sec;
		int diffUSec = end.tv_usec - start.tv_usec;
		double diffTime = (double) diffSec + 0.000001 * diffUSec;
		// Speed of sound conversion: 340m/s * 0.5 (round trip)
		data.distance = diffTime * 170.0;
		data.checking = false;

	}
}

JNIEXPORT jdouble JNICALL Java_sensors_UltraSonicSensor_echoHandle(JNIEnv *env,
		jobject thisObj, jlong echoPointer, jlong trigPointer) {
	info data;
	data.echo = (mraa::Gpio*)echoPointer;
	data.checking = true;
	mraa::Gpio* trig = (mraa::Gpio*)trigPointer;
	data.echo->isr(mraa::EDGE_BOTH, echo_handler, data);

	while(data.checking) {
		// minimum 10us trigger pulse
		trig->write(1);
		usleep(20);
		trig->write(0);
	}
	data.echo->isrExit();
	jdouble distance = (jdouble)data.distance;
	return distance;

}


