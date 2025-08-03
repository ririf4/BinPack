package net.ririfa.binpack

import net.ririfa.binpack.additional.EnumAdapter
import net.ririfa.binpack.additional.ListAdapter
import net.ririfa.binpack.additional.MapAdapter
import net.ririfa.binpack.additional.NullableAdapter
import net.ririfa.binpack.additional.PropertyAdapter
import java.nio.ByteBuffer
import kotlin.reflect.KClass
import kotlin.reflect.KProperty1
import kotlin.reflect.KType
import kotlin.reflect.full.*
import java.util.concurrent.ConcurrentHashMap

object AdapterResolver {
    private val adapterCache = ConcurrentHashMap<KType, TypeAdapter<*>>()

    fun getAdapterForType(type: KType): TypeAdapter<*> {
        return adapterCache.getOrPut(type) {
            val cls = type.classifier as? KClass<*>
                ?: error("Unsupported type: $type")

            // 1. Nullable
            if (type.isMarkedNullable) {
                val nonNullType = type.withNullability(false)
                val inner = getAdapterForType(nonNullType) as TypeAdapter<Any>
                return@getOrPut NullableAdapter(inner)
            }

            // 2. Primitive / Built-in
            when (cls) {
                Int::class     -> return@getOrPut IntAdapter
                Long::class    -> return@getOrPut LongAdapter
                Short::class   -> return@getOrPut ShortAdapter
                Byte::class    -> return@getOrPut ByteAdapter
                Boolean::class -> return@getOrPut BooleanAdapter
                Float::class   -> return@getOrPut FloatAdapter
                Double::class  -> return@getOrPut DoubleAdapter
                Char::class    -> return@getOrPut CharAdapter
                String::class  -> return@getOrPut StringAdapter
                ByteArray::class -> return@getOrPut ByteArrayAdapter

                java.util.UUID::class -> return@getOrPut UUIDAdapter
                java.math.BigInteger::class -> return@getOrPut BigIntegerAdapter
                java.math.BigDecimal::class -> return@getOrPut BigDecimalAdapter
                java.time.LocalDate::class -> return@getOrPut LocalDateAdapter
                java.time.LocalTime::class -> return@getOrPut LocalTimeAdapter
                java.time.LocalDateTime::class -> return@getOrPut LocalDateTimeAdapter
            }

            // 3. Enum
            if (cls.java.isEnum) {
                @Suppress("UNCHECKED_CAST")
                return@getOrPut EnumAdapter(cls as KClass<Enum<*>>)
            }

            // 4. List<T>
            if (cls == List::class) {
                val elementType = type.arguments.firstOrNull()?.type
                    ?: error("List<T> must have type argument")
                val elementAdapter = getAdapterForType(elementType)
                @Suppress("UNCHECKED_CAST")
                return@getOrPut ListAdapter(elementAdapter as TypeAdapter<Any>)
            }

            // 5. Map<K, V>
            if (cls == Map::class) {
                val (keyType, valueType) = type.arguments.mapNotNull { it.type }
                val keyAdapter = getAdapterForType(keyType)
                val valueAdapter = getAdapterForType(valueType)
                @Suppress("UNCHECKED_CAST")
                return@getOrPut MapAdapter(
                    keyAdapter as TypeAdapter<Any>,
                    valueAdapter as TypeAdapter<Any>
                )
            }

            // 6. data class
            if (cls.isData) {
                @Suppress("UNCHECKED_CAST")
                return@getOrPut generateCompositeAdapter(cls as KClass<Any>)
            }

            error("Unsupported type: $type")
        }
    }

    @Suppress("UNCHECKED_CAST")
    fun <T : Any> getAdapterForClass(kClass: KClass<T>): TypeAdapter<T> {
        return getAdapterForType(kClass.createType()) as TypeAdapter<T>
    }

    fun <T : Any> generateCompositeAdapter(kClass: KClass<T>): TypeAdapter<T> {
        val constructor = kClass.primaryConstructor
            ?: error("Class ${kClass.simpleName} has no primary constructor")

        val propertyAdapters = constructor.parameters.mapIndexed { index, param ->
            val prop = kClass.memberProperties.find { it.name == param.name }
                ?: error("No matching property for constructor parameter '${param.name}'")

            val adapter = getAdapterForType(param.type)

            PropertyAdapter(prop, param, adapter as TypeAdapter<Any?>)
        }

        return object : TypeAdapter<T> {
            override fun estimateSize(value: T): Int {
                return propertyAdapters.sumOf { it.estimateSize(value) }
            }

            override fun write(value: T, buffer: ByteBuffer) {
                propertyAdapters.forEach { it.write(value, buffer) }
            }

            override fun read(buffer: ByteBuffer): T {
                val args = propertyAdapters.associate { it.param to it.read(buffer) }
                return constructor.callBy(args)
            }
        }
    }
}
