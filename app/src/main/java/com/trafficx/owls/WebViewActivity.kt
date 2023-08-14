package com.trafficx.owls

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.webkit.CookieManager
import android.webkit.WebSettings
import android.webkit.WebView

class WebViewActivity : AppCompatActivity() {
    private lateinit var webView: WebView
    private val URL = "https://pin-up.ua/ru"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web_view)

        hideStatusBar()

        initViews()
        webView.settings.apply {
            javaScriptEnabled = true
            domStorageEnabled = true
            useWideViewPort = true
            allowFileAccess = true
            allowContentAccess = true
            displayZoomControls = false
            builtInZoomControls = true
            loadsImagesAutomatically = true
            allowUniversalAccessFromFileURLs = true
            allowFileAccessFromFileURLs = true
            cacheMode = WebSettings.LOAD_NO_CACHE
            mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
            databaseEnabled = true
        }

        webView.loadUrl(URL)

        val cookieManager = CookieManager.getInstance()
        cookieManager.setAcceptThirdPartyCookies(webView, true)
    }

    private fun initViews() {
        webView = findViewById(R.id.webView)
    }

    override fun onBackPressed() {
        if (webView.canGoBack()) webView.goBack()
        else finishAffinity()
    }

    private fun hideStatusBar() {
        // Hide the status bar.
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_FULLSCREEN
                or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)

        // Hide the action bar.
        actionBar?.hide()
    }
}