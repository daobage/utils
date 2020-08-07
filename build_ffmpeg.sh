make clean
NDK=../android-ndk-r17c
SYSROOT=$NDK/platforms/android-14/arch-arm
TOOLCHAIN=$NDK/toolchains/arm-linux-androideabi-4.9/prebuilt/linux-x86_64
CROSS_PREFIX=$TOOLCHAIN/bin/arm-linux-androideabi-
CPU=armv7-a
x264=./x264/
EXTRA_CFLAGS="-I./x264/include"
EXTRA_LDFLAGS="-L./x264/lib"
PREFIX=./libs
./configure \
	--target-os=android \
	--prefix=$PREFIX \
	--arch=arm \
	--cpu=armv7-a \
	--disable-doc \
	--disable-static \
	--disable-asm \
	--enable-cross-compile \
	--cross-prefix=$CROSS_PREFIX \
	--sysroot=$SYSROOT \
	--cc=$TOOLCHAIN/bin/arm-linux-androideabi-gcc \
	--enable-version3 \
	--enable-pthreads \
	--disable-network \
	--disable-symver \
	--disable-ffmpeg \
	--disable-ffplay \
	--disable-ffprobe \
	--disable-debug \
	--enable-neon \
	--enable-shared \
	--enable-small \
	--disable-libx264 \
	--enable-gpl \
	--enable-pic \
	--enable-jni \
	--enable-mediacodec \
	--enable-encoder=aac \
	--enable-encoder=gif \
	--enable-encoder=libopenjpeg \
	--enable-encoder=libmp3lame \
	--enable-encoder=libwavpack \
	--enable-encoder=mpeg4 \
	--enable-encoder=pcm_s16le \
	--enable-encoder=png \
	--enable-encoder=mjpeg \
	--enable-encoder=srt \
	--enable-encoder=subrip \
	--enable-encoder=yuv4 \
	--enable-encoder=text \
	--enable-decoder=aac \
	--enable-decoder=acc_latm \
	--enable-decoder=libopenjpeg \
	--enable-decoder=mp3 \
	--enable-decoder=mpeg4_mediacodec \
	--enable-decoder=pcm_s16le \
	--enable-decoder=flac \
	--enable-decoder=flv \
	--enable-decoder=gif \
	--enable-decoder=png \
	--enable-decoder=srt \
	--enable-decoder=xsub \
	--enable-decoder=yuv4 \
	--enable-decoder=vp8_mediacodec \
	--enable-decoder=h264_mediacodec \
	--enable-decoder=hevc_mediacodec \
	--enable-bsf=h264_mp4toannexb \
	--enable-bsf=hevc_mp4toannexb \
	--enable-protocols \
	--enable-hwaccels \
	--extra-cflags="$ADDI_CFLAGS -I$NDK/sysroot/usr/include/arm-linux-androideabi -isysroot $NDK/sysroot -fPIC -marm -march=armv7-a" \
	--extra-ldflags="$ADDI_LDFLAGS" \
	$ADDITIONAL_CONFIGURE_FLAG


echo "end!"
