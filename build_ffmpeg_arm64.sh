#!/bin/bash
NDK=/home/xinlai/Downloads/android-ndk-r17c

function build_android
{
echo "Compiling FFmpeg for $CPU"
./configure \
    --prefix=$PREFIX \
    --enable-neon \
    --enable-hwaccels \
    --enable-gpl \
    --enable-postproc \
    --enable-shared \
    --enable-jni \
    --enable-mediacodec \
    --enable-decoder=h264_mediacodec \
    --disable-static \
    --disable-doc \
    --enable-ffmpeg \
    --disable-ffplay \
    --disable-ffprobe \
    --enable-avdevice \
    --disable-doc \
    --disable-symver \
    --cross-prefix=$CROSS_PREFIX \
    --target-os=android \
    --arch=$ARCH \
    --cpu=$CPU \
    --enable-cross-compile \
    --sysroot=$SYSROOT \
    --extra-cflags="-Os -fpic $OPTIMIZE_CFLAGS" \
    --extra-ldflags="$ADDI_LDFLAGS" \
    $ADDITIONAL_CONFIGURE_FLAG
make clean
make
make install
echo "The Compilation of FFmpeg for $CPU is completed"
}

#armv8-a
ARCH=arm64
CPU=armv8-a
TOOLCHAIN=$NDK/toolchains/aarch64-linux-android-4.9/prebuilt/linux-x86_64
SYSROOT=$NDK/platforms/android-21/arch-$ARCH/
CROSS_PREFIX=$TOOLCHAIN/bin/aarch64-linux-android-
PREFIX=$(pwd)/android/$CPU
OPTIMIZE_CFLAGS="-march=$CPU"
build_android

#armv7-a
ARCH=arm
CPU=armv7-a
TOOLCHAIN=$NDK/toolchains/arm-linux-androideabi-4.9/prebuilt/linux-x86_64
SYSROOT=$NDK/platforms/android-21/arch-$ARCH/
CROSS_PREFIX=$TOOLCHAIN/bin/arm-linux-androideabi-
PREFIX=$(pwd)/android/$CPU
OPTIMIZE_CFLAGS="-mfloat-abi=softfp -mfpu=vfp -marm -march=$CPU "
build_android

#x86
#ARCH=x86
#CPU=x86
#TOOLCHAIN=$NDK/toolchains/x86-4.9/prebuilt/linux-x86_64
#SYSROOT=$NDK/platforms/android-21/arch-$ARCH/
#CROSS_PREFIX=$TOOLCHAIN/bin/i686-linux-android-
#PREFIX=$(pwd)/android/$CPU
#OPTIMIZE_CFLAGS="-march=i686 -mtune=intel -mssse3 -mfpmath=sse -m32"
#build_android

#x86_64
#ARCH=x86_64
#CPU=x86-64
#TOOLCHAIN=$NDK/toolchains/x86_64-4.9/prebuilt/linux-x86_64
#SYSROOT=$NDK/platforms/android-21/arch-$ARCH/
#CROSS_PREFIX=$TOOLCHAIN/bin/x86_64-linux-android-
#PREFIX=$(pwd)/android/$CPU
#OPTIMIZE_CFLAGS="-march=$CPU -msse4.2 -mpopcnt -m64 -mtune=intel"
#build_android

