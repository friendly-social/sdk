package friendly.sdk

public data class FriendToken private constructor(val string: String) {
    public fun serializable(): FriendTokenSerializable =
        FriendTokenSerializable(string)

    public companion object {
        public val Length: Int = 256

        public fun orThrow(string: String): FriendToken {
            require(string.length == Length) {
                "FriendToken should have $Length length, but was ${string.length}"
            }
            return FriendToken(string)
        }
    }
}
