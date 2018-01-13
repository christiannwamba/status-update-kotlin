package com.example.android.statusupdate


import android.app.Activity
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import com.pusher.client.Pusher
import com.pusher.client.PusherOptions
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // setup recycler view and adapter
        val adapter = StatusAdapter()
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        // setup pusher to receive status update
        val options = PusherOptions()
        options.setCluster("eu")
        val pusher = Pusher(PUSHER_API_KEY, options)
        val channel = pusher.subscribe("my-channel")
        channel.bind("my-event") { channelName, eventName, data ->
            val jsonObject = JSONObject(data)
            runOnUiThread { adapter.addMessage(jsonObject.getString("message")) }
        }
        pusher.connect()

        // post status to server
        buttonPost.setOnClickListener {

            if (newStatus.text.isNotEmpty())
                RetrofitClient().getClient().updateStatus(newStatus.text.toString()).enqueue(object : Callback<String> {
                    override fun onResponse(call: Call<String>?, response: Response<String>?) {
                        newStatus.text.clear()
                        hideKeyboard()
                    }

                    override fun onFailure(call: Call<String>?, t: Throwable?) {
                        Toast.makeText(this@MainActivity,"Error occurred",Toast.LENGTH_SHORT).show()
                    }
                })
        }

    }

    private fun hideKeyboard() {
        val imm = this.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        var view = this.currentFocus
        if (view == null)
            view = View(this)
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

}