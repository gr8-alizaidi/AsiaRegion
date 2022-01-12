package com.alizaidi.regioninfo.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.alizaidi.regioninfo.db.models.Country
import com.alizaidi.regioninfo.db.models.CountryRoom

@Dao
interface CountryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCountry(cn: CountryRoom)

    @Query("DELETE FROM country_table")
    suspend fun delete()

    @Query("SELECT * FROM country_table")
    fun getAllCountry(): List<CountryRoom>

}