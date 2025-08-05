package net.ririfa.binpack.primitive

import net.ririfa.binpack.TypeAdapter
import java.nio.ByteBuffer

object IntAdapter : TypeAdapter<Int> {
    override fun estimateSize(value: Int) = Int.SIZE_BYTES
    override fun write(value: Int, buffer: ByteBuffer) {
        buffer.putInt(value)
    }

    override fun read(buffer: ByteBuffer) = buffer.int
}

