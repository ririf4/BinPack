package net.ririfa.binpack.primitive

import net.ririfa.binpack.TypeAdapter
import java.nio.ByteBuffer

object ByteArrayAdapter : TypeAdapter<ByteArray> {
    override fun estimateSize(value: ByteArray): Int {
        return Int.SIZE_BYTES + value.size
    }

    override fun write(value: ByteArray, buffer: ByteBuffer) {
        buffer.putInt(value.size)
        buffer.put(value)
    }

    override fun read(buffer: ByteBuffer): ByteArray {
        val size = buffer.int
        val bytes = ByteArray(size)
        buffer.get(bytes)
        return bytes
    }
}