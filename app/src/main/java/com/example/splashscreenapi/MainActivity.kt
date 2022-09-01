package com.example.splashscreenapi

import android.animation.ObjectAnimator
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.ViewTreeObserver
import android.view.animation.LinearInterpolator
import androidx.appcompat.app.AppCompatActivity
import androidx.core.animation.doOnEnd
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        /**
         * Keep the splash screen on-screen for longer periods
         */
        val content: View = findViewById(android.R.id.content)
        content.viewTreeObserver.addOnPreDrawListener(
            object: ViewTreeObserver.OnPreDrawListener {
                var drawMainActivity = false
                override fun onPreDraw(): Boolean {
                    if(drawMainActivity) {
                        content.viewTreeObserver.removeOnPreDrawListener(this)
                        return true
                    }
                    Handler(Looper.getMainLooper()).postDelayed({
                        drawMainActivity = true
                    }, 3000)
                    return false
                }
            }
        )

        /**
         * Customize the animation for dismissing the splash screen
         */
        splashScreen.setOnExitAnimationListener {splashScreenProvider ->
            val slideUp = ObjectAnimator.ofFloat(
                splashScreenProvider.view,
                View.TRANSLATION_Y,
                0f,
                -splashScreenProvider.view.height.toFloat()
            )
            slideUp.interpolator = LinearInterpolator()
            slideUp.duration = 200L
            slideUp.doOnEnd {
                splashScreenProvider.remove()
            }
            slideUp.start()
        }
    }
}