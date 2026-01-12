package friendly.sdk

import io.ktor.client.request.HttpRequestBuilder

public data class Authorization(
    val id: UserId,
    val accessHash: UserAccessHash,
    val token: Token,
)

internal fun HttpRequestBuilder.authorization(authorization: Authorization) {
    headers["X-User-Id"] = authorization.id.long.toString()
    headers["X-Token"] = authorization.token.string
}
