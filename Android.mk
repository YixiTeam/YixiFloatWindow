#
# Copyright (c) 2011 QUALCOMM Incorporated.
# All Rights Reserved.
# Qualcomm Confidential and Proprietary.
# Developed by QRD Engineering team.
#
LOCAL_PATH:= $(call my-dir)

include $(CLEAR_VARS)

LOCAL_STATIC_JAVA_LIBRARIES := android-support-v4 andbase arity guava umeng

LOCAL_MODULE_TAGS := optional

LOCAL_SRC_FILES := $(call all-java-files-under, src) \
	src/com/yixi/window/service/IMediaConnect.aidl

LOCAL_PACKAGE_NAME := MainActivity


LOCAL_CERTIFICATE := platform

include $(BUILD_PACKAGE)
##################################################
include $(CLEAR_VARS)

LOCAL_PREBUILT_STATIC_JAVA_LIBRARIES := andbase:libs/andbase.jar \
    arity:libs/arity-2.1.2.jar \
    umeng:libs/umeng-update-v2.4.2.jar

include $(BUILD_MULTI_PREBUILT)
# Use the folloing include to make our test apk.
include $(call all-makefiles-under,$(LOCAL_PATH))
