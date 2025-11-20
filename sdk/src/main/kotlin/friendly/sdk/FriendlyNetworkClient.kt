package friendly.sdk

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get

public class FriendlyNetworkClient(
    endpoint: FriendlyEndpoint,
    private val httpClient: HttpClient,
) {
    private val endpoint: FriendlyEndpoint = endpoint / "network"

    public sealed interface DetailsResult {
        public fun orThrow(): NetworkDetails

        public data class IOError(val cause: Throwable?) : DetailsResult {
            override fun orThrow(): Nothing = error("$this")
        }
        public data object ServerError : DetailsResult {
            override fun orThrow(): Nothing = error("$this")
        }
        public data object Unauthorized : DetailsResult {
            override fun orThrow(): Nothing = error("$this")
        }
        public data class Success(val details: NetworkDetails) : DetailsResult {
            override fun orThrow(): NetworkDetails = details
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
            OK -> response.body<NetworkDetailsSerializable>()
            else -> error("Unknown status code")
        }
        val details = responseBody.typed()
        return DetailsResult.Success(details)
    }
}
