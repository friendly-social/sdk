package friendly.sdk

public data class FileAccessHash private constructor(val string: String) {

    public fun serializable(): FileAccessHashSerializable =
        FileAccessHashSerializable(string)

    public companion object {
        public val Length: Int = 256

        public fun orThrow(string: String): FileAccessHash {
            require(string.length == Length) {
                "Token should have $Length length, but was ${string.length}"
            }
            return FileAccessHash(string)
        }
    }
}
