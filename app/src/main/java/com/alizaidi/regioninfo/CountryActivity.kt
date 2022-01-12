package com.alizaidi.regioninfo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.alizaidi.regioninfo.databinding.ActivityCountryBinding
import com.alizaidi.regioninfo.db.models.Country
import com.alizaidi.regioninfo.viewmodel.HomeViewmodel
import com.bumptech.glide.Glide
import android.graphics.Bitmap
import android.graphics.Canvas

import android.graphics.drawable.Drawable
import android.view.View


class CountryActivity:AppCompatActivity() {
    private lateinit var binding: ActivityCountryBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_FULLSCREEN)
        binding=DataBindingUtil.setContentView(this,R.layout.activity_country)
        val country = intent.getParcelableExtra<Country>("Country")
        binding.country=country
        Glide.with(this).load(country!!.flags["png"]).into(binding.CFlag)
        var borders="-"
        var lang=""
        var capitals=""
        if(country!!.borders!=null) {
            borders=""
            for (i in country!!.borders!!) {
                borders = "$i,$borders"
            }
            borders = borders.substring(0, borders.length - 1)
        }
        binding.bor.text=borders
        for(i in country!!.capital!!)
        {
            capitals="$i,$capitals"
        }

        capitals=capitals.substring(0,capitals.length-1)
        binding.cap.text=capitals
        for(i in country!!.languages!!.values!!)
        {
            lang="$i,$lang"
        }
        lang=lang.substring(0,lang.length-1)
        binding.lan.text=lang
    }

}