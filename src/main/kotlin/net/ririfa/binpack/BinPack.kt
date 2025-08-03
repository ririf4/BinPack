package net.ririfa.binpack

import java.nio.ByteBuffer

object BinPack {
    inline fun <reified T : Any> encode(value: T): ByteBuffer {
        val adapter = AdapterResolver.getAdapterForClass(T::class)
        val size = adapter.estimateSize(value)
        val buffer = BinPackBufferPool.borrow(size)
        adapter.write(value, buffer)
        buffer.flip()
        return buffer
    }

    inline fun <reified T : Any> decode(buffer: ByteBuffer): T {
        val adapter = AdapterResolver.getAdapterForClass(T::class)
        return adapter.read(buffer)
    }

    inline fun <reified T : Any> encodeInto(value: T, buffer: ByteBuffer) {
        val adapter = AdapterResolver.getAdapterForClass(T::class)
        adapter.write(value, buffer)
    }

    inline fun <reified T : Any> decodeFrom(buffer: ByteBuffer): T {
        val adapter = AdapterResolver.getAdapterForClass(T::class)
        return adapter.read(buffer)
    }
}
