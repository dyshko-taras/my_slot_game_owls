package com.trafficx.owls

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.trafficx.owls.libgdx.LauncherLibgdx

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        launchLibGDX()
    }

    fun launchLibGDX() {
        val intent = Intent(this, LauncherLibgdx::class.java)
        startActivity(intent)
    }
}