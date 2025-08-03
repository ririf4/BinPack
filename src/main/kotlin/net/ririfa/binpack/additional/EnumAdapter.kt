package net.ririfa.binpack.additional

import net.ririfa.binpack.TypeAdapter
import java.nio.ByteBuffer
import kotlin.reflect.KClass

class EnumAdapter(
    private val enumClass: KClass<out Enum<*>>
) : TypeAdapter<Enum<*>> {

    private val values = enumClass.java.enumConstants!!

    override fun estimateSize(value: Enum<*>): Int = Int.SIZE_BYTES

    override fun write(value: Enum<*>, buffer: ByteBuffer) {
        buffer.putInt(value.ordinal)
    }

    override fun read(buffer: ByteBuffer): Enum<*> {
        val ordinal = buffer.int
        require(ordinal in values.indices)
        return values[ordinal]
    }
}
