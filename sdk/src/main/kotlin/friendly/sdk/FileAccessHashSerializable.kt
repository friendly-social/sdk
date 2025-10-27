package friendly.sdk

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationException

@JvmInline
@Serializable
public value class FileAccessHashSerializable(public val string: String) {
    init {
        if (string.length != FileAccessHash.Length) {
            throw SerializationException(
                "FileAccessHash is supposed to have a length of ${FileAccessHash.Length}, but was ${string.length}",
            )
        }
    }

    public fun typed(): FileAccessHash = FileAccessHash.orThrow(string)
}
