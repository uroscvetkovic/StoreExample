package com.example.storeexample.util

import android.graphics.BitmapFactory
import android.util.LruCache
import android.widget.ImageView
import java.net.URL

object ImageLoader {

    private val cache = LruCache<String, android.graphics.Bitmap>(30)

    fun load(url: String?, into: ImageView) {
        if (url.isNullOrEmpty()) return

        into.tag = url

        cache.get(url)?.let {
            into.setImageBitmap(it)
            return
        }

        Thread {
            try {
                val bitmap = BitmapFactory.decodeStream(URL(url).openStream())
                if (bitmap != null) {
                    cache.put(url, bitmap)
                    into.post {
                        if (into.tag == url) into.setImageBitmap(bitmap)
                    }
                }
            } catch (_: Exception) {}
        }.start()
    }
}
