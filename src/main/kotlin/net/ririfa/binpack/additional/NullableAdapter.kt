package net.ririfa.binpack.additional

import net.ririfa.binpack.TypeAdapter
import java.nio.ByteBuffer

class NullableAdapter<T : Any>(
    private val inner: TypeAdapter<T>
) : TypeAdapter<T?> {
    override fun estimateSize(value: T?): Int {
        return 1 + (value?.let { inner.estimateSize(it) } ?: 0)
    }

    override fun write(value: T?, buffer: ByteBuffer) {
        if (value == null) {
            buffer.put(0)
        } else {
            buffer.put(1)
            inner.write(value, buffer)
        }
    }

    override fun read(buffer: ByteBuffer): T? {
        val isNotNull = buffer.get().toInt() != 0
        return if (isNotNull) inner.read(buffer) else null
    }
}
