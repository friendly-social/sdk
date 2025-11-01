package friendly.sdk.examples

import friendly.sdk.Interest
import friendly.sdk.Nickname
import friendly.sdk.UserDescription

// This example only works in production
suspend fun usersExample() {
    println()
    val authorization1 = client.auth.generate(
        nickname = Nickname.orThrow("y9san9"),
        description = UserDescription.orThrow("Phronology Evangelist"),
        interests = listOf(
            Interest.orThrow("phronology"),
        ),
    )
    println("=== Authorization 1 ===")
    println(authorization1)
    println()
    val user1Details = client.users.details(authorization1)
    println("=== Self User Details ===")
    println(user1Details)
    println()
    val authorization2 = client.auth.generate(
        nickname = Nickname.orThrow("y9demn"),
        description = UserDescription.orThrow("Zed Enjoyer"),
        interests = listOf(
            Interest.orThrow("zed"),
        ),
    )
    println("=== Authorization 2 ===")
    println(authorization2)
    println()
    val user2Details = client.users.details(
        authorization = authorization1,
        id = authorization2.id,
        accessHash = authorization2.accessHash,
    )
    println("=== Other User Details ===")
    println(user2Details)
    println()
}
