package friendly.sdk

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

public class FriendlyFriendsClient(
    endpoint: FriendlyEndpoint,
    private val httpClient: HttpClient,
) {
    private val endpoint: FriendlyEndpoint = endpoint / "friends"

    @Serializable
    private data class GenerateResponse(val token: FriendTokenSerializable)

    public suspend fun generate(authorization: Authorization): FriendToken {
        val endpoint = endpoint / "generate"
        val response = httpClient
            .post(endpoint.string) {
                authorization(authorization)
            }
            .body<GenerateResponse>()
        return response.token.typed()
    }

    @Serializable
    private data class AddBody(
        val token: FriendTokenSerializable,
        val userId: UserIdSerializable,
    )

    @Serializable
    private sealed interface AddResponse {
        @Serializable
        @SerialName("FriendTokenExpired")
        data object FriendTokenExpired : AddResponse

        @Serializable
        @SerialName("Success")
        data object Success : AddResponse
    }

    public sealed interface AddResult {
        public data object FriendTokenExpired : AddResult
        public data object Success : AddResult
    }

    public suspend fun add(
        authorization: Authorization,
        token: FriendToken,
        userId: UserId,
    ): AddResult {
        val endpoint = endpoint / "add"
        val body = AddBody(token.serializable(), userId.serializable())
        val response = httpClient
            .post(endpoint.string) {
                authorization(authorization)
                setBody(body)
            }
            .body<AddResponse>()
        return response.toResult()
    }

    private fun AddResponse.toResult(): AddResult = when (this) {
        FriendTokenExpired -> FriendTokenExpired
        Success -> Success
    }
}
