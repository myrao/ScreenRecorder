package net.yrom.screenrecorder.core;

import android.media.MediaCodecInfo;

/**
 * Created by raomengyang on 5/3/17.
 */
public class YuvUtil {

    //yuv420FormatIndex仅支持3种取值: MediaCodecInfo.CodecCapabilities#COLOR_FormatYUV420Planar, #COLOR_FormatYUV420PackedPlanar, #COLOR_FormatYUV420SemiPlanar
    public static void convertYV12ToSpecifiedYUV420(final byte[] capturedYUVData, final byte[] convertedYUVData, final int yuv420FormatIndex, final int width, final int height) {
        // color space transform.
        if (yuv420FormatIndex == MediaCodecInfo.CodecCapabilities.COLOR_FormatYUV420Planar) {
            convertYV12ToYUV420Planar(capturedYUVData, convertedYUVData, width, height);
        } else if (yuv420FormatIndex == MediaCodecInfo.CodecCapabilities.COLOR_FormatYUV420PackedPlanar) {
            convertYV12ToYUV420PackedSemiPlanar(capturedYUVData, convertedYUVData, width, height);
        } else if (yuv420FormatIndex == MediaCodecInfo.CodecCapabilities.COLOR_FormatYUV420SemiPlanar) {
            convertYV12ToYUV420PackedSemiPlanar(capturedYUVData, convertedYUVData, width, height);
        } else {
            System.arraycopy(capturedYUVData, 0, convertedYUVData, 0, capturedYUVData.length);
        }
    }

    // the color transform, @see http://stackoverflow.com/questions/15739684/mediacodec-and-camera-color-space-incorrect
    private static byte[] convertYV12ToYUV420PackedSemiPlanar(final byte[] input, final byte[] output, final int width, final int height) {
        /*
         * COLOR_TI_FormatYUV420PackedSemiPlanar is NV12
         * We convert by putting the corresponding U and V bytes together (interleaved).
         */
        final int frameSize = width * height;
        final int qFrameSize = frameSize / 4;

        System.arraycopy(input, 0, output, 0, frameSize); // Y

        for (int i = 0; i < qFrameSize; i++) {
            output[frameSize + i * 2] = input[frameSize + i + qFrameSize]; // Cb (U)
            output[frameSize + i * 2 + 1] = input[frameSize + i]; // Cr (V)
        }
        return output;
    }

    private static byte[] convertYV12ToYUV420Planar(byte[] input, byte[] output, int width, int height) {
        /*
         * COLOR_FormatYUV420Planar is I420 which is like YV12, but with U and V reversed.
         * So we just have to reverse U and V.
         */
        final int frameSize = width * height;
        final int qFrameSize = frameSize / 4;

        System.arraycopy(input, 0, output, 0, frameSize); // Y
        System.arraycopy(input, frameSize, output, frameSize + qFrameSize, qFrameSize); // Cr (V)
        System.arraycopy(input, frameSize + qFrameSize, output, frameSize, qFrameSize); // Cb (U)

        return output;
    }
}
