import kotlinx.serialization.Serializable

enum class Light { Red, RedYellow, Yellow, Green }

@Serializable
data class MessageFromSterownik(val mainLight : Light, val sideLight : Light)

@Serializable
data class MessageToSterownik(val isMainCar : Boolean, val isSideCar : Boolean)
