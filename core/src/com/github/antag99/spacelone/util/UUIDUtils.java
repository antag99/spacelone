package com.github.antag99.spacelone.util;

import java.util.UUID;

public final class UUIDUtils {
    public static String toHexString(UUID uuid) {
        String msb = Long.toHexString(uuid.getMostSignificantBits());
        String lsb = Long.toHexString(uuid.getLeastSignificantBits());
        return new StringBuilder(32)
                .append("0000000000000000".substring(msb.length())).append(msb)
                .append("0000000000000000".substring(lsb.length())).append(lsb)
                .toString();
    }

    public static UUID fromHexString(String hexString) {
        if (hexString.length() != 32)
            throw new IllegalArgumentException("malformed input");
        long msb = Long.parseUnsignedLong(hexString.substring(0, 16), 16);
        long lsb = Long.parseUnsignedLong(hexString.substring(16, 32), 16);
        return new UUID(msb, lsb);
    }
}
