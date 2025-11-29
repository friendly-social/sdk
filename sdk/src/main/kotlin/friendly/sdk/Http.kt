package friendly.sdk

import io.ktor.client.HttpClient
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.invoke
import io.ktor.client.request.request
import io.ktor.client.request.url
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.HttpStatement

internal sealed interface SafeHttpRequestResult {
    data class IOError(val cause: Exception) : SafeHttpRequestResult
    data object ServerError : SafeHttpRequestResult
    data class Success(val response: HttpResponse) : SafeHttpRequestResult
}

internal suspend fun HttpClient.safeHttpRequest(
    url: String,
    block: HttpRequestBuilder.() -> Unit,
): SafeHttpRequestResult {
    val request = HttpRequestBuilder().apply {
        url(url)
        block()
    }
    val statement = HttpStatement(request, this)
    return try {
        val response = statement.execute()
        when (response.status) {
            InternalServerError -> SafeHttpRequestResult.ServerError
            else -> SafeHttpRequestResult.Success(response)
        }
    } catch (cause: Exception) {
        SafeHttpRequestResult.IOError(cause)
    }
}
