package friendly.sdk

import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.engine.config
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json

public class FriendlyClient(
    private val endpoint: FriendlyEndpoint,
    httpClient: HttpClient = HttpClient(CIO),
) {
    private val httpClient = httpClient.config {
        install(ContentNegotiation) {
            json()
        }
        defaultRequest {
            contentType(ContentType.Application.Json)
        }
        expectSuccess = true
    }

    public val auth: FriendlyAuthClient = FriendlyAuthClient(
        endpoint = endpoint,
        httpClient = this.httpClient,
    )

    public val files: FriendlyFilesClient = FriendlyFilesClient(
        endpoint = endpoint,
        httpClient = this.httpClient,
    )

    public val users: FriendlyUsersClient = FriendlyUsersClient(
        endpoint = endpoint,
        httpClient = this.httpClient,
    )

    public val friends: FriendlyFriendsClient = FriendlyFriendsClient(
        endpoint = endpoint,
        httpClient = this.httpClient,
    )

    public companion object {
        public fun localhost(
            port: Int = 8080,
            httpClient: HttpClient = HttpClient(CIO),
        ): FriendlyClient {
            val endpoint = FriendlyEndpoint.localhost(port)
            return FriendlyClient(endpoint, httpClient)
        }

        public fun meetacy(
            httpClient: HttpClient = HttpClient(CIO),
        ): FriendlyClient {
            val endpoint = FriendlyEndpoint.meetacy()
            return FriendlyClient(endpoint, httpClient)
        }
    }
}
