package com.catwave.demo;

import java.nio.ByteBuffer;
import java.util.UUID;

public class UUIDConvert {

    public static UUID fromLong(long id) {
        ByteBuffer buffer = ByteBuffer.allocate(16);
        buffer.putLong(id);
        buffer.putLong(0);
        buffer.flip();
        return new UUID(buffer.getLong(), buffer.getLong());
    }

    public static UUID bytesToUUID(byte[] bytes) {
        ByteBuffer bb = ByteBuffer.wrap(bytes);
        long high = bb.getLong();
        long low = bb.getLong();
        return new UUID(high, low);
    }

    public static byte[] uuidToBytes(UUID uuid) {
        ByteBuffer bb = ByteBuffer.wrap(new byte[16]);
        bb.putLong(uuid.getMostSignificantBits());
        bb.putLong(uuid.getLeastSignificantBits());
        return bb.array();
    }
}
