package EQMobileWork


import android.Manifest.permission.*
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.os.Build
import android.os.Looper
import android.util.Log
import androidx.annotation.RequiresApi
import com.google.android.gms.location.*
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory


/**
 * Libray init and log() func
 *
 * The fun log() will use the
 */
class Library {


    /**
     * Setup is the init call for the Library
     * this will set the url to POST the data to API
     * @param fullURL for API ENDPOINT
     */
    fun setup(fullURL: String): Boolean {

        apiURL = fullURL.plus("/") // passing the builder with "/" on the end
        retrofit = Retrofit.Builder().baseUrl(apiURL)
                .addConverterFactory(MoshiConverterFactory.create())
                .client(OkHttpClient().newBuilder().build())
                .build()
        retrofitService = retrofit.create(LocationPostService::class.java)
        apiURL =
                fullURL // reverting "/" for the retrofit issue for not allowing the base url to end with /
        return true
    }


    /**
     * Func to send @LocationEvent
     * sets the lat long from device automatically
     * @param Context
     */
    @RequiresApi(Build.VERSION_CODES.M)
    fun log(ctx: Context) {

        //Log back to server if the location services scopes are not available in application
        locationEvent = if (lat == 0.0f) {
            LocationEvent(
                    lat,
                    lon,
                    ext = "The application Does not have valid location permissions."
            )
        } else {
            LocationEvent(lat, lon)
        }

        // uses the Fine Location only if both are allowed
        // FINE LOCATION is more accurate as it gives the gps based location data
        if (
                ctx.checkSelfPermission(ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
        ) {
            getLatLong(ctx)
            return // Breaks and uses the ACCESS_FINE_LOCATION only
        } else if (
                ctx.checkSelfPermission(ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
        ) {
            getLatLong(ctx)
        }

    }

    private fun postLocationRetrofit(locationEvent: LocationEvent) {
        // retrofit handles the background thread execution for network calls

        Log.i("Location:", " Posting Location data -> Lat:$lat & Lon:$lon")
        retrofitService.postLocation(apiURL, locationEvent).enqueue(
                object : Callback<LocationEvent> {

                    override fun onResponse(
                            call: Call<LocationEvent>,
                            response: Response<LocationEvent>
                    ) {
                        Log.d(
                                "Location POST success: ", response.isSuccessful.toString() +
                                " HTTP CODE: " +
                                response.code().toString()
                        )
                    }

                    override fun onFailure(call: Call<LocationEvent>, t: Throwable) {
                        Log.e(
                                "Location POST Denied:",
                                "Reason -> No internet connection Found" + t.stackTraceToString()
                        )
                    }
                }
        )
    }


    // With Location Event if User wants to pass data explicitly
    fun log(locationEvent: LocationEvent) {
        postLocationRetrofit(locationEvent)
    }


    @SuppressLint("MissingPermission")
    @RequiresApi(Build.VERSION_CODES.M)
    private fun getLatLong(context: Context) {
        Log.i(context.applicationInfo.name, "Context passed to the Location library")

        val fusedLocationProviderClient =
                LocationServices.getFusedLocationProviderClient(context)

        //get last known location if possible
        fusedLocationProviderClient.lastLocation
                .addOnSuccessListener { location: Location? ->
                    // Got last known location. In some rare situations this can be null.
                    lat = location?.latitude?.toFloat() ?: 0.0F
                    lon = location?.longitude?.toFloat() ?: 0.0F
                }

        // if last know location is not available call to Google gms api to get updates on location data
        //Location callback from Google gms service that gives location data on requested
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                locationResult ?: return
                for (location in locationResult.locations) {
                    lat = location.latitude.toFloat()
                    lon = location.longitude.toFloat()
                    if (lat != 0.0f) {
                        fusedLocationProviderClient.removeLocationUpdates(locationCallback)
                    }
                    Log.i("Location:", " Updates received -> Lat:$lat & Lon:$lon")
                    postLocationRetrofit(locationEvent ?: LocationEvent())
                }
            }
        }

        // Location request with fastest interval and high priority
        val locationRequest = LocationRequest.create().apply {
            interval = 0
            fastestInterval = 0
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }


        // Start looper and call for request location updates
        fusedLocationProviderClient.requestLocationUpdates(
                locationRequest,
                locationCallback,
                Looper.getMainLooper()
        )


        //if last known location is recorded -> close the callback for new request location
        if (lat != 0.0f) { // shut of recieving updates if lat and lon is recorded
            fusedLocationProviderClient.removeLocationUpdates(locationCallback)
            Log.i(
                    "Location:",
                    " Receiving location updates stopped - system now has recorded last known location"
            )
            postLocationRetrofit(locationEvent)
        }

        Log.d("latLong", "${lat.toString()} ${lon.toString()}")

    }

    @RequiresApi(Build.VERSION_CODES.DONUT)
    @SuppressLint("MissingPermission")
    private fun getFusedLocation(fusedLocationProviderClient: FusedLocationProviderClient) {


    }


    //Tests a new retrofit call for the status code
    fun getStatus(): Int {
        return retrofitService.postLocation("https://httpbin.org/post", LocationEvent()).execute()
                .code()
    }

    companion object {
        private lateinit var locationCallback: LocationCallback
        var apiURL: String = ""
        private lateinit var retrofit: Retrofit
        private lateinit var retrofitService: LocationPostService
        private lateinit var locationEvent: LocationEvent
        var lat: Float = 0.0f
        var lon: Float = 0.0f
    }
}


