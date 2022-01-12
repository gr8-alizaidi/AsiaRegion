package com.alizaidi.regioninfo.db.models

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CName
    (
        var common:String?,
        var official:String?
            ):Parcelable