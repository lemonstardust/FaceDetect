project(scrfdncnn)

cmake_minimum_required(VERSION 3.10)

set(OpenCV_DIR ${CMAKE_SOURCE_DIR}/opencv-mobile/sdk/native/jni)
find_package(OpenCV REQUIRED core imgproc)

set(ncnn_DIR ${CMAKE_SOURCE_DIR}/ncnn-20240820-android-vulkan/${ANDROID_ABI}/lib/cmake/ncnn)
find_package(ncnn REQUIRED)

add_library(scrfdncnn SHARED scrfdncnn.cpp scrfd.cpp ndkcamera.cpp)

target_link_libraries(scrfdncnn ncnn ${OpenCV_LIBS} camera2ndk mediandk)