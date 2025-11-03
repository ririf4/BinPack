package net.ririfa.binpack.primitive

import net.ririfa.binpack.ByteBufferL
import net.ririfa.binpack.TypeAdapter
import java.lang.Double.doubleToRawLongBits
import java.lang.Double.longBitsToDouble

object DoubleAdapter : TypeAdapter<Double> {
    override fun estimateSize(value: Double) = 8
    override fun write(value: Double, buffer: ByteBufferL) {
        buffer.i64 = doubleToRawLongBits(value)
    }

    override fun read(buffer: ByteBufferL) =
        longBitsToDouble(buffer.i64)

}