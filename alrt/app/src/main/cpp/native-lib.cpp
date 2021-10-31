#include <jni.h>
#include <string>

extern "C" JNIEXPORT jstring JNICALL
Java_com_example_alrt_MainActivity_stringFromJNI(
        JNIEnv* env,
        jobject /* this */,
        jstring test) {
    return env->NewStringUTF(env->GetStringUTFChars(test, 0));
}