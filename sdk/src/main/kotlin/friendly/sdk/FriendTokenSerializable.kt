package friendly.sdk

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationException

@Serializable
@JvmInline
public value class FriendTokenSerializable(public val string: String) {
    init {
        if (string.length != FriendToken.Length) {
            throw SerializationException(
                "FriendToken is supposed to have a length of ${FriendToken.Length}, but was ${string.length}",
            )
        }
    }

    public fun typed(): FriendToken = FriendToken.orThrow(string)
}
