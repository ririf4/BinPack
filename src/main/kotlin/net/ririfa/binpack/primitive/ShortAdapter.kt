package net.ririfa.binpack.primitive

import net.ririfa.binpack.ByteBufferL
import net.ririfa.binpack.TypeAdapter

object ShortAdapter : TypeAdapter<Short> {
    override fun estimateSize(value: Short) = 2

    override fun write(value: Short, buffer: ByteBufferL) {
        buffer.i16 = value
    }

    override fun read(buffer: ByteBufferL): Short = buffer.i16
}