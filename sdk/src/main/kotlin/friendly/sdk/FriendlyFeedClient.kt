package friendly.sdk

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get

public class FriendlyFeedClient(
    endpoint: FriendlyEndpoint,
    private val httpClient: HttpClient,
) {
    private val endpoint = endpoint / "feed"

    public sealed interface QueueResult {
        public fun orThrow(): FeedQueue

        public data class IOError(val cause: Exception) : QueueResult {
            override fun orThrow(): Nothing = error("$this")
        }
        public data object ServerError : QueueResult {
            override fun orThrow(): Nothing = error("$this")
        }
        public data object Unauthorized : QueueResult {
            override fun orThrow(): Nothing = error("$this")
        }
        public data class Success(val queue: FeedQueue) : QueueResult {
            override fun orThrow(): FeedQueue = queue
        }
    }

    public suspend fun queue(authorization: Authorization): QueueResult {
        val endpoint = endpoint / "queue"
        val request = httpClient.safeHttpRequest(endpoint.string) {
            method = Get
            authorization(authorization)
        }
        val response = when (request) {
            is IOError -> return QueueResult.IOError(request.cause)
            is ServerError -> return QueueResult.ServerError
            is Success -> request.response
        }
        val responseBody = when (response.status) {
            Unauthorized -> return QueueResult.Unauthorized
            OK -> response.body<FeedQueueSerializable>()
            else -> error("Unknown status code")
        }
        val queue = responseBody.typed()
        return QueueResult.Success(queue)
    }
}
