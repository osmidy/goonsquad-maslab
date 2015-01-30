#include <jni.h>
#include <stdio.h>
#include <math.h>
#include <iostream>
#include <array>
#include <opencv2/opencv.hpp>
#include "imageprocessing_ObjectFinder.h"

using namespace cv;
using namespace std;

JNIEXPORT jint JNICALL Java_imageprocessing_ObjectFinder_getObjectType
  (JNIEnv *env, jobject thisObj, jlong pointer, jint x, jint y) {
    Mat* image = (Mat*)pointer;
    int channels = image->channels();
    int width = 160;
    int height = 120;

    int notObject = 33;
    int redCenter = 44;
    int greenCenter = 55;
    int wall = 66;
    int dropzone = 77;
    int objectType;

    double inputX = (double)x;
    double inputY = (double)y;
    double threshold;
    // Get distance in inches based on y
    double distance = (0.0029 * pow(inputY, 2.0)) - (0.8845 * inputY) + 77.271;
    // Get expected area/pixel count for a cube based on distance from camera
    double minimumArea = 142538 * pow(distance, -1.839);
    // Check surrounding pixels (roughly square/rectangle shape)
    double radius = 0.5 * pow(minimumArea, 0.5);
    if (inputX < radius || inputX > (width - radius) || inputY < radius || inputY > (height - radius)) {
        threshold = 0.05; // 0.35;
    }
    else {
        threshold = 0.15; // 0.7;
    }
    // Apply threshold to area
    threshold *= minimumArea;
    // Boundaries for iteration
    int lowerX = (int)(x - radius);
    int upperX = (int)(x + radius);
    int lowerY = (int)(y - radius);
    int upperY = (int)(y + radius);

    int innerLowerX = (int)(y - (radius * 0.9));
    int innerUpperX = (int)(y + (radius * 0.9));
    int innerLowerY = (int)(y - (radius * 0.9));
    int innerUpperY = (int)(y + (radius * 0.9));

    // Colors
    array<int, 3> blue {255, 0, 0};
    array<int, 3> green {0, 255, 0};
    array<int, 3> red {0, 0, 255};
    array<int, 3> yellow {0, 255, 255};
    array<int, 3> purple {128, 0, 128};
    array<int, 3> black {0, 0, 0};

    int pix0 = image->data[channels * (image->cols*y + x) + 0];
    int pix1 = image->data[channels * (image->cols*y + x) + 1];
    int pix2 = image->data[channels * (image->cols*y + x) + 2];
    array<int, 3> pixelColor {pix0, pix1, pix2};

    if ((pixelColor == blue) || (pixelColor == yellow)) {
    	objectType = wall;
    } else if (pixelColor == purple) {
    	objectType = dropzone;
    } else {
    	// Check if center of cube
		int greenCount = 0;
		int redCount = 0;
		for (int i = lowerY; i >= 0 && i < upperY && i < height; i+=2) {
			for (int j = lowerX; j >= 0 && j < upperX && j < width; j+=2) {
				if (!(((i > innerLowerY ) && (i < innerUpperY)) && ((j > innerLowerX) && (j < innerUpperX)))) {
					int val0 = image->data[channels * (image->cols*i+j) + 0];
					int val1 = image->data[channels * (image->cols*i+j) + 1];
					int val2 = image->data[channels * (image->cols*i+j) + 2];
					array<int, 3> color {val0, val1, val2};
					if (color == green) {
						greenCount++;
					}
					else if (color == red) {
						redCount++;
					}
				}
			}
		}
		if ((redCount > 0) && (minimumArea > redCount) && (redCount >= threshold)) {
			objectType = redCenter;
		}
		else if ((greenCount > 0) && (minimumArea > greenCount) && (greenCount >= threshold)) {
			objectType = greenCenter;
		}
		else {
			objectType = notObject;
		}
    }
    return (jint) objectType;
}
