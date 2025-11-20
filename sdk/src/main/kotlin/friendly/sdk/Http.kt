package friendly.sdk

import io.ktor.client.HttpClient
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.request
import io.ktor.client.request.url
import io.ktor.client.statement.HttpResponse
import kotlinx.io.IOException

internal sealed interface SafeHttpRequestResult {
    data class IOError(val cause: IOException) : SafeHttpRequestResult
    data object ServerError : SafeHttpRequestResult
    data class Success(val response: HttpResponse) : SafeHttpRequestResult
}

internal suspend inline fun HttpClient.safeHttpRequest(
    url: String,
    block: HttpRequestBuilder.() -> Unit,
): SafeHttpRequestResult = try {
    val response = request {
        url(url)
        block()
    }
    when (response.status) {
        InternalServerError -> SafeHttpRequestResult.ServerError
        else -> SafeHttpRequestResult.Success(response)
    }
} catch (cause: IOException) {
    SafeHttpRequestResult.IOError(cause)
}
