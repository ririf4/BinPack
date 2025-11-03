package net.ririfa.binpack.additional

import net.ririfa.binpack.ByteBufferL
import net.ririfa.binpack.TypeAdapter
import java.lang.invoke.MethodHandle
import java.lang.invoke.MethodHandles
import java.lang.reflect.Modifier
import kotlin.reflect.KParameter
import kotlin.reflect.KProperty1
import kotlin.reflect.jvm.isAccessible
import kotlin.reflect.jvm.javaField
import kotlin.reflect.jvm.javaGetter

class PropertyAdapter<E : Any, V>(
    private val prop: KProperty1<E, V>,
    val param: KParameter,
    private val inner: TypeAdapter<V>
) {
    private val getterFn: (E) -> V = run {
        prop.javaGetter?.let { jg ->
            val owner = jg.declaringClass
            val lookup =
                if (Modifier.isPublic(jg.modifiers) && Modifier.isPublic(owner.modifiers))
                    MethodHandles.publicLookup()
                else
                    MethodHandles.privateLookupIn(owner, MethodHandles.lookup())

            try {
                val mh: MethodHandle = lookup.unreflect(jg)
                @Suppress("UNCHECKED_CAST")
                return@run { recv -> mh.invoke(recv) as V }
            } catch (_: IllegalAccessException) {
            }
        }

        prop.javaField?.let { f ->
            try {
                f.isAccessible = true
                @Suppress("UNCHECKED_CAST")
                return@run { recv -> f.get(recv) as V }
            } catch (_: Throwable) {
            }
        }

        prop.isAccessible = true
        return@run { recv -> prop.get(recv) }
    }

    fun estimateSize(instance: E): Int {
        val v = getterFn(instance)
        return inner.estimateSize(v)
    }

    fun write(instance: E, buffer: ByteBufferL) {
        val v = getterFn(instance)
        inner.write(v, buffer)
    }

    fun read(buffer: ByteBufferL): V = inner.read(buffer)
}
