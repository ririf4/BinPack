package net.ririfa.binpack.primitive

import net.ririfa.binpack.TypeAdapter
import java.lang.Float.floatToRawIntBits
import java.lang.Float.intBitsToFloat
import java.nio.ByteBuffer

object FloatAdapter : TypeAdapter<Float> {
    override fun estimateSize(value: Float) = 4
    override fun write(value: Float, buffer: ByteBuffer) {
        buffer.putInt(floatToRawIntBits(value))
    }
    override fun read(buffer: ByteBuffer) =
       intBitsToFloat(buffer.int)

}