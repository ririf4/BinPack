package net.ririfa.binpack.primitive

import net.ririfa.binpack.ByteBufferL
import net.ririfa.binpack.TypeAdapter
import java.util.*

object DateAdapter : TypeAdapter<Date> {
    override fun estimateSize(value: Date) = 8

    override fun write(value: Date, buffer: ByteBufferL) {
        buffer.i64 = value.time
    }

    override fun read(buffer: ByteBufferL): Date {
        return Date(buffer.i64)
    }
}