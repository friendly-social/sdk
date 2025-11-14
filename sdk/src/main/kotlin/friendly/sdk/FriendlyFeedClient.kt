package friendly.sdk

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get

public class FriendlyFeedClient(
    endpoint: FriendlyEndpoint,
    private val httpClient: HttpClient,
) {
    private val endpoint = endpoint / "feed"

    public suspend fun queue(authorization: Authorization): FeedQueue {
        val endpoint = endpoint / "queue"
        val response = httpClient.get(endpoint.string) {
            authorization(authorization)
        }.body<FeedQueueSerializable>()
        return response.typed()
    }
}
