//
// Created by IlijaK on 5/28/2019.
//
#include "myJni.h"

JNIEXPORT jint JNICALL Java_r_rtrk_weatherforecast_MyNDK_convert
        (JNIEnv *env, jobject obj, jint x, jint y){

    double help=(double)x;
    if(y == 0){
        return x;
    }else{
        help=(help-32)*((double)5/(double)9);
    }
    //printf("%f",help);

    return help;
}
