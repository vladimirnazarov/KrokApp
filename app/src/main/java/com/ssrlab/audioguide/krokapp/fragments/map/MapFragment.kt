package com.ssrlab.audioguide.krokapp.fragments.map

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.ssrlab.audioguide.krokapp.R
import com.ssrlab.audioguide.krokapp.databinding.FragmentMapBinding
import com.ssrlab.audioguide.krokapp.db.objects.PointObject
import com.ssrlab.audioguide.krokapp.fragments.BaseFragment
import com.ssrlab.audioguide.krokapp.vm.ExhibitViewModel
import kotlinx.coroutines.launch

class MapFragment: BaseFragment(), OnMapReadyCallback {

    private lateinit var binding: FragmentMapBinding
    private lateinit var map: GoogleMap
    private lateinit var points: List<PointObject>

    private val exhibitVM: ExhibitViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentMapBinding.inflate(layoutInflater)

        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(this)

        return binding.root
    }

    override fun onResume() {
        super.onResume()

        mainActivity.setToolbar(resources.getString(R.string.map), isBackButtonShown = true)
        mainActivity.controlNavigationUi(false)
    }

    override fun onStop() {
        super.onStop()

        mainActivity.controlNavigationUi(true)
        exhibitVM.setCurrentObject(null)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap

        initMap()
    }

    @SuppressLint("SuspiciousIndentation")
    private fun initMap(){
        if (ActivityCompat.checkSelfPermission(requireContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != 1) {
            ActivityCompat.requestPermissions(mainActivity, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), 1)
        } else {
            map.isMyLocationEnabled = true
        }

        if (exhibitVM.getCurrentObject() != null) {
            val pos: CameraPosition = map.cameraPosition
            val newPos = CameraPosition.Builder(pos).target(LatLng(exhibitVM.getCurrentObject()!!.coordinates["lat"]?.toDouble()!!, exhibitVM.getCurrentObject()!!.coordinates["lng"]?.toDouble()!!)).zoom(18f).tilt(0f).build()
            map.animateCamera(CameraUpdateFactory.newCameraPosition(newPos))
        } else {
            val pos: CameraPosition = map.cameraPosition
            val newPos = CameraPosition.Builder(pos).target(LatLng(53.893009, 27.567444)).zoom(7f).tilt(0f).build()
            map.animateCamera(CameraUpdateFactory.newCameraPosition(newPos))
        }

        scope.launch {
            points = mainActivity.pointDao().getPoints()

            for (i in points) {
                if (i.coordinates["lat"] != "null" && i.coordinates["lng"] != "null")
                mainActivity.runOnUiThread { map.addMarker(MarkerOptions().position(LatLng(i.coordinates["lat"]!!.toDouble(), i.coordinates["lng"]!!.toDouble())).icon(bitmapDescriptorFromVector()).title(i.id.toString())) }
            }
        }

        map.setOnMarkerClickListener { marker ->
            points.find { it.id == marker.title?.toInt()}?.let { MapBottomSheet(it).show(parentFragmentManager, marker.title) }
            true
        }
    }

    private fun bitmapDescriptorFromVector(): BitmapDescriptor {
        val vectorDrawable = ContextCompat.getDrawable(mainActivity, R.drawable.ic_map_point)
        vectorDrawable!!.setBounds(0, 0, vectorDrawable.intrinsicWidth, vectorDrawable.intrinsicHeight)

        val bitmap = Bitmap.createBitmap(vectorDrawable.intrinsicWidth, vectorDrawable.intrinsicHeight, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        vectorDrawable.draw(canvas)
        return BitmapDescriptorFactory.fromBitmap(bitmap)
    }
}