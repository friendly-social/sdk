package friendly.sdk

public data class FirebaseToken private constructor(val string: String) {
    public fun serializable(): FirebaseTokenSerializable =
        FirebaseTokenSerializable(string = string)

    public companion object {
        // There's no official limit and usually tokens are sub-256
        public val MaxLength: Int = 4096

        public fun orThrow(string: String): FirebaseToken {
            require(string.length <= MaxLength) {
                "Firebase token is too large to be stored."
            }
            return FirebaseToken(string)
        }
    }
}
