package com.example.android.myfavoriteplaces

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import java.text.SimpleDateFormat
import java.util.*


class MapsActivity : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnMapLongClickListener {

    private var locationManager: LocationManager? = null

    private lateinit var mMap: GoogleMap
    var userLocation: LatLng? = null

    private fun centerMapOnLocation(location: Location, title: String) {
        userLocation = LatLng(location.latitude, location.longitude)
        mMap.clear()
        mMap.addMarker(MarkerOptions().position(userLocation).title(title))

        val myPosition = CameraPosition.Builder()
            .target(userLocation).zoom(13f).bearing(300f).tilt(30f).build()
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(myPosition))
    }

    fun currentLocation() {
            if (ContextCompat.checkSelfPermission(this,android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
                var lastLocation = locationManager!!.getLastKnownLocation(LocationManager.GPS_PROVIDER)
                centerMapOnLocation(lastLocation!!, "Your location")
            }
        }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            currentLocation()
//            if (ContextCompat.checkSelfPermission(
//                    this,
//                    android.Manifest.permission.ACCESS_FINE_LOCATION
//                )
//                == PackageManager.PERMISSION_GRANTED) {
//                var lastLocation = locationManager!!.getLastKnownLocation(LocationManager.GPS_PROVIDER)
//                locationManager!!.requestLocationUpdates(
//                    LocationManager.GPS_PROVIDER,
//                    0,
//                    0f,
//                    locationListener!!
//                )
//                centerMapOnLocation(lastLocation!!, "Your location")
//            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        mMap.setOnMapLongClickListener(this)
        val intent = intent
        if (intent.getIntExtra("placeId", 0) == 0) {

            locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
            currentLocation()


//            locationListener = object : LocationListener {
//                override fun onLocationChanged(location: Location) {
//
//                }
//
//                @Suppress("DEPRECATION")
//                override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
//                    super.onStatusChanged(provider, status, extras)
//                }
//            }

            if (ContextCompat.checkSelfPermission(
                    this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION
                )
                == PackageManager.PERMISSION_GRANTED
            ) {
//                locationManager!!.requestLocationUpdates(
//                    LocationManager.GPS_PROVIDER,
//                    0,
//                    0f,
//                    locationListener!!
//                )
                var lastLocation = locationManager!!.getLastKnownLocation(LocationManager.GPS_PROVIDER)
                centerMapOnLocation(lastLocation!!, "Your location")
            } else {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                    1
                )
            }
        }
//        else {
//        val placeLocation = Location(LocationManager.GPS_PROVIDER)
//        placeLocation.latitude = MainActivity.locations.get(intent.getIntExtra("placeNumber", 0)).latitude
//        placeLocation.longitude =
//            MainActivity.locations.get(intent.getIntExtra("placeNumber", 0)).longitude
//
//        centerMapOnLocation(
//            placeLocation,
//            MainActivity.places.get(intent.getIntExtra("placeNumber", 0))
//        )
//    }
    }

    override fun onMapLongClick(latLng: LatLng) {
        val geocoder = Geocoder(applicationContext, Locale.getDefault())
        var address = ""

        try {
            var addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1)
            if (addresses.isNotEmpty()) {
                if (addresses[0].thoroughfare != null){
                    if (addresses[0].subThoroughfare != null) {
                        address += addresses[0].subThoroughfare + " "
                    }
                    address += addresses[0].thoroughfare
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        if (address.equals("")) {
            var dateFormat = SimpleDateFormat("HH:mm yyyy-MM-dd")
            address += dateFormat.format(Date())
        }

        mMap.addMarker(MarkerOptions().position(latLng).title(address))
    }

}
