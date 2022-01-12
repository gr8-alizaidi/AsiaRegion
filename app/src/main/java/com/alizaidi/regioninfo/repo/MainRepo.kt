package com.alizaidi.regioninfo.repo

import android.util.Log
import androidx.annotation.WorkerThread
import com.alizaidi.regioninfo.db.CountryDao
import com.alizaidi.regioninfo.db.models.Country
import com.alizaidi.regioninfo.db.models.CountryRoom
import com.alizaidi.regioninfo.retrofit.instance
import retrofit2.HttpException
import java.io.IOException


class MainRepo (private val Dao: CountryDao)
{
    var status=0
    var countryN = ArrayList<Country>()
    val allCountry: List<CountryRoom>
        get() = Dao.getAllCountry()

    @WorkerThread
    suspend fun insert(cn: CountryRoom) {
        Dao.insertCountry(cn)
    }

    @WorkerThread
    suspend fun delete() {
        Dao.delete()
    }

    suspend fun callNetwork()
    {

        val response = try {
                instance.api.getTodos()
            } catch (e: IOException) {
                Log.e("TAG", "IOException, you might not have internet connection $e")
                status = -1;
                return
            } catch (e: HttpException) {
                status = 0;
                Log.e("TAG", "HttpException, unexpected response")
                return
            }
            if (response.isSuccessful && response.body() != null) {
                status = 1;
                countryN=response.body() as ArrayList<Country>

                Log.d("data repo",countryN.toString())
            } else {
                status = 0;
            }
        }


}
