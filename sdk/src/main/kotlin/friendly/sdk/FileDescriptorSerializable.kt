package friendly.sdk

import kotlinx.serialization.Serializable

@Serializable
public data class FileDescriptorSerializable(
    val id: FileIdSerializable,
    val accessHash: FileAccessHashSerializable,
) {
    public fun typed(): FileDescriptor =
        FileDescriptor(id.typed(), accessHash.typed())
}
