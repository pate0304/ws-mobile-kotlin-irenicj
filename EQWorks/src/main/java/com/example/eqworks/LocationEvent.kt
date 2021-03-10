package EQMobileWork

import androidx.annotation.Keep
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Data class for Log call to submit the class object values to REST and
 * @author irenicj.ml
 * @param lat : Float - Latitude
 * @param lon  : Float - Longitude
 * @param time : Long - Timestamp for when Location Lat-lon was recorded
 *              -  If not passed explicitly this will be automatically
 *               recorded with system's current time
 *
 * @param ext : String - Extra field for logging Error on server side
 */
@Keep
@Serializable
data class LocationEvent(
    @SerialName("lat") val lat: Float? = 0.0f,
    @SerialName("lon") val lon: Float? = 0.0f,
    @SerialName("time") val time: Long? = System.currentTimeMillis(),
    @SerialName("ext") val ext: String? = "No args passed in Log() call in library."
)