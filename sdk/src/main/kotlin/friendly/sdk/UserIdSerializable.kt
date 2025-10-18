package friendly.sdk

import kotlinx.serialization.Serializable

@Serializable
@JvmInline
public value class UserIdSerializable(public val long: Long) {
    public fun typed(): UserId = UserId(long)
}
