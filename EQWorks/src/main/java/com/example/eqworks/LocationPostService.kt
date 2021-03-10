package EQMobileWork

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Url


/**
 * Retrofit interface service class
 */
interface LocationPostService {

    @Headers("Content-Type: application/json")
    @POST
    fun postLocation(@Url fullUrl: String, @Body locationEvent: LocationEvent): Call<LocationEvent>

}