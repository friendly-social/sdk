package friendly.sdk

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationException

@Serializable
@JvmInline
public value class TokenSerializable(public val string: String) {
    init {
        if (string.length != Token.Length) {
            throw SerializationException(
                "Token is supposed to have a length of ${Token.Length}, but was ${string.length}",
            )
        }
    }

    public fun typed(): Token = Token.orThrow(string)
}
