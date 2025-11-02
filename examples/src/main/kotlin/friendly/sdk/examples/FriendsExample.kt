package friendly.sdk.examples

import friendly.sdk.Interest
import friendly.sdk.Nickname
import friendly.sdk.UserDescription

suspend fun friendsExample() {
    println()
    val authorization1 = client.auth.generate(
        nickname = Nickname.orThrow("y9san9"),
        description = UserDescription.orThrow("Phronology Evangelist"),
        interests = listOf(
            Interest.orThrow("phronology"),
        ),
        avatar = null,
    )
    println("=== Authorization 1 ===")
    println(authorization1)
    println()
    val friendToken = client.friends.generate(authorization1)
    println("=== Friend Token ===")
    println(friendToken)
    println()
    val authorization2 = client.auth.generate(
        nickname = Nickname.orThrow("y9demn"),
        description = UserDescription.orThrow("Zed Enjoyer"),
        interests = listOf(
            Interest.orThrow("zed"),
        ),
        avatar = null,
    )
    println("=== Authorization 2 ===")
    println(authorization2)
    println()
    val addResultSuccess = client.friends
        .add(authorization2, friendToken, authorization1.id)
    println("=== Add Friend Success ===")
    println(addResultSuccess)
    println()
    val addResultFailure = client.friends
        .add(authorization2, friendToken, authorization1.id)
    println("=== Add Friend Failure ===")
    println(addResultFailure)
    println()
}
