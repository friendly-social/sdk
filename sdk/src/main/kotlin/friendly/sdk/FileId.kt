package friendly.sdk

public data class FileId(public val long: Long) {
    public fun serializable(): FileIdSerializable = FileIdSerializable(long)
}
