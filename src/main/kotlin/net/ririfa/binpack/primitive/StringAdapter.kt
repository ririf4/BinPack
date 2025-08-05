package net.ririfa.binpack.primitive

import net.ririfa.binpack.TypeAdapter
import java.nio.ByteBuffer

object StringAdapter : TypeAdapter<String> {
    override fun estimateSize(value: String): Int {
        val bytes = value.toByteArray(Charsets.UTF_8)
        return Int.SIZE_BYTES + bytes.size
    }

    override fun write(value: String, buffer: ByteBuffer) {
        val bytes = value.toByteArray(Charsets.UTF_8)
        buffer.putInt(bytes.size)
        buffer.put(bytes)
    }

    override fun read(buffer: ByteBuffer): String {
        val size = buffer.int
        val bytes = ByteArray(size)
        buffer.get(bytes)
        return bytes.toString(Charsets.UTF_8)
    }
}