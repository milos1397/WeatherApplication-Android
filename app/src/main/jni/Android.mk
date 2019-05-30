LOCAL_PATH := $(call my-dir)
include $(CLEAR_VARS)
LOCAL_MODULE := MyJNI
LOCAL_SRC_FILES := convert.c
include $(BUILD_SHARED_LIBRARY)