package friendly.sdk

public data class Authorization(
    val id: UserId,
    val accessHash: UserAccessHash,
    val token: Token,
)
