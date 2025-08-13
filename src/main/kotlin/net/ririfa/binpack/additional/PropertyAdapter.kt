package net.ririfa.binpack.additional

import net.ririfa.binpack.TypeAdapter
import java.lang.invoke.MethodHandle
import java.lang.invoke.MethodHandles
import java.lang.invoke.MethodType
import java.nio.ByteBuffer
import kotlin.reflect.KParameter
import kotlin.reflect.KProperty1
import kotlin.reflect.jvm.javaField

class PropertyAdapter<T : Any, V>(
    property: KProperty1<T, V>,
    val param: KParameter,
    val adapter: TypeAdapter<V>
) {
    private val getter: MethodHandle = MethodHandles.lookup()
        .unreflectGetter(property.javaField!!)
        .asType(MethodType.methodType(Any::class.java, Any::class.java))

    @Suppress("UNCHECKED_CAST")
    private fun get(instance: T): V = getter.invoke(instance) as V

    fun estimateSize(instance: T): Int = adapter.estimateSize(get(instance))

    fun write(instance: T, buffer: ByteBuffer) {
        adapter.write(get(instance), buffer)
    }

    fun read(buffer: ByteBuffer): V = adapter.read(buffer)
}
