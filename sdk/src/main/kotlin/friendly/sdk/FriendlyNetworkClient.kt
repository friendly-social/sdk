package friendly.sdk

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get

public class FriendlyNetworkClient(
    endpoint: FriendlyEndpoint,
    private val httpClient: HttpClient,
) {
    private val endpoint: FriendlyEndpoint = endpoint / "network"

    public suspend fun details(authorization: Authorization): NetworkDetails {
        val endpoint = endpoint / "details"
        val request = httpClient.get(endpoint.string) {
            authorization(authorization)
        }
        val response = request.body<NetworkDetailsSerializable>()
        return response.typed()
    }
}
