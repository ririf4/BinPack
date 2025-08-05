package net.ririfa.binpack.additional

import net.ririfa.binpack.AdapterSetting
import net.ririfa.binpack.TypeAdapter
import java.nio.ByteBuffer

class ListAdapter<T>(
    private val elementAdapter: TypeAdapter<T>
) : TypeAdapter<List<T>> {

    override fun estimateSize(value: List<T>): Int =
        Int.SIZE_BYTES + value.sumOf { elementAdapter.estimateSize(it) }

    override fun write(value: List<T>, buffer: ByteBuffer) {
        buffer.putInt(value.size)
        value.forEach { elementAdapter.write(it, buffer) }
    }

    override fun read(buffer: ByteBuffer): List<T> {
        val size = buffer.int
        require(size in 0..AdapterSetting.maxCollectionSize) {
            "Collection size $size exceeds configured limit (${AdapterSetting.maxCollectionSize})"
        }
        return List(size) { elementAdapter.read(buffer) }
    }
}
