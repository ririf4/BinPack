package net.ririfa.binpack

import java.nio.ByteBuffer

interface TypeAdapter<T> {
    fun estimateSize(value: T): Int
    fun write(value: T, buffer: ByteBuffer)
    fun read(buffer: ByteBuffer): T
    fun copy(value: T): T {
        val size = estimateSize(value)
        val buf = BinPackBufferPool.borrow(size)
        try {
            write(value, buf)
            buf.flip()
            return read(buf)
        } finally {
            BinPackBufferPool.recycle(buf)
        }
    }
}
