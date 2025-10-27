package friendly.sdk

import kotlinx.serialization.Serializable

@Serializable
@JvmInline
public value class FileIdSerializable(public val long: Long) {
    public fun typed(): FileId = FileId(long)
}
