package com.example.android.myfavoriteplaces

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.widget.Toast
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

    private var userLocation: LatLng? = null
    private var locationManager: LocationManager? = null
    private lateinit var mMap: GoogleMap

    private fun centerMapOnLocation(location: Location, title: String) {
        userLocation = LatLng(location.latitude, location.longitude)
        mMap.clear()
        mMap.addMarker(MarkerOptions().position(userLocation!!).title(title))

        val cameraPosition = CameraPosition.Builder()
            .target(userLocation!!)
            .zoom(13f)
            .bearing(300f)
            .tilt(30f)
            .build()
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
    }

    private fun currentLocation() {
            if (ContextCompat.checkSelfPermission(
                    this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION
                )
                == PackageManager.PERMISSION_GRANTED) {
                val lastLocation = locationManager!!.getLastKnownLocation(LocationManager.GPS_PROVIDER)
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

            if (ContextCompat.checkSelfPermission(
                    this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION
                )
                != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                    1)
            } else currentLocation()
        } else {
        val savedLocation = Location(LocationManager.GPS_PROVIDER)
            savedLocation.latitude = MainActivity.selectedPlace[intent.getIntExtra("placeId", 0)].latitude
            savedLocation.longitude = MainActivity.selectedPlace[intent.getIntExtra("placeId", 0)].longitude

        centerMapOnLocation(
            savedLocation,
            MainActivity.places[intent.getIntExtra("placeId", 0)])
        }
    }

    @SuppressLint("SimpleDateFormat")
    override fun onMapLongClick(latLng: LatLng) {
        val geocoder = Geocoder(applicationContext, Locale.getDefault())
        var address = ""

        try {
            val addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1)
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
        if (address == "") {
            val dateFormat = SimpleDateFormat("HH:mm yyyy-MM-dd")
            address += dateFormat.format(Date())
        }

        mMap.addMarker(MarkerOptions().position(latLng).title(address))

        MainActivity.places.add(address)
        MainActivity.selectedPlace.add(latLng)
        MainActivity.listAdapter?.notifyDataSetChanged()
        Toast.makeText(applicationContext, "Location saved", Toast.LENGTH_SHORT).show()
    }

}
