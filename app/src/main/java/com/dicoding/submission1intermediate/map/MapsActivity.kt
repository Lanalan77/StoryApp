package com.dicoding.submission1intermediate.map

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.dicoding.submission1intermediate.R
import com.dicoding.submission1intermediate.data.response.ListStoryItem
import com.dicoding.submission1intermediate.databinding.ActivityMapsBinding
import com.dicoding.submission1intermediate.ui.story.StoryDataClass
import com.dicoding.submission1intermediate.ui.story.StoryViewModel
import com.dicoding.submission1intermediate.ui.story.ViewModelFactory
import com.dicoding.submission1intermediate.ui.welcome.WelcomeActivity
import com.dicoding.submission1intermediate.util.UserPreference

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private lateinit var storyViewModel: StoryViewModel
    private lateinit var userPreference: UserPreference
    private lateinit var token: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.title = "Map"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        userPreference = UserPreference(this)

        val data = userPreference.getSession()
        token = "Bearer ${data.apiToken.toString()}"

        if (data.isLogin == false){
            startActivity(Intent(this, WelcomeActivity::class.java))
        }

        storyViewModel = ViewModelProvider(this, ViewModelFactory(this, token)).get(StoryViewModel::class.java)


        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    private val listStory = ArrayList<ListStoryItem>()
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.uiSettings.isIndoorLevelPickerEnabled = true
        mMap.uiSettings.isCompassEnabled = true
        mMap.uiSettings.isMapToolbarEnabled = true

        storyViewModel.getAllStory(token)


        storyViewModel.listStory.observe(this){
            storyMap(it)
        }


        lifecycleScope.launch {
            delay(2000)
            addMarker(listStory)
        }

    }

    private fun storyMap(listStoryItems: List<ListStoryItem>) {
        for(i in listStoryItems){
            listStory.add(i)
        }
    }

    private val boundsBuilder = LatLngBounds.Builder()

    private fun addMarker(listData: ArrayList<ListStoryItem>) {


        listData.forEach { data ->
            val latLng = LatLng(data.lat as Double, data.lon as Double)
            mMap.addMarker(MarkerOptions().position(latLng).title(data.name))
            boundsBuilder.include(latLng)

        }

        val bounds: LatLngBounds = boundsBuilder.build()
        mMap.animateCamera(
            CameraUpdateFactory.newLatLngBounds(
                bounds,
                resources.displayMetrics.widthPixels,
                resources.displayMetrics.widthPixels,
                300
            )
        )

    }



}