package friendly.sdk

public data class Nickname private constructor(val string: String) {
    public fun serializable(): NicknameSerializable =
        NicknameSerializable(string)

    public companion object {
        public val MaxLength: Int = 256

        public fun orThrow(string: String): Nickname {
            require(string.length <= MaxLength)
            return Nickname(string)
        }
    }
}
