package net.ririfa.binpack.primitive

import net.ririfa.binpack.TypeAdapter
import java.nio.ByteBuffer
import java.time.LocalDate

object LocalDateAdapter : TypeAdapter<LocalDate> {
    override fun estimateSize(value: LocalDate) = Long.SIZE_BYTES // Store as epoch day
    override fun write(value: LocalDate, buffer: ByteBuffer) {
        buffer.putLong(value.toEpochDay())
    }

    override fun read(buffer: ByteBuffer): LocalDate {
        return LocalDate.ofEpochDay(buffer.long)
    }
}