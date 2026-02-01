package friendly.sdk

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public sealed interface NotificationDetailsSerializable {
    public fun typed(): NotificationDetails

    @Serializable
    @SerialName("new_request")
    public data class NewRequest(
        val from: UserDetailsSerializable,
        val isMutual: Boolean,
    ) : NotificationDetailsSerializable {
        override fun typed(): NotificationDetails =
            NotificationDetails.NewRequest(
                from = from.typed(),
                isMutual = isMutual,
            )
    }
}
