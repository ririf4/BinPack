package net.ririfa.binpack.primitive

import net.ririfa.binpack.ByteBufferL
import net.ririfa.binpack.TypeAdapter
import java.util.*

object UUIDAdapter : TypeAdapter<UUID> {
    override fun estimateSize(value: UUID) = 16

    override fun write(value: UUID, buffer: ByteBufferL) {
        buffer.i64 = value.mostSignificantBits
        buffer.i64 = value.leastSignificantBits
    }

    override fun read(buffer: ByteBufferL): UUID {
        val msb = buffer.i64
        val lsb = buffer.i64
        return UUID(msb, lsb)
    }
}