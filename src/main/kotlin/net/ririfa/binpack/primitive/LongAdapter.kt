package net.ririfa.binpack.primitive

import net.ririfa.binpack.ByteBufferL
import net.ririfa.binpack.TypeAdapter

object LongAdapter : TypeAdapter<Long> {
    override fun estimateSize(value: Long) = 8

    override fun write(value: Long, buffer: ByteBufferL) {
        buffer.i64 = value
    }

    override fun read(buffer: ByteBufferL): Long = buffer.i64
}