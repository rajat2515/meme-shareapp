package com.example.memeapp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide

class MainActivity : AppCompatActivity() {
    private var currentImageUrl: String? = null // Declare currentImageUrl as a class property

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        loadMeme(this)
    }

    private fun loadMeme(context: Context) {
        val queue = Volley.newRequestQueue(context)
        val url = "https://meme-api.com/gimme"

        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET, url, null,
            { response ->
                Log.d("MemeApp", "Response: $response")
                if (response.has("url")) {
                    currentImageUrl = response.getString("url")
                    val memeImageView = findViewById<ImageView>(R.id.MemeImageView)
                    Glide.with(context).load(currentImageUrl).into(memeImageView) // Use currentImageUrl here
                    Log.d("MemeApp", "Loaded meme URL: $currentImageUrl")
                } else {
                    Toast.makeText(context, "No meme found", Toast.LENGTH_SHORT).show()
                    Log.e("MemeApp", "URL not found in response")
                }
            },
            { error ->
                Toast.makeText(context, "Error: ${error.message}", Toast.LENGTH_LONG).show()
                Log.e("MemeApp", "Error loading meme", error)
            }
        )

        queue.add(jsonObjectRequest)
    }


    fun shareMeme(view: View) {
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "text/plain"
        intent.putExtra(Intent.EXTRA_TEXT, "Hey, check out this cool meme I got from Reddit $currentImageUrl")
        val chooser = Intent.createChooser(intent, "Share this Meme using....")
        startActivity(chooser)
    }

    fun nextMeme(view: View) {
        loadMeme(this)
    }

    private fun enableEdgeToEdge() {
        // Add code to enable edge-to-edge layout
    }
}
