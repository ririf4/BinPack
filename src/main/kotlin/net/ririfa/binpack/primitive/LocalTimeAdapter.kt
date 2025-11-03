package net.ririfa.binpack.primitive

import net.ririfa.binpack.ByteBufferL
import net.ririfa.binpack.TypeAdapter
import java.time.LocalTime

object LocalTimeAdapter : TypeAdapter<LocalTime> {
    override fun estimateSize(value: LocalTime) = 8

    override fun write(value: LocalTime, buffer: ByteBufferL) {
        buffer.i64 = value.toNanoOfDay()
    }

    override fun read(buffer: ByteBufferL): LocalTime =
        LocalTime.ofNanoOfDay(buffer.i64)
}