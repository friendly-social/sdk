package friendly.sdk

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.setBody
import kotlinx.serialization.Serializable

public class FriendlyAuthClient(
    endpoint: FriendlyEndpoint,
    private val httpClient: HttpClient,
) {
    private val endpoint: FriendlyEndpoint = endpoint / "auth"

    @Serializable
    private data class GenerateRequestBody(
        val nickname: NicknameSerializable,
        val description: UserDescriptionSerializable,
        val interests: InterestListSerializable,
        val avatar: FileDescriptorSerializable?,
        val socialLink: SocialLinkSerializable?,
    )

    @Serializable
    private data class GenerateResponseBody(
        val token: TokenSerializable,
        val id: UserIdSerializable,
        val accessHash: UserAccessHashSerializable,
    )

    public sealed interface GenerateResult {
        public fun orThrow(): Authorization

        public data class IOError(val cause: Exception) : GenerateResult {
            override fun orThrow(): Nothing = error("$this")
        }
        public data object ServerError : GenerateResult {
            override fun orThrow(): Nothing = error("$this")
        }
        public data class Success(val authorization: Authorization) :
            GenerateResult {
            override fun orThrow(): Authorization = authorization
        }
    }

    public suspend fun generate(
        nickname: Nickname,
        description: UserDescription,
        interests: InterestList,
        avatar: FileDescriptor?,
        socialLink: SocialLink?,
    ): GenerateResult {
        val endpoint = endpoint / "generate"
        val requestBody = GenerateRequestBody(
            nickname = nickname.serializable(),
            description = description.serializable(),
            interests = interests.serializable(),
            avatar = avatar?.serializable(),
            socialLink = socialLink?.serializable(),
        )
        val request = httpClient.safeHttpRequest(endpoint.string) {
            method = Post
            setBody(requestBody)
        }
        val response = when (request) {
            is IOError -> return GenerateResult.IOError(request.cause)
            is ServerError -> return GenerateResult.ServerError
            is Success -> request.response
        }
        val responseBody = when (response.status) {
            OK -> response.body<GenerateResponseBody>()
            else -> error("Unknown status")
        }
        val authorization = responseBody.toAuthorization()
        return GenerateResult.Success(authorization)
    }

    private fun GenerateResponseBody.toAuthorization(): Authorization =
        Authorization(
            id = id.typed(),
            accessHash = accessHash.typed(),
            token = token.typed(),
        )

    @Serializable
    private data class FirebaseRequestBody(
        val firebaseToken: FirebaseTokenSerializable,
    )

    @Serializable
    private data object FirebaseResponseBody

    public sealed interface FirebaseResult {
        public fun orThrow()

        public data class IOError(val cause: Exception) : FirebaseResult {
            override fun orThrow(): Nothing = error("$this")
        }
        public data object ServerError : FirebaseResult {
            override fun orThrow(): Nothing = error("$this")
        }
        public data object Success : FirebaseResult {
            override fun orThrow() {}
        }
    }

    public suspend fun firebase(
        authorization: Authorization,
        firebaseToken: FirebaseToken,
    ): FirebaseResult {
        val endpoint = endpoint / "firebase"
        val requestBody = FirebaseRequestBody(
            firebaseToken = firebaseToken.serializable(),
        )
        val request = httpClient.safeHttpRequest(endpoint.string) {
            method = Post
            setBody(requestBody)
        }
        val response = when (request) {
            is IOError -> return FirebaseResult.IOError(request.cause)
            is ServerError -> return FirebaseResult.ServerError
            is Success -> request.response
        }
        when (response.status) {
            OK -> response.body<FirebaseRequestBody>()
            else -> error("Unknown status")
        }
        return FirebaseResult.Success
    }
}
