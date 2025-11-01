package friendly.sdk

import kotlinx.serialization.Serializable

@Serializable
public data class UserDetailsSerializable(
    val id: UserIdSerializable,
    val accessHash: UserAccessHashSerializable,
    val nickname: NicknameSerializable,
    val description: UserDescriptionSerializable,
    val interests: List<InterestSerializable>,
) {
    public fun typed(): UserDetails = UserDetails(
        id = id.typed(),
        accessHash = accessHash.typed(),
        nickname = nickname.typed(),
        description = description.typed(),
        interests = interests.typed(),
    )
}
