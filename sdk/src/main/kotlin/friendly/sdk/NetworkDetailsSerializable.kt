package friendly.sdk

import kotlinx.serialization.Serializable

@Serializable
public data class NetworkDetailsSerializable(
    val friends: List<UserDetailsSerializable>,
    val connections: List<UserDetailsSerializable>,
) {
    public fun typed(): NetworkDetails = NetworkDetails(
        friends = friends.map(UserDetailsSerializable::typed),
        connections = connections.map(UserDetailsSerializable::typed),
    )
}
