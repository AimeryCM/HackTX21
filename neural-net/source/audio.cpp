/* Edge Impulse Linux SDK
 * Copyright (c) 2021 EdgeImpulse Inc.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

#include <stdlib.h>
#include <stdint.h>
#include <stdio.h>
#include <pthread.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <unistd.h>
#include <signal.h>
#include "edge-impulse-sdk/classifier/ei_run_classifier.h"
#include <alsa/asoundlib.h>

// Forward declarations
int microphone_audio_signal_get_data(size_t, size_t, float *);

#define SLICE_LENGTH_MS      250        // 4 inferences per second
#define SLICE_LENGTH_VALUES  (EI_CLASSIFIER_RAW_SAMPLE_COUNT / (1000 / SLICE_LENGTH_MS))

static bool use_debug = false; // Set this to true to see e.g. features generated from the raw signal and log WAV files
static bool use_maf = false; // Set this (can be done from command line) to enable the moving average filter

static int16_t classifier_buffer[EI_CLASSIFIER_RAW_SAMPLE_COUNT * sizeof(int16_t)]; // full classifier buffer

// libalsa state
static snd_pcm_t *capture_handle;
static int channels = 1;
static unsigned int rate = EI_CLASSIFIER_FREQUENCY;
static snd_pcm_format_t format = SND_PCM_FORMAT_S16_LE;
static char *card;

/**
 * Classify the current buffer
 */
int classify_current_buffer(int16_t *stream) {
    memcpy(classifier_buffer, stream, EI_CLASSIFIER_RAW_SAMPLE_COUNT * sizeof(int16_t));
    // classify the current buffer and print the results
    signal_t signal;
    signal.total_length = EI_CLASSIFIER_RAW_SAMPLE_COUNT;
    signal.get_data = &microphone_audio_signal_get_data;
    ei_impulse_result_t result = { 0 };

    EI_IMPULSE_ERROR r = run_classifier(&signal, &result, use_debug);
    if (r != EI_IMPULSE_OK) {
        printf("ERR: Failed to run classifier (%d)\n", r);
        return -1;
    }
    int retCode = -1;
    for (size_t ix = 0; ix < EI_CLASSIFIER_LABEL_COUNT; ix++) {
        printf("%s: %.05f", result.classification[ix].label, result.classification[ix].value);
        if (ix != EI_CLASSIFIER_LABEL_COUNT - 1) {
            printf(", ");
        }
        if(result.classification[ix].value > 0.6) {
            if(strcmp(result.classification[ix].label, "car_horn") == 0){
                retCode = 0;
            }
            else{
                retCode = 1;
            }
        }
    }
    return retCode;
}
int main(int argc, char* argv){
    return 0;
}
/**
 * Get data from the classifier buffer
 */
int microphone_audio_signal_get_data(size_t offset, size_t length, float *out_ptr) {
    return numpy::int16_to_float(classifier_buffer + offset, out_ptr, length);
}


#if !defined(EI_CLASSIFIER_SENSOR) || EI_CLASSIFIER_SENSOR != EI_CLASSIFIER_SENSOR_MICROPHONE
#error "Invalid model for current sensor."
#endif
