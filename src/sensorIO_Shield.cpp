#include <jni.h>
#include <cassert>
#include <csignal>
#include <iostream>
#include "mraa.hpp"
#include "sensorIO_Shield.h"

#define SHIELD_I2C_ADDR 0x40
#define MS 1000

// From staff code
void initPWM(mraa::I2c *i2c) {
    uint8_t writeBuf[2] = { 0 };
    writeBuf[0] = 0x00; // Write to MODE 1 Register;
    writeBuf[1] = 1 << 4; // Enable Sleep Mode

    i2c->address(SHIELD_I2C_ADDR);
    i2c->write(writeBuf, 2);

    usleep(10 * MS);

    writeBuf[0] = 0xFE; // Write Prescaler Register
    writeBuf[1] = 0xA3; // Set pwm frequency to ~40 Hz

    i2c->address(SHIELD_I2C_ADDR);
    i2c->write(writeBuf, 2);

    writeBuf[0] = 0; // Write to the MODE 1 register
    writeBuf[1] = 1 << 5 // Enable auto increment mode
    | 0 << 4; // Enable the oscillator

    i2c->address(SHIELD_I2C_ADDR);
    i2c->write(writeBuf, 2);
}

JNIEXPORT jlong JNICALL Java_sensorIO_Shield_init(JNIEnv *env, jobject thisObj,
        jint bus) {
    mraa::I2c* shield = new mraa::I2c((int) bus);
    assert(shield != NULL);
    jlong pointer = (jlong) shield;
    initPWM(shield);
    return pointer;
}
