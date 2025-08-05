package net.ririfa.binpack.primitive

import net.ririfa.binpack.TypeAdapter
import java.nio.ByteBuffer

object CharAdapter : TypeAdapter<Char> {
    override fun estimateSize(value: Char) = Char.SIZE_BYTES
    override fun write(value: Char, buffer: ByteBuffer) {
        buffer.putChar(value)
    }

    override fun read(buffer: ByteBuffer) = buffer.char
}