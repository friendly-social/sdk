package friendly.sdk

public data class InterestList private constructor(val raw: List<Interest>) {
    public fun serializable(): InterestListSerializable =
        InterestListSerializable(
            raw = raw.map(Interest::serializable),
        )

    public companion object {
        public fun orThrow(vararg raw: Interest): InterestList =
            orThrow(raw.toList())

        public fun orThrow(raw: List<Interest>): InterestList {
            require(raw.toSet().size == raw.size) {
                "A list of interests must be unique"
            }
            return InterestList(raw)
        }
    }
}
