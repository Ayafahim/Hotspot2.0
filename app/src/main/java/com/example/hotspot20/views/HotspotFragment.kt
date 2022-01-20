package com.example.hotspot20.views

import android.Manifest
import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.hotspot20.R
import com.example.hotspot20.model.Hotspot
import com.google.android.gms.location.*
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.firestore.FirebaseFirestore
import com.vmadalin.easypermissions.EasyPermissions
import com.vmadalin.easypermissions.dialogs.SettingsDialog

class HotspotFragment : Fragment(), EasyPermissions.PermissionCallbacks {

    val TAG = "HotspotFragment"
    private lateinit var mMap: GoogleMap
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private var userLat = 0.0
    private var userLong = 0.0
    private lateinit var userLoc: LatLng

    companion object {
        const val PERMISSION_LOCATION_REQUEST_CODE = 1
    }

    //private var mapReady = false
    var db = FirebaseFirestore.getInstance()
    var arHotspots = arrayListOf<Hotspot>()

    @SuppressLint("MissingPermission")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.fragment_hotspot, container, false)
        val mapFragment =
            childFragmentManager.findFragmentById(R.id.mapFragment) as SupportMapFragment
        mapFragment.getMapAsync { googleMap ->
            mMap = googleMap
            val uiSettings = mMap.uiSettings
            //uiSettings.isZoomControlsEnabled = true
            uiSettings.isCompassEnabled = true
            uiSettings.isMyLocationButtonEnabled = true
            mMap.setPadding(0, 50, 30, 300)
            addMarkers()
            fusedLocationProviderClient =
                LocationServices.getFusedLocationProviderClient(requireContext())
            /*if (hasLocationPermission()){
                fusedLocationProviderClient.lastLocation.addOnSuccessListener { location ->
                    userLat = location.latitude
                    userLong = location.longitude
                    userLoc = LatLng(location.latitude, location.longitude)
                    getLocationAccess()
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(userLoc, 13F))
                }
            } else {
                requestLocationPermission()
            }

             */
            /*
            if (hasLocationPermission()) {
                fusedLocationProviderClient.requestLocationUpdates(
                    locationRequest,
                    object : LocationCallback() {
                        override fun onLocationResult(locationResult: LocationResult) {
                            super.onLocationResult(locationResult)
                            for (location in locationResult.locations) {
                                getLocationAccess()
                                userLat = location.latitude
                                userLong = location.longitude
                                userLoc = LatLng(location.latitude, location.longitude)
                                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(userLoc, 13F))
                            }
                        }
                    },
                    Looper.getMainLooper()
                )
            } else {
                requestLocationPermission()
            }

             */
            zoomToCurrentLocation()
            //mapReady = true
        }
        return view
    }

    private fun addMarkers() {
        db.collection("hotspots").addSnapshotListener { result, e ->
            if (e != null) {
                Log.w(TAG, "listen failed", e)
                return@addSnapshotListener
            }
            mMap.clear()
            arHotspots.clear()
            arHotspots.addAll(result!!.toObjects(Hotspot::class.java))

            for (hotspot in arHotspots) {
                val geoPosition = LatLng(hotspot.latitude, hotspot.longitude)
                mMap.addMarker(MarkerOptions().position(geoPosition).title(hotspot.name))
            }
        }
    }

    /**
     * TODO: Fix log in screen skip
     */

    @SuppressLint("MissingPermission")
    private fun zoomToCurrentLocation() {
        val locationRequest = LocationRequest().setInterval(2000).setFastestInterval(2000)
            .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
        if (hasLocationPermission()) {
            fusedLocationProviderClient.requestLocationUpdates(
                locationRequest,
                object : LocationCallback() {
                    override fun onLocationResult(locationResult: LocationResult) {
                        super.onLocationResult(locationResult)
                        for (location in locationResult.locations) {
                            getLocationAccess()
                            userLat = location.latitude
                            userLong = location.longitude
                            userLoc = LatLng(location.latitude, location.longitude)
                            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(userLoc, 13F))
                        }
                    }
                },
                Looper.getMainLooper()
            )
        } else {
            requestLocationPermission()
        }
    }

    private fun hasLocationPermission() =
        EasyPermissions.hasPermissions(
            requireContext(),
            Manifest.permission.ACCESS_FINE_LOCATION
        )

    private fun requestLocationPermission() {
        EasyPermissions.requestPermissions(
            this,
            "This application cannot work without Location Permission.\n Please allow Location Permissions, and try again.",
            PERMISSION_LOCATION_REQUEST_CODE,
            Manifest.permission.ACCESS_FINE_LOCATION
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    override fun onPermissionsDenied(requestCode: Int, perms: List<String>) {
        if (EasyPermissions.somePermissionDenied(this, perms.first())) {
            SettingsDialog.Builder(requireActivity()).build().show()
        } else {
            requestLocationPermission()
        }
    }

    override fun onPermissionsGranted(requestCode: Int, perms: List<String>) {
        Toast.makeText(
            requireContext(),
            "Permission Granted",
            Toast.LENGTH_SHORT
        ).show()
    }


    @SuppressLint("MissingPermission")
    private fun getLocationAccess() {
        mMap.isMyLocationEnabled = true
    }


/*
    private fun getLocationAccess() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            mMap.isMyLocationEnabled = true
        } else
            requestPermissions(
                arrayOf
                    (android.Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_PERMISSION_REQUEST
            )
    }

    @SuppressLint("MissingPermission")
    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>, grantResults:
        IntArray
    ) {
        if (requestCode == LOCATION_PERMISSION_REQUEST) {
            if (grantResults.contains(PackageManager.PERMISSION_GRANTED)) {
                mMap.isMyLocationEnabled=true
            }
            else {
                Toast.makeText(
                    activity, "User has not granted location access permission",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

 */
}
