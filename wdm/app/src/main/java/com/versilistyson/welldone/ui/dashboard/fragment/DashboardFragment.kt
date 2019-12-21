package com.versilistyson.welldone.ui.dashboard.fragment

import android.app.Application
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.versilistyson.welldone.R
import com.versilistyson.welldone.adapter.SensorStatusListAdapter
import com.versilistyson.welldone.data.remote.dto.SensorRecentResponse
import com.versilistyson.welldone.ui.dashboard.DashboardViewmodel
import com.versilistyson.welldone.util.MAPVIEW_BUNDLE_KEY
import kotlinx.android.synthetic.main.fragment_dashboard.*

/*
    Dashboard map for WellDone operator where they can view the pumps on a map.
    Landscape and portrait mode. In landscape mode, map is bigger and pump status
    displays in a little box on the map when you click on a marker on the map. In
    portrait mode it displays in a little recycler view underneath, shows previous marker
    clicks as well.
 */
class DashboardFragment : Fragment(), OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private lateinit var viewmodel: DashboardViewmodel
    private lateinit var mMap: GoogleMap
    private lateinit var mapView: MapView
    private lateinit var sensorStatusListAdapter: SensorStatusListAdapter
    private lateinit var sensorStatusRecyclerView: RecyclerView

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewmodel = activity.let {
            val appContext = activity?.applicationContext as Application
            ViewModelProvider
                .AndroidViewModelFactory
                .getInstance(appContext)
                .create(DashboardViewmodel::class.java)
        }

        viewmodel.sensorLiveData.value?.body().let {
            sensorStatusListAdapter = if(it != null) {
                SensorStatusListAdapter(it)
            } else {
                SensorStatusListAdapter(emptyList())
            }
            initRecyclerView()
        }

        viewmodel.sensorStatusLiveData.observe(viewLifecycleOwner, Observer {
            sensorStatusListAdapter.notifyDataSetChanged()
        })
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_dashboard, container, false)
        mapView = view.findViewById(R.id.map_view)
        initGoogleMap(savedInstanceState)
        return view
    }

    private fun initGoogleMap(savedInstanceState: Bundle?) {
        if(savedInstanceState != null){
            mapView.onCreate(savedInstanceState.getBundle(MAPVIEW_BUNDLE_KEY))
        } else {
            mapView.onCreate(savedInstanceState)
        }
        mapView.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        val sydney = LatLng(-34.0, -151.0)
        mMap.addMarker(MarkerOptions().position(sydney).title("Marker Title").snippet("Marker Description"))
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 15f))
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        var mapViewBundle = outState.getBundle(MAPVIEW_BUNDLE_KEY)
        if (mapViewBundle == null) {
            mapViewBundle = Bundle()
            outState.putBundle(MAPVIEW_BUNDLE_KEY, mapViewBundle)
        }
        mapView.onSaveInstanceState(mapViewBundle)
    }

    private fun initRecyclerView() {
        sensorStatusRecyclerView = RecyclerView(activity!!.applicationContext).apply{
            adapter = sensorStatusListAdapter
            layoutManager = LinearLayoutManager(activity!!.applicationContext, LinearLayoutManager.VERTICAL, false)
        }
    }

    override fun onMarkerClick(marker: Marker?): Boolean {
        marker?.let{
            viewmodel.addSensorStatus(marker.tag as SensorRecentResponse)
            return true
        }
        return false
    }

    override fun onStart() {
        super.onStart()
        map_view.onStart()
    }

    override fun onResume() {
        super.onResume()
        map_view.onResume()
    }

    override fun onStop() {
        super.onStop()
        map_view.onStop()
    }

    override fun onPause() {
        super.onPause()
        map_view.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        map_view.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        map_view.onLowMemory()
    }
}
