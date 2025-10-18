package friendly.sdk

public data class Token private constructor(val string: String) {
    public fun serializable(): TokenSerializable = TokenSerializable(string)

    public companion object {
        public val Length: Int = 256

        public val Alphabet: String =
            "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789_-.~"

        // todo: make it a pure function
        public fun random(): Token {
            val string = buildString {
                repeat(Length) {
                    // We shouldn't use that pseudorandom LoL
                    append(Alphabet.random())
                }
            }
            return Token(string)
        }

        public fun orThrow(string: String): Token {
            require(string.length == Length) {
                "Token should have $Length length, but was ${string.length}"
            }
            return Token(string)
        }
    }
}
