package friendly.sdk

public suspend fun main() {
    val client = FriendlyClient.meetacy()
    val authorization = client.auth.generate(
        nickname = Nickname.orThrow("y9san9"),
        description = UserDescription.orThrow("Phronology Evangelist"),
        interests = listOf(
            Interest.orThrow("Phronology"),
        ),
    )
    println(authorization)
}
