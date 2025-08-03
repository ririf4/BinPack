package net.ririfa.binpack

import java.nio.ByteBuffer

interface TypeAdapter<T> {
    fun estimateSize(value: T): Int
    fun write(value: T, buffer: ByteBuffer)
    fun read(buffer: ByteBuffer): T
}
