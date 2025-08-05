package net.ririfa.binpack.primitive

import net.ririfa.binpack.TypeAdapter
import java.nio.ByteBuffer

object FloatAdapter : TypeAdapter<Float> {
    override fun estimateSize(value: Float) = Float.SIZE_BYTES
    override fun write(value: Float, buffer: ByteBuffer) {
        buffer.putFloat(value)
    }

    override fun read(buffer: ByteBuffer) = buffer.float
}