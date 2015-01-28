#include <jni.h>
#include <stdio.h>
#include <math.h>
#include <iostream>
#include <array>
#include <opencv2/opencv.hpp>
#include "imageprocessing_ObjectFinder.h"

using namespace cv;
using namespace std;

JNIEXPORT jint JNICALL Java_imageprocessing_ObjectFinder_checkCube
  (JNIEnv *env, jobject thisObj, jlong pointer, jint x, jint y) {
    Mat* image = (Mat*)pointer;
    int channels = image->channels();
    int width = 160;
    int height = 120;
    int notCenter = 33;
    int redCenter = 44;
    int greenCenter = 55;
    int cubeCenter;
    double inputX = (double)x;
    double inputY = (double)y;
    double threshold;
    // Get distance in inches based on y
    double distance = (0.0029 * pow(inputY, 2.0)) - (0.8845 * inputY) + 77.271;
    // Get expected area/pixel count based on distance from camera
    double minimumArea = 142538 * pow(distance, -1.839);
    // Check surrounding pixels (roughly square/rectangle shape)
    double radius = 0.5 * pow(minimumArea, 0.5);
    if (inputX < radius || inputX > (width - radius) || inputY < radius || inputY > (height - radius)) {
        threshold = 0.35;
    }
    else {
        threshold = 0.7;
    }
    // Apply threshold to area
    threshold *= minimumArea;
    // Boundaries for iteration
    int lowerX = (int)(x - radius);
    int upperX = (int)(x + radius);
    int lowerY = (int)(y - radius);
    int upperY = (int)(y + radius);

    int greenCount = 0;
    int redCount = 0;
    for (int i = lowerY; i >= 0 && i < upperY && i < height; i+=2) {
        for (int j = lowerX; j >= 0 && j < upperX && j < width; j+=2) {
            int val0 = image->data[channels * (image->cols*i+j) + 0];
            int val1 = image->data[channels * (image->cols*i+j) + 1];
            int val2 = image->data[channels * (image->cols*i+j) + 2];
            if ((val0 == 0) && (val1 == 255) && (val2 == 0)) {
                greenCount++;
            }
            else if ((val0 == 0) && (val1 == 0) && (val2 == 255)) {
                redCount++;
            }
        }
    }
    if ((redCount > 0) && (minimumArea > redCount) && (redCount >= threshold)) {
        cubeCenter = redCenter;
    }
    else if ((greenCount > 0) && (minimumArea > greenCount) && (greenCount >= threshold)) {
        cubeCenter = greenCenter;
    }
    else {
        cubeCenter = notCenter;
    }
    return (jint) cubeCenter;
}
