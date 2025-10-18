package friendly.sdk

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.serialization.Serializable

public class FriendlyAuthClient(
    private val endpoint: FriendlyEndpoint,
    private val httpClient: HttpClient,
) {
    @Serializable
    private data class GenerateBody(
        val nickname: NicknameSerializable,
        val description: UserDescriptionSerializable,
        val interests: List<InterestSerializable>,
    )

    @Serializable
    private data class GenerateResponse(
        val userId: UserIdSerializable,
        val token: TokenSerializable,
    )

    public suspend fun generate(
        nickname: Nickname,
        description: UserDescription,
        interests: List<Interest>,
    ): Authorization {
        val body = GenerateBody(
            nickname = nickname.serializable(),
            description = description.serializable(),
            interests = interests.serializable(),
        )
        val response = httpClient
            .post("${endpoint.string}/auth/generate") {
                contentType(ContentType.Application.Json)
                setBody(body)
            }
            .body<GenerateResponse>()
        return response.toAuthorization()
    }

    private fun List<Interest>.serializable(): List<InterestSerializable> =
        map { interest ->
            interest.serializable()
        }

    private fun GenerateResponse.toAuthorization(): Authorization =
        Authorization(
            userId = userId.typed(),
            token = token.typed(),
        )
}
