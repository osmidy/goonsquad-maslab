#include <jni.h>
#include <cassert>
#include <csignal>
#include <iostream>
#include "mraa.hpp"
#include "robotparts_Servo.h"

#define SHIELD_I2C_ADDR 0x40

uint8_t registers[] = {
  6,   // output 0
  10,  // output 1
  14,  // output 2
  18,  // output 3
  22,  // output 4
  26,  // output 5
  30,  // output 6
  34,  // output 7
  38,  // output 8
  42,  // output 9
  46,  // output 10
  50,  // output 11
  54,  // output 12
  58,  // output 13
  62,  // output 14
  66   // output 15
};

JNIEXPORT void JNICALL Java_robotparts_Servo_setPosition
(JNIEnv *env, jobject thisObj, jlong pointer, jint indx, jdouble dty) {
    mraa::I2c* i2c = (mraa::I2c*)pointer;
    int index = (int)indx;
    double duty = 0.04 * (double)dty + 0.04;
    // From staff code
    assert(0.0 <= duty && duty <= 1.0);
    assert(0 <= index && index < 16);
    double on = 4095.0 * duty;
    uint16_t onRounded = (uint16_t) on;

    uint8_t writeBuf[5];

    // ON_L
    writeBuf[0] = registers[index];
    writeBuf[1] = 0x00;// ON LSB
    writeBuf[2] = 0x00;// ON MSB
    writeBuf[3] = onRounded & 0xFF;// OFF LSB
    writeBuf[4] = (onRounded >> 8) & 0xFF;// OFF MSB
    i2c->address(SHIELD_I2C_ADDR);
    i2c->write(writeBuf, 5);
}
