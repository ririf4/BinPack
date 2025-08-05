package net.ririfa.binpack.primitive

import net.ririfa.binpack.TypeAdapter
import java.math.BigInteger
import java.nio.ByteBuffer

object BigIntegerAdapter : TypeAdapter<BigInteger> {
    override fun estimateSize(value: BigInteger): Int {
        return Int.SIZE_BYTES + value.toByteArray().size
    }

    override fun write(value: BigInteger, buffer: ByteBuffer) {
        val bytes = value.toByteArray()
        buffer.putInt(bytes.size)
        buffer.put(bytes)
    }

    override fun read(buffer: ByteBuffer): BigInteger {
        val size = buffer.int
        val bytes = ByteArray(size)
        buffer.get(bytes)
        return BigInteger(bytes)
    }
}