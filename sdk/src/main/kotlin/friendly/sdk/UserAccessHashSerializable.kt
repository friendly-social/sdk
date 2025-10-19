package friendly.sdk

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationException

@JvmInline
@Serializable
public value class UserAccessHashSerializable(public val string: String) {
    init {
        if (string.length != UserAccessHash.Length) {
            throw SerializationException(
                "UserAccessHash is supposed to have a length of ${UserAccessHash.Length}, but was ${string.length}",
            )
        }
    }

    public fun typed(): UserAccessHash = UserAccessHash.orThrow(string)
}
