package net.ririfa.binpack

import java.nio.ByteBuffer
import java.util.ArrayDeque
import kotlin.jvm.Synchronized


object BinPackBufferPool {
    private val pool = ArrayDeque<ByteBuffer>()

    @Synchronized
    fun borrow(size: Int): ByteBuffer {
        return pool.firstOrNull { it.capacity() >= size }?.also {
            pool.remove(it)
            it.clear()
        } ?: ByteBuffer.allocateDirect(size)
    }

    @Synchronized
    fun recycle(buffer: ByteBuffer) {
        pool.addLast(buffer)
    }
}
