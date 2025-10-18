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

        public fun meetacy(): FriendlyEndpoint =
            FriendlyEndpoint("https://meetacy.app/friendly")
    }
}
