package com.example.android.myfavoriteplaces

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.example.android.myfavoriteplaces.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.textView
        val listView = binding.listView

        var places = arrayListOf<String>()
        places.add("Add a favorite place")
        val listAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, places)
        listView.adapter= listAdapter

        listView.setOnItemClickListener { parent, view, position, id ->
            val intent = Intent(applicationContext, MapsActivity::class.java)
            intent.putExtra("placeId", position)

        }
    }


}