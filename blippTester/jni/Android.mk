LOCAL_PATH := $(call my-dir)
 
include $(CLEAR_VARS)
 
# Here we give our module name and source file(s)
LOCAL_MODULE    := blipp_native
LOCAL_SRC_FILES := blipp_native.c
 
include $(BUILD_SHARED_LIBRARY)