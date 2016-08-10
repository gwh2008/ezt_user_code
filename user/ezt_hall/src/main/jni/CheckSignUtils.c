#include "com_eztcn_user_hall_utils_CheckSignUtils.h"
#include <stdio.h>
#include <stdlib.h>
#include <jni.h>
#include <string.h>
#include <android/log.h>
#include <malloc.h>
#include "md5c.h"

#define LOG_TAG "System.out.c"
#define LOGD(...) __android_log_print(ANDROID_LOG_DEBUG, LOG_TAG, __VA_ARGS__)
#define LOGI(...) __android_log_print(ANDROID_LOG_INFO, LOG_TAG, __VA_ARGS__)


/**
 * 工具方法
 * 返回值 char* 这个代表char数组的首地址
 *  Jstring2CStr 把java中的jstring的类型转化成一个c语言中的char 字符串
 */
char *Jstring2CStr(JNIEnv *env, jstring jstr) {
    char *rtn = NULL;
    jclass clsstring = (*env)->FindClass(env, "java/lang/String");
    jstring strencode = (*env)->NewStringUTF(env, "utf-8");
    jmethodID mid = (*env)->GetMethodID(env, clsstring, "getBytes",
                                        "(Ljava/lang/String;)[B");
    jbyteArray barr = (jbyteArray) (*env)->CallObjectMethod(env, jstr, mid,
                                                            strencode);
    jsize alen = (*env)->GetArrayLength(env, barr);
    jbyte *ba = (*env)->GetByteArrayElements(env, barr, JNI_FALSE);
    if (alen > 0) {
        rtn = (char *) malloc(alen + 1);
        memcpy(rtn, ba, alen);
        rtn[alen] = 0;
    }
    (*env)->ReleaseByteArrayElements(env, barr, ba, 0);
    return rtn;
}

/*
 * Class:     com_eztcn_user_hall_utils_CheckSignUtils
 * Method:    getSign
 * Signature: (Ljava/lang/String;[Ljava/lang/String;Ljava/util/Map;)Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL Java_com_eztcn_user_hall_utils_CheckSignUtils_getSign
        (JNIEnv *env, jclass obj, jstring str, jobjectArray stringArray, jobject hashMap){


    //获得java中的Map集合
    jclass jclass_of_hashmap = (*env)->GetObjectClass(env,hashMap);
    //得到getValue方法
    jmethodID getValueMethod = (*env)->GetMethodID(env,jclass_of_hashmap, "get", "()Ljava/lang/HashMap");
    jstring new_string;
    int stringCount = (*env)->GetArrayLength(env,stringArray);
//    for (int i=0; i<stringCount; i++) {
    jstring string = (jstring) (*env)->GetObjectArrayElement(env, stringArray, 0);
    const char *rawString = (*env)->GetStringUTFChars(env, string, 0);
    jstring js_value=(jstring)(*env)->CallObjectMethodA(env,jclass_of_hashmap,getValueMethod,string);
    // Don't forget to call `ReleaseStringUTFChars` when you're done.
    new_string = js_value;
//    }


    return (*env)->NewStringUTF(env,new_string);

}

/*
 * Class:     com_eztcn_user_hall_utils_CheckSignUtils
 * Method:    getAppId
 * Signature: ()Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL Java_com_eztcn_user_hall_utils_CheckSignUtils_getAppId
        (JNIEnv *env, jclass obj) {
    char *appId = "09721ab88e0a552087391be1ef0c6826";
    return (*env)->NewStringUTF(env, appId);
}

/*
 * Class:     com_eztcn_user_hall_utils_CheckSignUtils
 * Method:    MD5
 * Signature: (Ljava/lang/String;)Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL Java_com_eztcn_user_hall_utils_CheckSignUtils_MD5
        (JNIEnv *env, jclass obj, jstring str){
    char* jstr = Jstring2CStr(env, str);
    char* secretKey ="ca26e68b388b4ffcad4942fa648695fa";

    char *result = malloc(strlen(secretKey) + strlen(jstr) + 1);
    if (result == NULL)
        exit(1);
    strcpy(result, secretKey);
    strcat(result, jstr);

    MD5_CTX context = { 0 };
    MD5Init(&context);
    MD5Update(&context, result, strlen(result));
    unsigned char dest[16] = { 0 };
    MD5Final(dest, &context);

    int i;
    char destination[64]={0};
    for (i = 0; i < 16; i++) {
        sprintf(destination, "%s%02x", destination, dest[i]);
    }
//    LOGI("%s", destination);
    return (*env)->NewStringUTF(env, destination);
}