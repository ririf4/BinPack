package net.ririfa.binpack

import java.math.BigDecimal
import java.math.BigInteger
import java.nio.ByteBuffer
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.util.*

object IntAdapter : TypeAdapter<Int> {
    override fun estimateSize(value: Int) = Int.SIZE_BYTES
    override fun write(value: Int, buffer: ByteBuffer) {
        buffer.putInt(value)
    }

    override fun read(buffer: ByteBuffer) = buffer.int
}

object BooleanAdapter : TypeAdapter<Boolean> {
    override fun estimateSize(value: Boolean) = 1
    override fun write(value: Boolean, buffer: ByteBuffer) {
        buffer.put(if (value) 1 else 0)
    }

    override fun read(buffer: ByteBuffer) = buffer.get().toInt() != 0
}

object StringAdapter : TypeAdapter<String> {
    override fun estimateSize(value: String): Int {
        val bytes = value.toByteArray(Charsets.UTF_8)
        return Int.SIZE_BYTES + bytes.size
    }

    override fun write(value: String, buffer: ByteBuffer) {
        val bytes = value.toByteArray(Charsets.UTF_8)
        buffer.putInt(bytes.size)
        buffer.put(bytes)
    }

    override fun read(buffer: ByteBuffer): String {
        val size = buffer.int
        val bytes = ByteArray(size)
        buffer.get(bytes)
        return bytes.toString(Charsets.UTF_8)
    }
}

object ByteArrayAdapter : TypeAdapter<ByteArray> {
    override fun estimateSize(value: ByteArray): Int {
        return Int.SIZE_BYTES + value.size
    }

    override fun write(value: ByteArray, buffer: ByteBuffer) {
        buffer.putInt(value.size)
        buffer.put(value)
    }

    override fun read(buffer: ByteBuffer): ByteArray {
        val size = buffer.int
        val bytes = ByteArray(size)
        buffer.get(bytes)
        return bytes
    }
}

object LongAdapter : TypeAdapter<Long> {
    override fun estimateSize(value: Long) = Long.SIZE_BYTES
    override fun write(value: Long, buffer: ByteBuffer) {
        buffer.putLong(value)
    }

    override fun read(buffer: ByteBuffer) = buffer.long
}

object DoubleAdapter : TypeAdapter<Double> {
    override fun estimateSize(value: Double) = Double.SIZE_BYTES
    override fun write(value: Double, buffer: ByteBuffer) {
        buffer.putDouble(value)
    }

    override fun read(buffer: ByteBuffer) = buffer.double
}

object FloatAdapter : TypeAdapter<Float> {
    override fun estimateSize(value: Float) = Float.SIZE_BYTES
    override fun write(value: Float, buffer: ByteBuffer) {
        buffer.putFloat(value)
    }

    override fun read(buffer: ByteBuffer) = buffer.float
}

object ShortAdapter : TypeAdapter<Short> {
    override fun estimateSize(value: Short) = Short.SIZE_BYTES
    override fun write(value: Short, buffer: ByteBuffer) {
        buffer.putShort(value)
    }

    override fun read(buffer: ByteBuffer) = buffer.short
}

object CharAdapter : TypeAdapter<Char> {
    override fun estimateSize(value: Char) = Char.SIZE_BYTES
    override fun write(value: Char, buffer: ByteBuffer) {
        buffer.putChar(value)
    }

    override fun read(buffer: ByteBuffer) = buffer.char
}

object ByteAdapter : TypeAdapter<Byte> {
    override fun estimateSize(value: Byte) = Byte.SIZE_BYTES
    override fun write(value: Byte, buffer: ByteBuffer) {
        buffer.put(value)
    }

    override fun read(buffer: ByteBuffer) = buffer.get()
}

object UUIDAdapter : TypeAdapter<UUID> {
    override fun estimateSize(value: UUID) = 16 // 2 * Long.SIZE_BYTES
    override fun write(value: UUID, buffer: ByteBuffer) {
        buffer.putLong(value.mostSignificantBits)
        buffer.putLong(value.leastSignificantBits)
    }

    override fun read(buffer: ByteBuffer): UUID {
        val mostSigBits = buffer.long
        val leastSigBits = buffer.long
        return UUID(mostSigBits, leastSigBits)
    }
}

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

object LocalDateAdapter : TypeAdapter<LocalDate> {
    override fun estimateSize(value: LocalDate) = Long.SIZE_BYTES // Store as epoch day
    override fun write(value: LocalDate, buffer: ByteBuffer) {
        buffer.putLong(value.toEpochDay())
    }

    override fun read(buffer: ByteBuffer): LocalDate {
        return LocalDate.ofEpochDay(buffer.long)
    }
}

object LocalDateTimeAdapter : TypeAdapter<LocalDateTime> {
    override fun estimateSize(value: LocalDateTime) = Long.SIZE_BYTES * 2 // Store as epoch seconds and nano
    override fun write(value: LocalDateTime, buffer: ByteBuffer) {
        buffer.putLong(value.toEpochSecond(java.time.ZoneOffset.UTC))
        buffer.putInt(value.nano)
    }

    override fun read(buffer: ByteBuffer): LocalDateTime {
        val epochSeconds = buffer.long
        val nano = buffer.int
        return LocalDateTime.ofEpochSecond(epochSeconds, nano, java.time.ZoneOffset.UTC)
    }
}

object LocalTimeAdapter : TypeAdapter<LocalTime> {
    override fun estimateSize(value: LocalTime) = Long.SIZE_BYTES // Store as nano of day
    override fun write(value: LocalTime, buffer: ByteBuffer) {
        buffer.putLong(value.toNanoOfDay())
    }

    override fun read(buffer: ByteBuffer): LocalTime {
        return LocalTime.ofNanoOfDay(buffer.long)
    }
}

object DateAdapter : TypeAdapter<Date> {
    override fun estimateSize(value: Date) = Long.SIZE_BYTES // Store as epoch milliseconds
    override fun write(value: Date, buffer: ByteBuffer) {
        buffer.putLong(value.time)
    }

    override fun read(buffer: ByteBuffer): Date {
        return Date(buffer.long)
    }
}

object ByteBufferAdapter : TypeAdapter<ByteBuffer> {
    override fun estimateSize(value: ByteBuffer): Int {
        return Int.SIZE_BYTES + value.remaining() // Size of length + remaining bytes
    }

    override fun write(value: ByteBuffer, buffer: ByteBuffer) {
        val size = value.remaining()
        buffer.putInt(size)
        buffer.put(value)
    }

    override fun read(buffer: ByteBuffer): ByteBuffer {
        val size = buffer.int
        val bytes = ByteArray(size)
        buffer.get(bytes)
        return ByteBuffer.wrap(bytes)
    }
}