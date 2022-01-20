package com.example.hotspot20.views

import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.hotspot20.R
import com.example.hotspot20.adapters.Hotspot
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.firestore.FirebaseFirestore

class HotspotFragment : Fragment() {

    val TAG = "HotspotFragment"
    private lateinit var mMap: GoogleMap

    //private var mapReady = false
    var db = FirebaseFirestore.getInstance()
    var arHotspots = arrayListOf<Hotspot>()
    private val LOCATION_PERMISSION_REQUEST = 1

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
            getLocationAccess()
            //mapReady = true
            addMarkers()
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

}
