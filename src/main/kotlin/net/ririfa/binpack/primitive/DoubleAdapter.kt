package net.ririfa.binpack.primitive

import net.ririfa.binpack.TypeAdapter
import java.nio.ByteBuffer

object DoubleAdapter : TypeAdapter<Double> {
    override fun estimateSize(value: Double) = Double.SIZE_BYTES
    override fun write(value: Double, buffer: ByteBuffer) {
        buffer.putDouble(value)
    }

    override fun read(buffer: ByteBuffer) = buffer.double
}