package com.yoavuli.imageloaderapp

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.yoavuli.imageloaderapp.adapters.ImageItemAdapter
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private val viewModel: MainScreenViewModel by viewModels()
    private val imageItemAdapter = ImageItemAdapter()
    private val recyclerview : RecyclerView by lazy { findViewById(R.id.imagesRecyclerView) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        recyclerview.apply {
            layoutManager = LinearLayoutManager(context).apply {
                isSmoothScrollbarEnabled = true
            }

            adapter = imageItemAdapter
        }
        lifecycleScope.launch {
            viewModel.images.collectLatest { images ->
                imageItemAdapter.updateList(images)

            }
        }
        findViewById<TextView>(R.id.invalidateButton)?.setOnClickListener {
            viewModel.invalidateCache()
            imageItemAdapter.notifyDataSetChanged()
        }
        viewModel.loadImages()


    }
}