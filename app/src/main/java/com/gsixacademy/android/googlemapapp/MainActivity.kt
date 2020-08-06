package com.gsixacademy.android.googlemapapp

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions


class MainActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var map: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val mapFragment =
            supportFragmentManager.findFragmentById(R.id.fragment_map) as SupportMapFragment

        mapFragment.getMapAsync(this)
        getLocationPermission()
    }

    fun getLocationPermission(): Boolean {

        val permissions = ArrayList<String>()

        if (!isPermissionGranted(android.Manifest.permission.ACCESS_FINE_LOCATION)) {
            permissions.add(android.Manifest.permission.ACCESS_FINE_LOCATION)
        }
        if (permissions.isNotEmpty()) {
            ActivityCompat.requestPermissions(this, permissions.toTypedArray(), 2002)
            return false
        } else
            return true


    }

    private fun isPermissionGranted(permission: String): Boolean {
        return this.checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if(requestCode==2002){
            if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            updateLocationUI()
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun updateLocationUI() {
        if(isPermissionGranted(Manifest.permission.ACCESS_FINE_LOCATION)){
            map.isMyLocationEnabled = true
            map.uiSettings.isMyLocationButtonEnabled = true
        }else{
            map.isMyLocationEnabled = false
            map.uiSettings.isMyLocationButtonEnabled = false
            getLocationPermission()
        }

    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap

        map.setOnMapLoadedCallback {
            val gsixLocation = LatLng(42.011886, 21.372291)
            val gtcLocation = LatLng(41.995422, 21.434676)
            val dolnoSonjeLocation = LatLng(41.940128, 21.378703)


            val builder = LatLngBounds.Builder()

            builder.include(gsixLocation)
            builder.include(gtcLocation)
            builder.include(dolnoSonjeLocation)
            val bounds = builder.build()


            map.addMarker(MarkerOptions().position(gsixLocation).title("GSIX"))
            map.addMarker(MarkerOptions().position(gtcLocation).title("GTC"))
            map.addMarker(MarkerOptions().position(dolnoSonjeLocation).title("Dolno Sonje"))
//          map.animateCamera(CameraUpdateFactory.newLatLngZoom(gsixLocation,15f))

            map.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 90))
            updateLocationUI()
        }
    }
}