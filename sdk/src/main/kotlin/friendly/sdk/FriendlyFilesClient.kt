package friendly.sdk

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.content.ProgressListener
import io.ktor.client.plugins.onUpload
import io.ktor.client.request.forms.MultiPartFormDataContent
import io.ktor.client.request.forms.append
import io.ktor.client.request.forms.formData
import io.ktor.client.request.header
import io.ktor.client.request.post
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
    private data class UploadFileResponse(
        val id: FileIdSerializable,
        val accessHash: FileAccessHashSerializable,
    )

    public suspend fun upload(
        filename: String,
        contentType: ContentType? = null,
        size: Long? = null,
        onUpload: ProgressListener? = null,
        bodyBuilder: Sink.() -> Unit,
    ): FileDescriptor {
        val endpoint = endpoint / "upload"

        val response = httpClient.post(endpoint.string) {
            header("CF-Connecting-IP", "1.1.1.1")
            setBody(
                MultiPartFormDataContent(
                    formData {
                        append(
                            key = "file",
                            filename = filename,
                            contentType = contentType,
                            size = size,
                            bodyBuilder = bodyBuilder,
                        )
                    },
                ),
            )
            if (onUpload != null) {
                onUpload(onUpload)
            }
        }.body<UploadFileResponse>()
        return response.typed()
    }

    private fun UploadFileResponse.typed() =
        FileDescriptor(id.typed(), accessHash.typed())
}
