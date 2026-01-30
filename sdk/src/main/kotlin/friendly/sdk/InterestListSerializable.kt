package friendly.sdk

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationException

@JvmInline
@Serializable
public value class InterestListSerializable(
    public val raw: List<InterestSerializable>,
) {
    init {
        if (raw.toSet().size != raw.size) {
            throw SerializationException("A list of interests must be unique")
        }
    }

    public fun typed(): InterestList = InterestList.orThrow(
        raw = raw.map(InterestSerializable::typed),
    )
}
