package com.alizaidi.regioninfo.db.models

import android.graphics.Bitmap
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.alizaidi.regioninfo.db.Converters

@Entity(tableName = "country_table")
data class CountryRoom(
    @PrimaryKey
    var name:String="",
    var oname:String="",
    var capital:String="",
    var region:String="",
    var sregion:String="",
    var borders:String="",
    var lang:String="",
    var pop:Int=0,
    var furl:String="",
    @TypeConverters(Converters::class)
    var flag: Bitmap?=null
) {
}