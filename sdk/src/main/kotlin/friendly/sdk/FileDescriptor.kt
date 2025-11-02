package friendly.sdk

public data class FileDescriptor(
    val id: FileId,
    val accessHash: FileAccessHash,
) {
    public fun serializable(): FileDescriptorSerializable =
        FileDescriptorSerializable(
            id = id.serializable(),
            accessHash = accessHash.serializable(),
        )
}
