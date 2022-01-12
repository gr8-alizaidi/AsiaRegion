package com.alizaidi.regioninfo.db.models
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Country(
    var name: CName,
    var flags: HashMap<String,String>,
    var capital:List<String>?,
    var region:String?,
    var subregion:String?,
    var borders:List<String>?,
    var population:Int,
    var languages:HashMap<String,String>?

    ): Parcelable