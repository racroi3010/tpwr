LOCAL_PATH := $(call my-dir) 
include $(CLEAR_VARS)
LOCAL_MODULE := JNIHanaone
LOCAL_SRC_FILES := JNIHanaone.cpp

LOCAL_LDLIBS :=  -llog -ldl

include $(BUILD_SHARED_LIBRARY)
