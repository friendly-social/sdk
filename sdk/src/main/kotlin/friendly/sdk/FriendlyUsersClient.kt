package friendly.sdk

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get

public class FriendlyUsersClient(
    endpoint: FriendlyEndpoint,
    private val httpClient: HttpClient,
) {
    private val endpoint = endpoint / "users"

    public sealed interface DetailsResult {
        public fun orThrow(): UserDetails

        public data class IOError(val cause: Throwable) : DetailsResult {
            override fun orThrow(): Nothing = error("$this")
        }
        public data object ServerError : DetailsResult {
            override fun orThrow(): Nothing = error("$this")
        }
        public data object Unauthorized : DetailsResult {
            override fun orThrow(): Nothing = error("$this")
        }
        public data class Success(val details: UserDetails) : DetailsResult {
            override fun orThrow(): UserDetails = details
        }
    }

    public suspend fun details(authorization: Authorization): DetailsResult {
        val endpoint = endpoint / "details"
        val request = httpClient.safeHttpRequest(endpoint.string) {
            method = Get
            authorization(authorization)
        }
        val response = when (request) {
            is IOError -> return DetailsResult.IOError(request.cause)
            is ServerError -> return DetailsResult.ServerError
            is Success -> request.response
        }
        val responseBody = when (response.status) {
            Unauthorized -> return DetailsResult.Unauthorized
            OK -> response.body<UserDetailsSerializable>()
            else -> error("Unknown status code")
        }
        val details = responseBody.typed()
        return DetailsResult.Success(details)
    }

    public suspend fun details(
        authorization: Authorization,
        id: UserId,
        accessHash: UserAccessHash,
    ): DetailsResult {
        val endpoint = endpoint / "details" / "${id.long}" / accessHash.string
        val request = httpClient.safeHttpRequest(endpoint.string) {
            method = Get
            authorization(authorization)
        }
        val response = when (request) {
            is IOError -> return DetailsResult.IOError(request.cause)
            is ServerError -> return DetailsResult.ServerError
            is Success -> request.response
        }
        val responseBody = when (response.status) {
            Unauthorized -> return DetailsResult.Unauthorized
            OK -> response.body<UserDetailsSerializable>()
            else -> error("Unknown status code")
        }
        val details = responseBody.typed()
        return DetailsResult.Success(details)
    }
}
