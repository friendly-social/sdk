package friendly.sdk

public data class FeedQueue(val entries: List<Entry>) {
    public fun serializable(): FeedQueueSerializable {
        val entries = entries.map { entry -> entry.serializable() }
        return FeedQueueSerializable(entries)
    }

    public data class Entry(
        val isRequest: Boolean,
        val isExtendedNetwork: Boolean,
        val commonFriends: List<UserDetails>,
        val details: UserDetails,
    ) {
        public fun serializable(): FeedQueueSerializable.Entry =
            FeedQueueSerializable.Entry(
                isRequest = isRequest,
                isExtendedNetwork = isExtendedNetwork,
                commonFriends = commonFriends.map { user ->
                    user.serializable()
                },
                details = details.serializable(),
            )
    }
}
