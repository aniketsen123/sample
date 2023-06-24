package com.example.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Adapter
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.ui.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
lateinit var binding:ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val list:ArrayList<model> = ArrayList<model>()
        list.add(model(R.drawable.convert,R.drawable.convert_night,"Pdf Convert"))

        binding.toolbar.inflateMenu(R.menu.menu2)
        val recyclerView=findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager=StaggeredGridLayoutManager(2,LinearLayout.VERTICAL)
        val adapter= com.example.ui.adapter.Adapter(this,list)
        recyclerView.adapter=adapter

    }
}