package com.example.android.myfavoriteplaces

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import com.example.android.myfavoriteplaces.databinding.ActivityMainBinding
import com.google.android.gms.maps.model.LatLng

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    companion object {
    var places = arrayListOf<String>()
    var selectedPlace = arrayListOf<LatLng>()
    var listAdapter: ArrayAdapter<*>? = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val viewRoot = binding.root
        setContentView(viewRoot)

        val listView = binding.listView

        places.add("Add new favorite place")
        selectedPlace.add(LatLng(0.0, 0.0))
        listAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, places)

        listView.adapter = listAdapter

        listView.setOnItemClickListener { parent, view, position, id ->
            val intent = Intent(applicationContext, MapsActivity::class.java)
            intent.putExtra("placeId", position)
            startActivity(intent)
        }
    }
}