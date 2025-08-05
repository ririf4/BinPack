package net.ririfa.binpack.primitive

import net.ririfa.binpack.TypeAdapter
import java.nio.ByteBuffer

object LongAdapter : TypeAdapter<Long> {
    override fun estimateSize(value: Long) = Long.SIZE_BYTES
    override fun write(value: Long, buffer: ByteBuffer) {
        buffer.putLong(value)
    }

    override fun read(buffer: ByteBuffer) = buffer.long
}