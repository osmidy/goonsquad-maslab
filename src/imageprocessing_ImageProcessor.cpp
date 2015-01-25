#include <jni.h>
#include <stdio.h>
#include <iostream>
#include <array>
#include <opencv2/opencv.hpp>
#include "imageprocessing_ImageProcessor.h"

using namespace cv;
using namespace std;

JNIEXPORT void JNICALL Java_imageprocessing_ImageProcessor_process
(JNIEnv *env, jobject thisObj, jlong pointer) {
	Mat* image = (Mat*)pointer;

	float rgRatio = 2.1f; //1.5f; // 1.8f;
	float rbRatio = 2.0f; //1.3f; // 1.7f;
	float gbRatio = 1.0f;
	float grRatio = 1.4f;
	float bgRatio = 1.3f;
	float brRatio = 2.1f;
	float rgYellowRatio = 1.05f;
	float rbYellowRatio = 1.7f;
	float bgPurpleRatio = 1.4f;
	float brPurpleRatio = 1.25;

	int channels = image->channels();
	for (int i = 0; i < image->rows; i++) {
		for (int j = 0; j < image->cols; j++) {
			uchar b = image->data[channels * (image->cols*i + j) + 0];
			uchar g = image->data[channels * (image->cols*i + j) + 1];
			uchar r = image->data[channels * (image->cols*i + j) + 2];

			float blue = b / 255.0f;
			float green = g / 255.0f;
			float red = r / 255.0f;

			if (blue/green > bgRatio && blue/red > brRatio) {
				image->data[channels * (image->cols*i + j) + 0] = 255;
				image->data[channels * (image->cols*i + j) + 1] = 0;
				image->data[channels * (image->cols*i + j) + 2] = 0;
			}
			else if (green/red > grRatio && green/blue > gbRatio) {
				image->data[channels * (image->cols*i + j) + 0] = 0;
				image->data[channels * (image->cols*i + j) + 1] = 255;
				image->data[channels * (image->cols*i + j) + 2] = 0;
			}
			else if (red/green > rgRatio && red/blue > rbRatio) {
				image->data[channels * (image->cols*i + j) + 0] = 0;
				image->data[channels * (image->cols*i + j) + 1] = 0;
				image->data[channels * (image->cols*i + j) + 2] = 255;
			}
			else if (red/green > rgYellowRatio && red/blue > rbYellowRatio) {
				image->data[channels * (image->cols*i + j) + 0] = 0;
				image->data[channels * (image->cols*i + j) + 1] = 255;
				image->data[channels * (image->cols*i + j) + 2] = 255;
			}
			else if (blue/green > bgPurpleRatio && blue/red > brPurpleRatio) {
				image->data[channels * (image->cols*i + j) + 0] = 128;
				image->data[channels * (image->cols*i + j) + 1] = 0;
				image->data[channels * (image->cols*i + j) + 2] = 128;
			}
			else {
				image->data[channels * (image->cols*i + j) + 0] = 0;
				image->data[channels * (image->cols*i + j) + 1] = 0;
				image->data[channels * (image->cols*i + j) + 2] = 0;
			}

		}
	}
}
