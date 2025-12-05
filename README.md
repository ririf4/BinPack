# BinPack 1.0.0

High-performance binary serialization library for Kotlin/Java with zero-config data class support, custom adapters, and efficient buffer pooling.

## Features

### v1.0.0 - Major Release

- **Custom Adapter Registration**: Register custom serializers for your types
- **Performance Optimizations**:
  - Fast path for primitive types
  - Optimized caching with computeIfAbsent
  - Reduced reflection overhead
  - Efficient buffer pooling
- **Statistics & Monitoring**: Track encoding/decoding performance
- **Enhanced Error Handling**: Detailed error messages with context
- **Thread-Safe**: All operations are thread-safe and lock-free where possible

## Quick Start

### Basic Usage

```kotlin
import net.ririfa.binpack.BinPack

data class User(val id: Int, val name: String, val active: Boolean)

fun main() {
    val user = User(42, "Alice", true)

    // Encode
    val buffer = BinPack.encode(user)
    println("Encoded ${buffer.remaining} bytes")

    // Decode
    val decoded = BinPack.decode<User>(buffer)
    println("Decoded: $decoded")

    // Deep copy
    val copy = BinPack.deepCopy(user)
    println("Copy: $copy")
}
```

## Custom Adapters

Register custom adapters for your types to control serialization:

```kotlin
import net.ririfa.binpack.*

// Define a custom type
data class Point(val x: Double, val y: Double)

// Create a custom adapter
object PointAdapter : TypeAdapter<Point> {
    override fun estimateSize(value: Point) = 16 // 2 doubles

    override fun write(value: Point, buffer: ByteBufferL) {
        buffer.f64 = value.x
        buffer.f64 = value.y
    }

    override fun read(buffer: ByteBufferL): Point {
        val x = buffer.f64
        val y = buffer.f64
        return Point(x, y)
    }
}

// Register the adapter
fun setup() {
    AdapterRegistry.register<Point>(PointAdapter)
}

// Now BinPack will use your custom adapter
fun main() {
    setup()

    val point = Point(3.14, 2.71)
    val buffer = BinPack.encode(point)
    val decoded = BinPack.decode<Point>(buffer)

    println("Original: $point")
    println("Decoded: $decoded")
}
```

### Registering Adapters

There are multiple ways to register adapters:

```kotlin
// Method 1: Using reified type parameter (recommended)
AdapterRegistry.register<MyClass>(MyClassAdapter())

// Method 2: Using KClass
AdapterRegistry.registerAdapter(MyClass::class, MyClassAdapter())

// Method 3: Using KType for more complex types
val type = typeOf<List<MyClass>>()
AdapterRegistry.registerAdapter(type, ListAdapter(MyClassAdapter()))
```

### Unregistering Adapters

```kotlin
// Unregister a specific adapter
AdapterRegistry.unregisterAdapter(MyClass::class)

// Clear all custom adapters
AdapterRegistry.clearAll()

// Check if an adapter is registered
val hasAdapter = AdapterRegistry.hasCustomAdapter(MyClass::class)
```

## Performance Statistics

Track your serialization performance:

```kotlin
import net.ririfa.binpack.AdapterSetting

fun main() {
    // Enable statistics collection
    AdapterSetting.enableStatistics = true

    // Perform some operations
    repeat(1000) {
        val data = MyData(...)
        val buffer = BinPack.encode(data)
        BinPack.decode<MyData>(buffer)
    }

    // Get statistics
    val stats = AdapterSetting.getStatistics()
    println(stats)

    // Output:
    // BinPack Statistics:
    //   Encode operations: 1000
    //   Decode operations: 1000
    //   Total bytes encoded: 45000
    //   Total bytes decoded: 45000
    //   Average bytes per encode: 45.00
    //   Average bytes per decode: 45.00
    //   Custom adapters registered: 3

    // Reset statistics
    AdapterSetting.resetStatistics()
}
```

## Configuration

Customize BinPack behavior via `AdapterSetting`:

```kotlin
import net.ririfa.binpack.AdapterSetting

// Set maximum collection size (default: 1,000,000)
AdapterSetting.maxCollectionSize = 10_000_000

// Set maximum string length (default: 10 MB)
AdapterSetting.maxStringLength = 50 * 1024 * 1024

// Enable statistics collection (default: false)
AdapterSetting.enableStatistics = true

// Enable detailed error messages (default: true)
AdapterSetting.enableDetailedErrors = true
```

## Supported Types

BinPack automatically supports:

