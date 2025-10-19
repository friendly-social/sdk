package friendly.sdk

public data class UserAccessHash private constructor(val string: String) {

    public fun serializable(): UserAccessHashSerializable =
        UserAccessHashSerializable(string)

    public companion object {
        public val Length: Int = 256

        public fun orThrow(string: String): UserAccessHash {
            require(string.length == Length) {
                "Token should have $Length length, but was ${string.length}"
            }
            return UserAccessHash(string)
        }
    }
}
