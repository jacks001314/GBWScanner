package com.gbw.scanner.protocol;

/**
 * Represents the received (potentially partially deserialized) packet data.
 * @param <B> The Buffer type.
 */
public interface GBWPacketData<B extends GBWBuffer<B>> {
    B getDataBuffer();
}
