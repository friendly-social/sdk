package friendly.sdk

public sealed interface NotificationDetails {
    public fun serializable(): NotificationDetailsSerializable

    public data class NewRequest(val from: UserDetails, val isMutual: Boolean) :
        NotificationDetails {
        override fun serializable(): NotificationDetailsSerializable =
            NotificationDetailsSerializable.NewRequest(
                from = from.serializable(),
                isMutual = isMutual,
            )
    }
}
