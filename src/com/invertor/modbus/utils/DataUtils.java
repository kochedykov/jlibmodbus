package com.invertor.modbus.utils;

/**
 * Copyright (c) 2015-2016 JSC Invertor
 * [http://www.sbp-invertor.ru]
 * <p/>
 * This file is part of JLibModbus.
 * <p/>
 * JLibModbus is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p/>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * <p/>
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * <p/>
 * Authors: Vladislav Y. Kochedykov, software engineer.
 * email: vladislav.kochedykov@gmail.com
 */
public class DataUtils {

    static public int fromAscii(char h, char l) {
        return Integer.parseInt("" + h + l, 16);
    }

    static public byte[] toAscii(byte b) {
        return toHexString(b).getBytes();
    }

    static public byte[] toAscii(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(toHexString(b));
        }
        return sb.toString().getBytes();
    }

    static public String toHexString(byte b) {
        return (((b & 0xff) < 0x10) ? "0" : "") + Integer.toString(b & 0xff, 16).toUpperCase();
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

    static public int[] toIntArray(byte[] bytes) {
        int[] dst = new int[bytes.length / 2];
        for (int i = 0, j = 0; i < dst.length; i++, j += 2)
            dst[i] = ((bytes[j] & 0xff) << 8) | (bytes[j + 1] & 0xff);
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

    public static short toShort(byte[] bytes) {
        return toShort(bytes[0], bytes[1]);
    }

    public static byte[] toByteArray(short i16) {
        return new byte[]{byteHigh(i16), byteLow(i16)};
    }

    public static short toShort(int h, int l) {
        return (short) (((byte) h & 0xff) << 8 | ((byte) l & 0xff));
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
}
