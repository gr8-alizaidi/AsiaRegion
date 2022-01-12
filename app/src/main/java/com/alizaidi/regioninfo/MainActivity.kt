package com.alizaidi.regioninfo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_FULLSCREEN)
        setContentView(R.layout.activity_main)
        val th=object: Thread()
        {
            override fun run() {
                super.run()
                Thread.sleep(2000)
                val i = Intent(baseContext, HomeActivity::class.java)
                startActivity(i)
            }
        }
        th.start()
    }
}