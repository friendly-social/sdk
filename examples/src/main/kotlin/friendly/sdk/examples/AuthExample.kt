package friendly.sdk.examples

import friendly.sdk.Interest
import friendly.sdk.Nickname
import friendly.sdk.UserDescription

suspend fun authExample() {
    val authorization = client.auth.generate(
        nickname = Nickname.orThrow("y9san9"),
        description = UserDescription.orThrow("Phronology Evangelist"),
        interests = listOf(
            Interest.orThrow("phronology"),
        ),
    )
    println(authorization)
}
