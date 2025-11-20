package friendly.sdk.examples

import io.ktor.http.ContentType
import kotlinx.io.buffered
import kotlinx.io.files.Path
import kotlinx.io.files.SystemFileSystem

// This example only works in production
suspend fun filesExample() {
    val fs = SystemFileSystem
    val path = fs.resolve(Path("assets/image.webp"))
    val metadata = fs.metadataOrNull(path) ?: error("Can't access assets")

    val fileDescriptor = client.files.upload(
        filename = "image.webp",
        contentType = ContentType.Image.WEBP,
        size = metadata.size,
        onUpload = { sent, total ->
            val percent = "%.0f".format(
                sent.toDouble() / metadata.size * 100,
            )
            println("Transferring: $sent of $total ($percent%)")
        },
    ) {
        fs.source(path)
            .buffered()
            .transferTo(this)
    }.orThrow()

    val url = client.files.getEndpoint(fileDescriptor)
    println("File uploaded to: ${url.string}")
}
