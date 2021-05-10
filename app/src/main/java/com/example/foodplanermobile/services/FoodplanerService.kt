package com.example.foodplanermobile.services

import android.app.Application
import io.socket.client.IO
import io.socket.client.Socket
import java.net.URISyntaxException

private const val URL = "http://10.0.2.2:3000"

class FoodplanerService : Application() {
    private var mSocket: Socket? = null

    override fun onCreate() {
        super.onCreate()
        try {
            mSocket = IO.socket(URL)
        } catch (e: URISyntaxException) {
            throw RuntimeException(e)
        }
    }

    fun getMSocket(): Socket? {
        return mSocket
    }
}