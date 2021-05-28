package org.techtown.myalbum

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import java.util.*
import kotlin.concurrent.timer

class PhotoFrame : AppCompatActivity() {
    private val photoList = mutableListOf<Uri>()

    private var currentPosition = 0

    private var timer:Timer? = null

    private val photoImg:ImageView by lazy {
        findViewById<ImageView>(R.id.photo)
    }

    private val backgroundImg:ImageView by lazy {
        findViewById<ImageView>(R.id.background)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_photo_frame)

        getPhotoUri()

    }

    private fun getPhotoUri(){
        val size = intent.getIntExtra("photoListSize", 0)
        for (i in 0..size){
            intent.getStringExtra("photo$i")?.let{
                photoList.add(Uri.parse(it))
            }
        }
    }

    private fun startTimer(){
        timer = timer(period = 5 * 1000) {
            runOnUiThread {

                val current = currentPosition
                val next = if (photoList.size <= currentPosition + 1) 0 else currentPosition + 1

                backgroundImg.setImageURI(photoList[current])

                photoImg.alpha = 0f
                photoImg.setImageURI(photoList[next])
                photoImg.animate()
                    .alpha(1.0f)
                    .setDuration(1000)
                    .start()

                currentPosition = next
            }

        }
    }

    override fun onStart() {
        super.onStart()

        startTimer()
    }

    override fun onStop() {
        super.onStop()

        timer?.cancel()
    }

    override fun onDestroy() {
        super.onDestroy()

        timer?.cancel()
    }
}