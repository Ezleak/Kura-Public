package me.windyteam.kura.utils;

import net.minecraft.util.EnumFacing;

import java.util.HashMap;

public final class GeometryMasks {
    public static final HashMap<EnumFacing, Integer> FACEMAP = new HashMap();

    static {
        FACEMAP.put(EnumFacing.DOWN, 1);
        FACEMAP.put(EnumFacing.WEST, 16);
        FACEMAP.put(EnumFacing.NORTH, 4);
        FACEMAP.put(EnumFacing.SOUTH, 8);
        FACEMAP.put(EnumFacing.EAST, 32);
        FACEMAP.put(EnumFacing.UP, 2);
    }

    public static final class Line {
        public static final int DOWN_WEST = 17;
        public static final int UP_WEST = 18;
        public static final int DOWN_EAST = 33;
        public static final int UP_EAST = 34;
        public static final int DOWN_NORTH = 5;
        public static final int UP_NORTH = 6;
        public static final int DOWN_SOUTH = 9;
        public static final int UP_SOUTH = 10;
        public static final int NORTH_WEST = 20;
        public static final int NORTH_EAST = 36;
        public static final int SOUTH_WEST = 24;
        public static final int SOUTH_EAST = 40;
        public static final int ALL = 63;
    }

    public static final class Quad {
        public static final int DOWN = 1;
        public static final int UP = 2;
        public static final int NORTH = 4;
        public static final int SOUTH = 8;
        public static final int WEST = 16;
        public static final int EAST = 32;
        public static final int ALL = 63;
    }
}

