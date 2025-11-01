package friendly.sdk

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get

public class FriendlyUsersClient(
    endpoint: FriendlyEndpoint,
    private val httpClient: HttpClient,
) {
    private val endpoint = endpoint / "users"

    public suspend fun details(authorization: Authorization): UserDetails {
        val endpoint = endpoint / "details"
        val request = httpClient.get(endpoint.string) {
            authorization(authorization)
        }
        val response = request.body<UserDetailsSerializable>()
        return response.typed()
    }

    public suspend fun details(
        authorization: Authorization,
        id: UserId,
        accessHash: UserAccessHash,
    ): UserDetails {
        val endpoint = endpoint / "details" / "${id.long}" / accessHash.string
        val request = httpClient.get(endpoint.string) {
            authorization(authorization)
        }
        val response = request.body<UserDetailsSerializable>()
        return response.typed()
    }
}
