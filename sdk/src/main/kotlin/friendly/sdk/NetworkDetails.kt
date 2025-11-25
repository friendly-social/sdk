package friendly.sdk

public data class NetworkDetails(val friends: List<UserDetails>) {
    public fun serializable(): NetworkDetailsSerializable =
        NetworkDetailsSerializable(
            friends = friends.map(UserDetails::serializable),
        )
}
