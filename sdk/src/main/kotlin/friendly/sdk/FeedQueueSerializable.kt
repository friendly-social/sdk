package friendly.sdk

import kotlinx.serialization.Serializable

@Serializable
public data class FeedQueueSerializable(val entries: List<Entry>) {
    public fun typed(): FeedQueue {
        val entries = entries.map { entry -> entry.typed() }
        return FeedQueue(entries)
    }

    @Serializable
    public data class Entry(
        val isRequest: Boolean,
        val isExtendedNetwork: Boolean,
        val commonFriends: List<UserDetailsSerializable>,
        val details: UserDetailsSerializable,
    ) {
        public fun typed(): FeedQueue.Entry = FeedQueue.Entry(
            isRequest = isRequest,
            isExtendedNetwork = isExtendedNetwork,
            commonFriends = commonFriends.map { friend -> friend.typed() },
            details = details.typed(),
        )
    }
}
