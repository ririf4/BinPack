package net.ririfa.binpack.primitive

import net.ririfa.binpack.TypeAdapter
import java.nio.ByteBuffer

object ByteBufferAdapter : TypeAdapter<ByteBuffer> {
    override fun estimateSize(value: ByteBuffer): Int {
        return Int.SIZE_BYTES + value.remaining() // Size of length + remaining bytes
    }

    override fun write(value: ByteBuffer, buffer: ByteBuffer) {
        val size = value.remaining()
        buffer.putInt(size)
        buffer.put(value)
    }

    override fun read(buffer: ByteBuffer): ByteBuffer {
        val size = buffer.int
        val bytes = ByteArray(size)
        buffer.get(bytes)
        return ByteBuffer.wrap(bytes)
    }
}