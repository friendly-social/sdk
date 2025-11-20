package friendly.sdk

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.content.ProgressListener
import io.ktor.client.plugins.onUpload
import io.ktor.client.request.forms.MultiPartFormDataContent
import io.ktor.client.request.forms.append
import io.ktor.client.request.forms.formData
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import kotlinx.io.Sink
import kotlinx.serialization.Serializable

public class FriendlyFilesClient(
    endpoint: FriendlyEndpoint,
    private val httpClient: HttpClient,
) {
    private val endpoint = endpoint / "files"

    public fun getEndpoint(descriptor: FileDescriptor): FriendlyEndpoint =
        endpoint /
            "download" /
            "${descriptor.id.long}" /
            descriptor.accessHash.string

    @Serializable
    private data class UploadFileResponseBody(
        val id: FileIdSerializable,
        val accessHash: FileAccessHashSerializable,
    )

    public sealed interface UploadFileResult {
        public fun orThrow(): FileDescriptor

        public data class IOError(val cause: Throwable?) : UploadFileResult {
            override fun orThrow(): Nothing = error("$this")
        }
        public data object ServerError : UploadFileResult {
            override fun orThrow(): Nothing = error("$this")
        }
        public data class Success(val descriptor: FileDescriptor) :
            UploadFileResult {
            override fun orThrow(): FileDescriptor = descriptor
        }
    }

    public suspend fun upload(
        filename: String,
        contentType: ContentType? = null,
        size: Long? = null,
        onUpload: ProgressListener? = null,
        bodyBuilder: Sink.() -> Unit,
    ): UploadFileResult {
        val endpoint = endpoint / "upload"
        val requestBody = MultiPartFormDataContent(
            formData {
                append(
                    key = "file",
                    filename = filename,
                    contentType = contentType,
                    size = size,
                    bodyBuilder = bodyBuilder,
                )
            },
        )
        val request = httpClient.safeHttpRequest(endpoint.string) {
            method = Post
            setBody(requestBody)
            if (onUpload != null) {
                onUpload(onUpload)
            }
        }
        val response = when (request) {
            is IOError -> return UploadFileResult.IOError(request.cause)
            is ServerError -> return UploadFileResult.ServerError
            is Success -> request.response
        }
        val responseBody = when (response.status) {
            OK -> response.body<UploadFileResponseBody>()
            else -> error("Unknown status code")
        }
        val descriptor = responseBody.typed()
        return UploadFileResult.Success(descriptor)
    }

    private fun UploadFileResponseBody.typed() =
        FileDescriptor(id.typed(), accessHash.typed())
}
