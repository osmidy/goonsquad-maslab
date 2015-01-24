#include <jni.h>
#include <stdio.h>
#include <math.h>
#include <iostream>
#include <array>
#include <opencv2/opencv.hpp>
#include "imageprocessing_ObjectFinder.h"

using namespace cv;

JNIEXPORT jboolean JNICALL Java_imageprocessing_ObjectFinder_checkCube
(JNIEnv *env, jobject thisObj, jlong pointer, jint x, jint y) {
	Mat* image = (Mat*) pointer;
	bool cubeCenter = false;
	int channels = image->channels();
	// Get distance in inches based on y-coordinate of pixel
	double distance = 59.5271 * pow((double)y, -0.6879);
	// Get expected area/pixel count based on distance from camera
	double minimumArea = pow(distance / 2274, 1 / -0.528);
	// Check surrounding pixels; assume shape is roughly square
	double radius = .5 * pow(minimumArea, .5);
	int count = 0;
	for (int i = y - radius; i >= 0 && i < y + radius; i++) {
		for (int j = x - radius; j >= 0 && j < x + radius; j++) {
			// Will change for specific color cubes; for now take red and green
			int val0 = image->data[channels * (image->cols * i + j) + 0];
			int val1 = image->data[channels * (image->cols * i + j) + 1];
			int val2 = image->data[channels * (image->cols * i + j) + 2];
			// if green or red
			if ((val0 == 0 && val1 == 255 && val2 == 0)
					|| (val0 == 0 && val1 == 0 && val2 == 255)) {
				count++;
			}
		}
	}
	// 80% threshold
	if (count >= minimumArea * .80) {
		cubeCenter = true;
	}
	return (jboolean) cubeCenter;
}
