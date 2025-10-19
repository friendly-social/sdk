package friendly.sdk

public data class Token private constructor(val string: String) {
    public fun serializable(): TokenSerializable = TokenSerializable(string)

    public companion object {
        public val Length: Int = 256

        public fun orThrow(string: String): Token {
            require(string.length == Length) {
                "Token should have $Length length, but was ${string.length}"
            }
            return Token(string)
        }
    }
}
