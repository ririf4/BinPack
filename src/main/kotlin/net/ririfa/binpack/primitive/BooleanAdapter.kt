package net.ririfa.binpack.primitive

import net.ririfa.binpack.ByteBufferL
import net.ririfa.binpack.TypeAdapter

object BooleanAdapter : TypeAdapter<Boolean> {
    override fun estimateSize(value: Boolean) = 1

    override fun write(value: Boolean, buffer: ByteBufferL) {
        buffer.i8 = if (value) 1 else 0
    }

    override fun read(buffer: ByteBufferL): Boolean {
        return buffer.i8 != 0
    }
}