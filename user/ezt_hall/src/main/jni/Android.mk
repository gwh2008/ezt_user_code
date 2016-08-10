LOCAL_PATH := $(call my-dir)
include $(CLEAR_VARS)

LOCAL_MODULE := EztSo
LOCAL_SRC_FILES := CheckSignUtils.c md5c.c

#liblog.so libGLESv2.so
LOCAL_LDLIBS += -llog
include $(BUILD_SHARED_LIBRARY)