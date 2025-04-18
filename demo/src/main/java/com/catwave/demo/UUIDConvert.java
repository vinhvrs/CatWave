package com.catwave.demo;

import java.nio.ByteBuffer;
import java.util.UUID;

public class UUIDConvert {
    public static UUID  fromLong(long id){
        ByteBuffer buffer = ByteBuffer.allocate(16);
        buffer.putLong(id);
        buffer.putLong(0);
        buffer.flip();
        return new UUID(buffer.getLong(), buffer.getLong());
    }
}