### Primitives
- `Int`, `Long`, `Short`, `Byte`, `Boolean`
- `Float`, `Double`
- `Char`, `String`
- `ByteArray`

### Common Types
- `UUID`
- `BigInteger`, `BigDecimal`
- `LocalDate`, `LocalTime`, `LocalDateTime`
- `Date`

### Collections
- `List<T>`
- `Map<K, V>`

### Complex Types
- Kotlin data classes (with primary constructor)
- Java records
- Enums
- Nullable types (`T?`)

### Custom Types
- Any type with a registered `TypeAdapter`

## Advanced Usage

### Encoding into Existing Buffer

```kotlin
val buffer = ByteBufferL.allocate(1024)
BinPack.encodeInto(myData, buffer)
// buffer.position is now advanced by bytes written
```

### Buffer Pooling

BinPack uses efficient buffer pooling internally. Buffers are automatically returned to the pool when possible.

```kotlin
// Pooling is automatic when using BinPack.encode()
val buffer1 = BinPack.encode(data1) // Gets buffer from pool
// ... use buffer1 ...

val buffer2 = BinPack.encode(data2) // May reuse buffer1's memory
```

### Error Handling

```kotlin
import net.ririfa.binpack.BinPackFormatException

try {
    val decoded = BinPack.decode<MyData>(buffer)
} catch (e: BinPackFormatException) {
    println("Decoding failed: ${e.message}")
    e.printStackTrace()
}
```

## Performance Tips

1. **Register custom adapters** for frequently serialized types to avoid reflection
2. **Disable statistics** in production (`AdapterSetting.enableStatistics = false`)
3. **Use `encodeInto()`** when you have a pre-allocated buffer
4. **Reuse buffers** via buffer pooling (automatic with `encode()`)
5. **Estimate sizes accurately** in custom adapters to avoid buffer resizing

## Migration from 0.x

### Breaking Changes

1. **ByteBuffer support removed**: All APIs now use `ByteBufferL`
   ```kotlin
   // Before (0.x)
   val buffer: ByteBuffer = BinPack.encode(data)

   // After (1.0.0)
   val buffer: ByteBufferL = BinPack.encode(data)
   ```

2. **Custom adapters must use ByteBufferL**
   ```kotlin
   // Before (0.x)
   override fun write(value: T, buffer: ByteBuffer) { ... }

   // After (1.0.0)
   override fun write(value: T, buffer: ByteBufferL) { ... }
   ```

### New Features

- **AdapterRegistry**: Register custom adapters at runtime
- **Statistics**: Optional performance monitoring
- **Better errors**: More detailed error messages
- **Configuration**: Fine-tune limits and behavior

## Example: Complete Application

```kotlin
import net.ririfa.binpack.*

// 1. Define your data classes
data class User(
    val id: Long,
    val username: String,
    val email: String,
    val roles: List<String>
)

data class Session(
    val userId: Long,
    val token: String,
    val expiresAt: Long
)

// 2. Optional: Create custom adapters for optimized serialization
object SessionAdapter : TypeAdapter<Session> {
    override fun estimateSize(value: Session) =
        8 + 4 + value.token.length + 8

    override fun write(value: Session, buffer: ByteBufferL) {
        buffer.i64 = value.userId
        StringAdapter().write(value.token, buffer)
        buffer.i64 = value.expiresAt
    }

    override fun read(buffer: ByteBufferL): Session {
        val userId = buffer.i64
        val token = StringAdapter().read(buffer)
        val expiresAt = buffer.i64
        return Session(userId, token, expiresAt)
    }
}

fun main() {
    // 3. Register custom adapters
    AdapterRegistry.register<Session>(SessionAdapter)

    // 4. Enable statistics (optional)
    AdapterSetting.enableStatistics = true

    // 5. Use BinPack
    val user = User(
        id = 12345,
        username = "alice",
        email = "alice@example.com",
        roles = listOf("admin", "user")
    )

    // Encode
    val encoded = BinPack.encode(user)
    println("Encoded ${encoded.remaining} bytes")

    // Decode
    val decoded = BinPack.decode<User>(encoded)
    println("Decoded: $decoded")

    // Deep copy
    val copy = BinPack.deepCopy(user)
    println("Copy: $copy")

    // Show statistics
    println("\n${AdapterSetting.getStatistics()}")
}
```

## License

MIT License - see LICENSE file for details

## Contributing

Contributions are welcome! Please open an issue or submit a pull request.

## Links

- GitHub: https://github.com/ririf4/BinPack
- Issues: https://github.com/ririf4/BinPack/issues
