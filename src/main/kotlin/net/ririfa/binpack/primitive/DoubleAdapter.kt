package net.ririfa.binpack.primitive

import net.ririfa.binpack.TypeAdapter
import java.lang.Double.doubleToRawLongBits
import java.lang.Double.longBitsToDouble
import java.nio.ByteBuffer

object DoubleAdapter : TypeAdapter<Double> {
    override fun estimateSize(value: Double) = 8
    override fun write(value: Double, buffer: ByteBuffer) {
        buffer.putLong(doubleToRawLongBits(value))
    }
    override fun read(buffer: ByteBuffer) =
       longBitsToDouble(buffer.long)

}