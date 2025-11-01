package friendly.sdk

public data class UserDetails(
    val id: UserId,
    val accessHash: UserAccessHash,
    val nickname: Nickname,
    val description: UserDescription,
    val interests: List<Interest>,
) {
    public fun serializable(): UserDetailsSerializable =
        UserDetailsSerializable(
            id = id.serializable(),
            accessHash = accessHash.serializable(),
            nickname = nickname.serializable(),
            description = description.serializable(),
            interests = interests.serializable(),
        )
}
