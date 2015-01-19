#include <jni.h>
#include <stdio.h>
#include <iostream>
#include <array>
#include <opencv2/opencv.hpp>

using namespace cv;

JNIEXPORT void JNICALL Java_imageprocessing_ImageProcessor_process
(JNIEnv *env, jobject thisObj, jlong pointer) {
	Mat* image = (Mat*)pointer;
	int channels = image->channels();
	for (int i = 0; i < image->rows; i++) {
		for (int j = 0; j < image->cols; j++) {
			image->data[channels * (image->cols*i + j) + 0] = 255;
			image->data[channels * (image->cols*i + j) + 1] = 255;
			image->data[channels * (image->cols*i + j) + 2] = 98;
		}
	}
//	int size = image->total() * image->elemSize();
//	for (int i = 0; i < size; i++) {
//		if (i % 3 == 0) {
//			image->data[i] = 98;
//		}
//		else {
//			image->data[i] = 255;
//		}
//	}
}
