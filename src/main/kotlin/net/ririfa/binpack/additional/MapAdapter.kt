package net.ririfa.binpack.additional

import net.ririfa.binpack.AdapterSetting
import net.ririfa.binpack.TypeAdapter
import java.nio.ByteBuffer

class MapAdapter<K, V>(
    private val keyAdapter: TypeAdapter<K>,
    private val valueAdapter: TypeAdapter<V>
) : TypeAdapter<Map<K, V>> {

    override fun estimateSize(value: Map<K, V>): Int =
        Int.SIZE_BYTES + value.entries.sumOf { keyAdapter.estimateSize(it.key) + valueAdapter.estimateSize(it.value) }

    override fun write(value: Map<K, V>, buffer: ByteBuffer) {
        buffer.putInt(value.size)
        value.forEach {
            keyAdapter.write(it.key, buffer)
            valueAdapter.write(it.value, buffer)
        }
    }

    override fun read(buffer: ByteBuffer): Map<K, V> {
        val size = buffer.int
        require(size in 0..AdapterSetting.maxCollectionSize) {
            "Collection size $size exceeds configured limit (${AdapterSetting.maxCollectionSize})"
        }
        return (0 until size).associate {
            keyAdapter.read(buffer) to valueAdapter.read(buffer)
        }
    }
}
