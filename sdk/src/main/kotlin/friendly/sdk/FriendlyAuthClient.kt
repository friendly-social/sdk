package friendly.sdk

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import kotlinx.serialization.Serializable

public class FriendlyAuthClient(
    endpoint: FriendlyEndpoint,
    private val httpClient: HttpClient,
) {
    private val endpoint: FriendlyEndpoint = endpoint / "auth"

    @Serializable
    private data class GenerateBody(
        val nickname: NicknameSerializable,
        val description: UserDescriptionSerializable,
        val interests: List<InterestSerializable>,
    )

    @Serializable
    private data class GenerateResponse(
        val token: TokenSerializable,
        val id: UserIdSerializable,
        val accessHash: UserAccessHashSerializable,
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
        val endpoint = endpoint / "generate"
        val response = httpClient
            .post(endpoint.string) {
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
            id = id.typed(),
            accessHash = accessHash.typed(),
            token = token.typed(),
        )
}
