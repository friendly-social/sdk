package friendly.sdk

public data class UserDescription private constructor(val string: String) {
    public fun serializable(): UserDescriptionSerializable =
        UserDescriptionSerializable(string)

    public companion object {
        public val MaxLength: Int = 1_024

        public fun orThrow(string: String): UserDescription {
            require(string.length <= MaxLength)
            return UserDescription(string)
        }
    }
}
