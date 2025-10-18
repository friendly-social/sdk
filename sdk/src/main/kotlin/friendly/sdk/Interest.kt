package friendly.sdk

public data class Interest private constructor(val string: String) {
    public fun serializable(): InterestSerializable =
        InterestSerializable(string)

    public companion object {
        public val MaxLength: Int = 64

        public fun orThrow(string: String): Interest {
            require(string.length <= MaxLength)
            return Interest(string)
        }
    }
}

public fun List<Interest>.serializable(): List<InterestSerializable> =
    map(Interest::serializable)
