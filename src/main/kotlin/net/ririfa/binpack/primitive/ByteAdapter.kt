package net.ririfa.binpack.primitive

import net.ririfa.binpack.ByteBufferL
import net.ririfa.binpack.TypeAdapter

object ByteAdapter : TypeAdapter<Byte> {
    override fun estimateSize(value: Byte) = 1
    override fun write(value: Byte, buffer: ByteBufferL) {
        buffer.i8 = value.toInt() and 0xFF
    }

    override fun read(buffer: ByteBufferL): Byte {
        return buffer.i8.toByte()
    }
}