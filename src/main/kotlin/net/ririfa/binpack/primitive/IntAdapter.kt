package net.ririfa.binpack.primitive

import net.ririfa.binpack.ByteBufferL
import net.ririfa.binpack.TypeAdapter

object IntAdapter : TypeAdapter<Int> {
    override fun estimateSize(value: Int) = 4

    override fun write(value: Int, buffer: ByteBufferL) {
        buffer.i32 = value
    }

    override fun read(buffer: ByteBufferL): Int = buffer.i32
}