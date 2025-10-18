package friendly.sdk

public data class UserId(val long: Long) {
    public fun serializable(): UserIdSerializable = UserIdSerializable(long)
}
