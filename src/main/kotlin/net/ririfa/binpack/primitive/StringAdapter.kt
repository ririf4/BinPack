package net.ririfa.binpack.primitive

import net.ririfa.binpack.TypeAdapter
import java.nio.ByteBuffer


class StringAdapter(private val validate: Boolean = true) : TypeAdapter<String> {

    override fun estimateSize(value: String): Int {
        return 4 + value.length
    }

    override fun write(value: String, buffer: ByteBuffer) {
        val start = buffer.position()
        buffer.position(start + 4)

        val len = value.length
        var i = 0
        while (i < len) {
            val c = value[i].code
            if (validate && (c and 0x80 != 0)) {
                throw IllegalArgumentException(
                    "Non-ASCII char detected: U+%04X at index %d".format(c, i)
                )
            }
            buffer.put(c.toByte())
            i++
        }

        val byteLen = len
        buffer.putInt(start, byteLen)
    }

    override fun read(buffer: ByteBuffer): String {
        val size = buffer.int
        if (size == 0) return ""

        val pos = buffer.position()
        val limit = pos + size

        if (validate) {
            var p = pos
            while (p < limit) {
                val b = buffer.get(p).toInt()
                if (b and 0x80 != 0) {
                    throw IllegalArgumentException(
                        "Non-ASCII byte detected at offset ${p - pos}"
                    )
                }
                p++
            }
        }

        val chars = CharArray(size)
        var p = pos
        var i = 0
        while (p < limit) {
            chars[i++] = (buffer.get(p++).toInt() and 0x7F).toChar()
        }
        buffer.position(limit)
        return String(chars, 0, size)
    }
}
