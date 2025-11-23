package friendly.sdk

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.setBody
import kotlinx.io.IOException
import kotlinx.serialization.Serializable

public class FriendlyConnectionsClient(
    endpoint: FriendlyEndpoint,
    private val httpClient: HttpClient,
) {
    private val endpoint: FriendlyEndpoint = endpoint / "connections"

    @Serializable
    private data class RequestRequestBody(
        val userId: UserIdSerializable,
        val userAccessHash: UserAccessHashSerializable,
    )

    @Serializable
    internal data object RequestResponseBody

    public sealed interface RequestResult {
        public fun orThrow()

        public data class IOError(val cause: IOException) : RequestResult {
            override fun orThrow(): Nothing = error("$this")
        }
        public data object ServerError : RequestResult {
            override fun orThrow(): Nothing = error("$this")
        }
        public data object Unauthorized : RequestResult {
            override fun orThrow(): Nothing = error("$this")
        }
        public data object NotFound : RequestResult {
            override fun orThrow(): Nothing = error("$this")
        }
        public data object Success : RequestResult {
            override fun orThrow() {}
        }
    }

    public suspend fun request(
        authorization: Authorization,
        userId: UserId,
        userAccessHash: UserAccessHash,
    ): DeclineResult {
        val endpoint = endpoint / "request"
        val requestBody = DeclineRequestBody(
            userId = userId.serializable(),
            userAccessHash = userAccessHash.serializable(),
        )
        val request = httpClient.safeHttpRequest(endpoint.string) {
            method = Post
            authorization(authorization)
            setBody(requestBody)
        }
        val response = when (request) {
            is IOError -> return DeclineResult.IOError(request.cause)
            is ServerError -> return DeclineResult.ServerError
            is Success -> request.response
        }
        when (response.status) {
            OK -> response.body<DeclineResponseBody>()
            Unauthorized -> return DeclineResult.Unauthorized
            NotFound -> return DeclineResult.NotFound
            else -> error("Unknown status code")
        }
        return DeclineResult.Success
    }

    @Serializable
    private data class DeclineRequestBody(
        val userId: UserIdSerializable,
        val userAccessHash: UserAccessHashSerializable,
    )

    @Serializable
    internal data object DeclineResponseBody

    public sealed interface DeclineResult {
        public fun orThrow()

        public data class IOError(val cause: IOException) : DeclineResult {
            override fun orThrow(): Nothing = error("$this")
        }
        public data object ServerError : DeclineResult {
            override fun orThrow(): Nothing = error("$this")
        }
        public data object Unauthorized : DeclineResult {
            override fun orThrow(): Nothing = error("$this")
        }
        public data object NotFound : DeclineResult {
            override fun orThrow(): Nothing = error("$this")
        }
        public data object Success : DeclineResult {
            override fun orThrow() {}
        }
    }

    public suspend fun decline(
        authorization: Authorization,
        userId: UserId,
        userAccessHash: UserAccessHash,
    ): RequestResult {
        val endpoint = endpoint / "decline"
        val requestBody = DeclineRequestBody(
            userId = userId.serializable(),
            userAccessHash = userAccessHash.serializable(),
        )
        val request = httpClient.safeHttpRequest(endpoint.string) {
            method = Post
            authorization(authorization)
            setBody(requestBody)
        }
        val response = when (request) {
            is IOError -> return RequestResult.IOError(request.cause)
            is ServerError -> return RequestResult.ServerError
            is Success -> request.response
        }
        when (response.status) {
            OK -> response.body<RequestResponseBody>()
            Unauthorized -> return RequestResult.Unauthorized
            NotFound -> return RequestResult.NotFound
            else -> error("Unknown status code")
        }
        return RequestResult.Success
    }
}
