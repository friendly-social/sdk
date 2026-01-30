package friendly.sdk

import kotlinx.serialization.Serializable

@JvmInline
@Serializable
public value class FirebaseTokenSerializable(public val string: String) {
    init {
        require(string.length <= FirebaseToken.MaxLength) {
            "Firebase token is too large to be stored."
        }
    }

    public fun typed(): FirebaseToken = FirebaseToken.orThrow(string = string)
}
