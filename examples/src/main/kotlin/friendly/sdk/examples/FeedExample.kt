package friendly.sdk.examples

import friendly.sdk.Interest
import friendly.sdk.Nickname
import friendly.sdk.UserDescription

suspend fun feedExample() {
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
    val authorization3 = client.auth.generate(
        nickname = Nickname.orThrow("y9kap"),
        description = UserDescription.orThrow("Senior Python Developer"),
        interests = listOf(
            Interest.orThrow("python3+"),
        ),
        avatar = null,
    )
    println("=== Authorization 3 ===")
    println(authorization3)
    println()
    val authorization4 = client.auth.generate(
        nickname = Nickname.orThrow("otomir23"),
        description = UserDescription.orThrow("Webring Master"),
        interests = listOf(
            Interest.orThrow("webring"),
        ),
        avatar = null,
    )
    println("=== Authorization 4 ===")
    println(authorization4)
    println()
    val friend1Token = client.friends.generate(authorization1)
    println("=== Friend 1 Token ===")
    println(friend1Token)
    println()
    val add1ResultSuccess = client.friends
        .add(authorization2, friend1Token, authorization1.id)
    println("=== Add Friend 1 Success ===")
    println(add1ResultSuccess)
    println()
    val friend2Token = client.friends.generate(authorization2)
    println("=== Friend 2 Token ===")
    println(friend1Token)
    println()
    val add2ResultSuccess = client.friends
        .add(authorization3, friend2Token, authorization2.id)
    println("=== Add Friend 2 Success ===")
    println(add2ResultSuccess)
    println()
    val friend3Token = client.friends.generate(authorization3)
    println("=== Friend 3 Token ===")
    println(friend2Token)
    println()
    val add3ResultSuccess = client.friends
        .add(authorization4, friend3Token, authorization3.id)
    println("=== Add Friend 3 Success ===")
    println(add3ResultSuccess)
    println()
    val network = client.feed.queue(authorization1)
    println("=== Feed ===")
    println(network.entries)
    println()
}
