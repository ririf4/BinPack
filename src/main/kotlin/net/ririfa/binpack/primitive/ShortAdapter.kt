package net.ririfa.binpack.primitive

import net.ririfa.binpack.TypeAdapter
import java.nio.ByteBuffer

object ShortAdapter : TypeAdapter<Short> {
    override fun estimateSize(value: Short) = Short.SIZE_BYTES
    override fun write(value: Short, buffer: ByteBuffer) {
        buffer.putShort(value)
    }

    override fun read(buffer: ByteBuffer) = buffer.short
}