package net.ririfa.binpack.primitive

import net.ririfa.binpack.TypeAdapter
import java.nio.ByteBuffer

object ByteAdapter : TypeAdapter<Byte> {
    override fun estimateSize(value: Byte) = Byte.SIZE_BYTES
    override fun write(value: Byte, buffer: ByteBuffer) {
        buffer.put(value)
    }

    override fun read(buffer: ByteBuffer) = buffer.get()
}