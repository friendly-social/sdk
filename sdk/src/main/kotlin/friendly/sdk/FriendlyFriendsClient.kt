package friendly.sdk

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.setBody
import kotlinx.io.IOException
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

public class FriendlyFriendsClient(
    endpoint: FriendlyEndpoint,
    private val httpClient: HttpClient,
) {
    private val endpoint: FriendlyEndpoint = endpoint / "friends"

    @Serializable
    private data class GenerateResponseBody(val token: FriendTokenSerializable)

    public sealed interface GenerateResult {
        public fun orThrow(): FriendToken

        public data class IOError(val cause: Throwable?) : GenerateResult {
            override fun orThrow(): Nothing = error("$this")
        }
        public data object ServerError : GenerateResult {
            override fun orThrow(): Nothing = error("$this")
        }
        public data object Unauthorized : GenerateResult {
            override fun orThrow(): Nothing = error("$this")
        }
        public data class Success(val token: FriendToken) : GenerateResult {
            override fun orThrow(): FriendToken = token
        }
    }

    public suspend fun generate(authorization: Authorization): GenerateResult {
        val endpoint = endpoint / "generate"
        val request = httpClient.safeHttpRequest(endpoint.string) {
            method = Post
            authorization(authorization)
        }
        val response = when (request) {
            is IOError -> return GenerateResult.IOError(request.cause)
            is ServerError -> return GenerateResult.ServerError
            is Success -> request.response
        }
        val responseBody = when (response.status) {
            Unauthorized -> return GenerateResult.Unauthorized
            OK -> response.body<GenerateResponseBody>()
            else -> error("Unknown status code")
        }
        val token = responseBody.token.typed()
        return GenerateResult.Success(token)
    }

    @Serializable
    private data class AddRequestBody(
        val token: FriendTokenSerializable,
        val userId: UserIdSerializable,
    )

    @Serializable
    private sealed interface AddResponseBody {
        @Serializable
        @SerialName("FriendTokenExpired")
        data object FriendTokenExpired : AddResponseBody

        @Serializable
        @SerialName("Success")
        data object Success : AddResponseBody
    }

    public sealed interface AddResult {
        public fun orThrow()

        public data class IOError(val cause: IOException) : AddResult {
            override fun orThrow(): Nothing = error("$this")
        }
        public data object ServerError : AddResult {
            override fun orThrow(): Nothing = error("$this")
        }
        public data object Unauthorized : AddResult {
            override fun orThrow(): Nothing = error("$this")
        }
        public data object FriendTokenExpired : AddResult {
            override fun orThrow(): Nothing = error("$this")
        }
        public data object Success : AddResult {
            override fun orThrow() {}
        }
    }

    public suspend fun add(
        authorization: Authorization,
        token: FriendToken,
        userId: UserId,
    ): AddResult {
        val endpoint = endpoint / "add"
        val requestBody = AddRequestBody(
            token = token.serializable(),
            userId = userId.serializable(),
        )
        val request = httpClient.safeHttpRequest(endpoint.string) {
            method = Post
            authorization(authorization)
            setBody(requestBody)
        }
        val response = when (request) {
            is IOError -> return AddResult.IOError(request.cause)
            is ServerError -> return AddResult.ServerError
            is Success -> request.response
        }
            .body<AddResponseBody>()
        return response.toResult()
    }

    private fun AddResponseBody.toResult(): AddResult = when (this) {
        FriendTokenExpired -> FriendTokenExpired
        Success -> Success
    }
}
