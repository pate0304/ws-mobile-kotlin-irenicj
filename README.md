# Work Sample for Mobile Aspect, Kotlin Variant

## This is a library built on top of retrofit to send location data to given url endpoint. 

# Download 
To get a sdk into your build:

Step 1. Add the JitPack repository to your build file
Add it in your root build.gradle at the end of repositories:

	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
  
Step 2. Add the dependency

	dependencies {
	        implementation 'com.github.pate0304:ws-mobile-kotlin-irenicj:0.1'
	}
[![](https://jitpack.io/v/pate0304/ws-mobile-kotlin-irenicj.svg)](https://jitpack.io/#pate0304/ws-mobile-kotlin-irenicj)



## Initilizing and usage

``` kotlin
            
             /**
             * Library initialization
             * Requires Minimum android version 23
             * The app manifest should have allowed ACCESS_FINE_LOCATION, INTERNET permissions
             * *Library does not prompt for turning on location permission for the application
             * The application must have the location enabled for all time or while using app from system's location settings.
             */
             
            val library = Library()

            /**
             * Library Setup
             * @param url : REST API endpoint for POST calls
             *  to submit @LocationEvent object (Location Data)
             *
             */
            library.setup("https://httpbin.org/post")

            /**
             * Call to library for fetching the device location data and
             *  Posting to defined api endpoint
             */
            library.log(this)
 ```
 
 - LocationEvent is a DataClass which can be dynamically initialized and used from the library. 
 - log function can explicitly set time to the server. 
 - If log is called with no params it will set the lat, long to (0,0) 
 - Field name "ext" : String can be used for silent error logging on the server


## To test the library 

-import this project to Android studio or IntelliJ IDEA 
-Build and install this app module on emulator or physical device. 
-Fab button on bottom corner mimics the library log call. 
-Testclass TestLogRequest has jUnit test function in library EQWorks package
[What is this for?](https://github.com/EQWorks/work-samples#what-is-this)

