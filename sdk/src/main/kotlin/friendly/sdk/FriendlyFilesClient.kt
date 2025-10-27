package friendly.sdk

import io.ktor.client.HttpClient

public class FriendlyFilesClient(
    private val endpoint: FriendlyEndpoint,
    private val httpClient: HttpClient,
) {
    public fun getFileEndpoint(descriptor: FileDescriptor): FriendlyEndpoint =
        endpoint /
            "files" /
            "${descriptor.id.long}" /
            descriptor.accessHash.string
}
