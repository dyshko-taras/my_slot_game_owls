package com.trafficx.owls.libgdx

import com.badlogic.gdx.backends.android.AndroidApplication
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration

class LauncherLibgdx : AndroidApplication() {
    override fun onCreate(savedInstanceState: android.os.Bundle?) {
        super.onCreate(savedInstanceState)
        var config = AndroidApplicationConfiguration()
        config.useAccelerometer = false;
        config.useCompass = false;
        initialize(MainLibgdx(this), config)
    }
}