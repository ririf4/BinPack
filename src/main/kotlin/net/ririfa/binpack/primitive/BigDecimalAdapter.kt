package net.ririfa.binpack.primitive

import net.ririfa.binpack.TypeAdapter
import java.math.BigDecimal
import java.math.BigInteger
import java.nio.ByteBuffer

object BigDecimalAdapter : TypeAdapter<BigDecimal> {
    override fun estimateSize(value: BigDecimal): Int {
        val unscaledValue = value.unscaledValue()
        return Int.SIZE_BYTES + unscaledValue.toByteArray().size + 1 // 1 byte for scale
    }

    override fun write(value: BigDecimal, buffer: ByteBuffer) {
        val unscaledBytes = value.unscaledValue().toByteArray()
        buffer.putInt(unscaledBytes.size)
        buffer.put(unscaledBytes)
        buffer.put(value.scale().toByte())
    }

    override fun read(buffer: ByteBuffer): BigDecimal {
        val size = buffer.int
        val unscaledBytes = ByteArray(size)
        buffer.get(unscaledBytes)
        val scale = buffer.get().toInt()
        return BigDecimal(BigInteger(unscaledBytes), scale)
    }
}