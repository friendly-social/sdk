package friendly.sdk

public data class FriendlyEndpoint(val string: String) {
    public operator fun div(endpoint: FriendlyEndpoint): FriendlyEndpoint =
        div(endpoint.string)

    public operator fun div(string: String): FriendlyEndpoint {
        val base = this
        val result = buildString {
            append(base.string.removeSuffix("/"))
            append("/")
            append(string.removePrefix("/"))
        }
        return FriendlyEndpoint(result)
    }

    public companion object {
        public fun localhost(port: Int = 8080): FriendlyEndpoint =
            FriendlyEndpoint("http://localhost:$port")

        @Suppress("ktlint:standard:max-line-length")
        @Deprecated(
            message = "This server is localed in Russia and is not used anymore",
            replaceWith = ReplaceWith("meetacySenko"),
        )
        public fun meetacy(): FriendlyEndpoint =
            FriendlyEndpoint("https://meetacy.app/friendly")

        public fun meetacySenko(): FriendlyEndpoint =
            FriendlyEndpoint("https://friendly.meetacy.app/")
    }
}
