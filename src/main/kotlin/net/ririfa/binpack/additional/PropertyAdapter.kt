package net.ririfa.binpack.additional

import net.ririfa.binpack.TypeAdapter
import java.nio.ByteBuffer
import kotlin.reflect.KParameter
import kotlin.reflect.KProperty1

class PropertyAdapter<T : Any, V>(
    val property: KProperty1<T, V>,
    val param: KParameter,
    val adapter: TypeAdapter<V>
) {
    fun estimateSize(instance: T): Int =
        adapter.estimateSize(property.get(instance))

    fun write(instance: T, buffer: ByteBuffer) {
        adapter.write(property.get(instance), buffer)
    }

    fun read(buffer: ByteBuffer): V =
        adapter.read(buffer)
}
