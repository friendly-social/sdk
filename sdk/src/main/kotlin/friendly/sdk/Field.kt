package friendly.sdk

/**
 * Simple wrapper for any [value] that is useful when you want to distinguish
 * explicitly set `null` value and undefined value. That is handy when it comes
 * to passing which fields should be edited and which should be left.
 */
public data class Field<out T>(val value: T) {
    public inline fun <R> serializable(block: (T) -> R): FieldSerializable<R> =
        FieldSerializable(block(value))
}
