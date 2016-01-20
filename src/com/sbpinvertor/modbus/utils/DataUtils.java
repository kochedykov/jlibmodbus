package com.sbpinvertor.modbus.utils;

/**
 * Copyright (c) 2015 JSC "Zavod "Invertor"
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

    static public byte[] toByteArray(boolean[] src) {
        byte[] dst = new byte[(int) Math.ceil((double) src.length / 8)];
        for (int i = 0; i < src.length; i++) {
            dst[i / 8] |= (byte) ((src[i] ? 1 : 0) << (i % 8));
        }
        return dst;
    }

    static public boolean[] toBitsArray(byte[] src, int bitCount) {
        boolean[] dst = new boolean[bitCount];
        for (int i = 0; i < dst.length; i++) {
            dst[i] = (src[i / 8] & (1 << (i % 8))) != 0;
        }
        return dst;
    }

    static public int[] toRegistersArray(byte[] src) {
        int[] dst = new int[src.length / 2];
        for (int i = 0; i < dst.length; i++)
            dst[i] = ((src[i] & 0xff) << 8) | (src[i + 1] & 0xff);
        return dst;
    }

    static public byte[] toByteArray(int[] src) {
        byte[] dst = new byte[src.length * 2];
        for (int i = 0, j = 0; i < src.length; i++, j += 2) {
            dst[j] = (byte) ((src[i] >> 8) & 0xff);
            dst[j + 1] = (byte) (src[i] & 0xff);
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

    public static short toShort(int h, int l) {
        return (short) (((byte) h & 0xff) << 8 | ((byte) l & 0xff));
    }

    public static byte[] shortToReg(int s) {
        byte[] regs = new byte[2];
        regs[0] = (byte) ((s >> 8) & 0xff);
        regs[1] = (byte) (s & 0xff);
        return regs;
    }

    public static int regsToInt(byte[] bytes) {
        return ((bytes[0] & 0xff) << 8) |
                (bytes[1] & 0xff) |
                ((bytes[2] & 0xff) << 24) |
                ((bytes[3] & 0xff) << 16);
    }

    public static byte[] intToRegs(int i) {
        byte[] regs = new byte[4];
        regs[0] = (byte) (0xff & (i >> 8));
        regs[1] = (byte) (0xff & i);
        regs[2] = (byte) (0xff & (i >> 24));
        regs[3] = (byte) (0xff & (i >> 16));
        return regs;
    }

    public static float regsToFloat(byte[] bytes) {
        return Float.intBitsToFloat(
                ((bytes[0] & 0xff) << 8) |
                        (bytes[1] & 0xff) |
                        ((bytes[2] & 0xff) << 24) |
                        ((bytes[3] & 0xff) << 16));
    }

    public static byte[] floatToRegs(float f) {
        return intToRegs(Float.floatToIntBits(f));
    }

    public static byte byteLow(int b) {
        return (byte) ((short)b & 0xff);
    }

    public static byte byteHigh(int b) {
        return (byte) (((short)b >> 8) & 0xff);
    }

    public static int getShort(int l, int h) {
        return (l & 0xff) | ((h & 0xff) << 8);
    }
}
