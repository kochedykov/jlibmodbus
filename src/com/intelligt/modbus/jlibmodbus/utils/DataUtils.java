package com.intelligt.modbus.jlibmodbus.utils;

import java.util.Locale;

/*
 * Copyright (C) 2016 "Invertor" Factory", JSC
 * [http://www.sbp-invertor.ru]
 *
 * This file is part of JLibModbus.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Authors: Vladislav Y. Kochedykov, software engineer.
 * email: vladislav.kochedykov@gmail.com
 */
public class DataUtils {

    static public int fromAscii(char h, char l) {
        try {
            return Integer.parseInt("" + h + l, 16);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return 0;
        }
    }

    static public String toAscii(byte b) {
        return toHexString(b);
    }

    static public String toAscii(byte[] bytes) {
        return toAscii(bytes, 0, bytes.length);
    }

    static public String toAscii(byte[] bytes, int offset, int length) {
        StringBuilder sb = new StringBuilder();
        int tail = offset + length;

        for (int i = offset; i < tail; i++) {
            sb.append(toHexString(bytes[i]));
        }

        return sb.toString().toUpperCase(Locale.ENGLISH);
    }

    static public String toHexString(byte b) {
        return (((b & 0xff) < 0x10) ? "0" : "") + Integer.toString(b & 0xff, 16).toUpperCase(Locale.ENGLISH);
    }

    static public byte[] toByteArray(boolean[] bits) {
        byte[] dst = new byte[(int) Math.ceil((double) bits.length / 8)];
        for (int i = 0; i < bits.length; i++) {
            dst[i / 8] |= (byte) ((bits[i] ? 1 : 0) << (i % 8));
        }
        return dst;
    }

    static public boolean[] toBitsArray(byte[] bytes, int bitCount) {
        boolean[] dst = new boolean[bitCount];
        for (int i = 0; i < dst.length; i++) {
            dst[i] = (bytes[i / 8] & (1 << (i % 8))) != 0;
        }
        return dst;
    }

    static public int[] BeToIntArray(byte[] bytes) {
        int[] dst = new int[bytes.length / 2];
        for (int i = 0, j = 0; i < dst.length; i++, j += 2)
            dst[i] = ((bytes[j] & 0xff) << 8) | (bytes[j + 1] & 0xff);
        return dst;
    }

    static public int[] LeToIntArray(byte[] bytes) {
        int[] dst = new int[bytes.length / 2];
        for (int i = 0, j = 0; i < dst.length; i++, j += 2)
            dst[i] = ((bytes[j + 1] & 0xff) << 8) | (bytes[j] & 0xff);
        return dst;
    }

    static public byte[] toByteArray(int[] i16) {
        byte[] dst = new byte[i16.length * 2];
        for (int i = 0, j = 0; i < i16.length; i++, j += 2) {
            dst[j] = (byte) ((i16[i] >> 8) & 0xff);
            dst[j + 1] = (byte) (i16[i] & 0xff);
        }
        return dst;
    }

    static public byte[] toByteArray(short[] src) {
        byte[] dst = new byte[src.length];
        for (int i = 0; i < src.length; i++)
            dst[i] = (byte) src[i];
        return dst;
    }

    public static byte[] toByteArray(short i16) {
        return new byte[]{byteHigh(i16), byteLow(i16)};
    }

    public static int toShort(int h, int l) {
        return ((h & 0xff) << 8) | (l & 0xff);
    }

    public static byte[] toByteArray(int i32) {
        byte[] regs = new byte[4];
        regs[0] = (byte) (0xff & (i32 >> 8));
        regs[1] = (byte) (0xff & i32);
        regs[2] = (byte) (0xff & (i32 >> 24));
        regs[3] = (byte) (0xff & (i32 >> 16));
        return regs;
    }

    public static float toFloat(byte[] bytes) {
        return Float.intBitsToFloat(
                ((bytes[0] & 0xff) << 8) |
                        (bytes[1] & 0xff) |
                        ((bytes[2] & 0xff) << 24) |
                        ((bytes[3] & 0xff) << 16));
    }

    public static byte[] toByteArray(float f32) {
        return toByteArray(Float.floatToIntBits(f32));
    }

    public static byte byteLow(int b) {
        return (byte) ((short) b & 0xff);
    }

    public static byte byteHigh(int b) {
        return (byte) (((short) b >> 8) & 0xff);
    }

    public static int wordLow(int b) {
        return b & 0xffff;
    }

    public static int wordHigh(int b) {
        return (b >> 16) & 0xffff;
    }

    public static int dwordLow(long l) {
        return (int) (l & 0xffffffff);
    }

    public static int dwordHigh(long l) {
        return (int) ((l >> 32) & 0xffffffff);
    }

    public static int wordFromBytes(int l, int h) {
        return (l & 0xff) | ((h & 0xff) << 8);
    }

    public static int dwordFromWords(int l, int h) {
        return (l & 0xffff) | ((h & 0xffff) << 16);
    }

    public static long Int64FromDwords(int l, int h) {
        return ((long) l & 0xffffffff) | (((long) h & 0xffffffff) << 32);
    }
}
