#include <jni.h>
#include <stdio.h>
#include <math.h>
#include <iostream>
#include <array>
#include <opencv2/opencv.hpp>
#include "imageprocessing_ObjectFinder.h"

using namespace cv;

JNIEXPORT jboolean JNICALL Java_imageprocessing_ObjectFinder_checkCube(
		JNIEnv *env, jobject thisObj, jlong pointer, jint x, jint y) {
	Mat* image = (Mat*) pointer;
	int width = 320;
	int height = 240;
	double threshold;
	bool cubeCenter = false;
	int channels = image->channels();
	// Get distance in inches based on y-coordinate of pixel
	double distance = 2129 * pow((double) y, -0.876); //(0.0005 * pow((double)y, 2)) - (0.3803 * (double)y) + 73.968;
	// Get expected area/pixel count based on distance from camera
	double minimumArea = 610099 * pow(distance, -1.892);
	// Check surrounding pixels; assume shape is roughly square
	double radius = .5 * pow(minimumArea, .5);
	if (x < radius || x > width - radius) {
		threshold = 0.4;
	} else {
		threshold = 0.8;
	}
	int count = 0;
	for (int i = y - radius; i >= 0 && i < y + radius; i++) {
		for (int j = x - radius; j >= 0 && j < x + radius; j++) {
			// Will change for specific color cubes; for now take red and green
			int val0 = image->data[channels * (image->cols * i + j) + 0];
			int val1 = image->data[channels * (image->cols * i + j) + 1];
			int val2 = image->data[channels * (image->cols * i + j) + 2];
			// if green or red
			if (((val0 == 0) && (val1 == 255) && (val2 == 0))
					|| ((val0 == 0) && (val1 == 0) && (val2 == 255))) {
				count++;
			}
		}
	}
	if (count >= (minimumArea * threshold) && count != 0) {
		cubeCenter = true;
	}
	return (jboolean) cubeCenter;
}
