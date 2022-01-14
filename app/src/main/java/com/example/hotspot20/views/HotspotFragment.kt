package com.example.hotspot20.views

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
            //mapReady = true
            addMarkers()
        }
        return view
    }

    private fun addMarkers(){
        db.collection("hotspots").addSnapshotListener { result, e ->
            if (e != null){
                Log.w(TAG,"listen failed", e)
                return@addSnapshotListener
            }
            mMap.clear()
            arHotspots.clear()
            arHotspots.addAll(result!!.toObjects(Hotspot::class.java))

            for (hotspot in arHotspots){
                val geoPosition = LatLng(hotspot.latitude, hotspot.longitude)
                mMap.addMarker(MarkerOptions().position(geoPosition).title(hotspot.name))
            }
        }
    }
}