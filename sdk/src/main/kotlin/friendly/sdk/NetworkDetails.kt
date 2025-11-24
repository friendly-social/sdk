package friendly.sdk

public data class NetworkDetails(
    val friends: List<UserDetails>,
    val connections: List<UserDetails>,
) {
    public fun serializable(): NetworkDetailsSerializable =
        NetworkDetailsSerializable(
            friends = friends.map(UserDetails::serializable),
            connections = connections.map(UserDetails::serializable),
        )
}
