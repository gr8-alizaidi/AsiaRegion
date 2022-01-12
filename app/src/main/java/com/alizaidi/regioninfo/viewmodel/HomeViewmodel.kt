package com.alizaidi.regioninfo.viewmodel

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.util.Log
import androidx.lifecycle.*
import com.alizaidi.regioninfo.db.CountryDatabase
import com.alizaidi.regioninfo.db.models.CName
import com.alizaidi.regioninfo.db.models.Country
import com.alizaidi.regioninfo.db.models.CountryRoom
import com.alizaidi.regioninfo.repo.MainRepo
import com.bumptech.glide.Glide
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch


class HomeViewmodel(application: Application) : AndroidViewModel(application) {

    private val context = getApplication<Application>().applicationContext
    val db: CountryDatabase = CountryDatabase.getDatabase(context)
    val dao = db.CountryDao()
    var repo: MainRepo? = null
    var listN = MutableLiveData<ArrayList<Country>>()

    init {
        repo = MainRepo(dao)
    }

    fun networkcall() {
        viewModelScope.launch {
            repo!!.callNetwork()
            listN.value = repo!!.countryN
            Log.d("data VM", listN.toString())
        }
    }

    fun checkForInternet(): Boolean {

        // register activity with the connectivity manager service
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        // if the android version is equal to M
        // or greater we need to use the
        // NetworkCapabilities to check what type of
        // network has the internet connection
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            // Returns a Network object corresponding to
            // the currently active default data network.
            val network = connectivityManager.activeNetwork ?: return false

            // Representation of the capabilities of an active network.
            val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false

            return when {
                // Indicates this network uses a Wi-Fi transport,
                // or WiFi has network connectivity
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true

                // Indicates this network uses a Cellular transport. or
                // Cellular has network connectivity
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true

                // else return false
                else -> false
            }
        } else {
            // if the android version is below M
            @Suppress("DEPRECATION") val networkInfo =
                connectivityManager.activeNetworkInfo ?: return false
            @Suppress("DEPRECATION")
            return networkInfo.isConnected
        }
    }

    fun fetchdb(): List<Country> {
        val it = repo!!.allCountry
        return cnroomTocn(it)
    }

    private fun cnroomTocn(list: List<CountryRoom>): List<Country> {

        val clist = ArrayList<Country>()
        if (list != null) {
            for (i in list) {
                val lang = i.lang.split(",")
                val lMap = HashMap<String, String>()
                for (j in lang) {
                    lMap.put(j, j)
                }
                val fmap = HashMap<String, String>()
                fmap.put("png", i.furl)
                clist.add(
                    Country(
                        CName(i.name, i.oname), fmap, i.capital.split(","), i.region, i.sregion,
                        i.borders.split(","), i.pop, lMap
                    )
                )
            }
        }
        return clist
    }


    fun insert(cn: Country) {
        CoroutineScope(IO).launch {
            val list = cnTocnroom(cn)
            repo!!.insert(list)
        }
    }
    fun deleteRoomData(){
        CoroutineScope(IO).launch {
            repo!!.delete()
        }
    }

    private fun cnTocnroom(country: Country): CountryRoom {
        var borders = "-"
        var lang = ""
        var capitals = ""
        val name = country.name.common
        val oname = country.name.official
        val region = country.region
        val subregion = country.subregion

        val flag = Glide.with(context).asBitmap().load(country.flags["png"]).submit().get();
        if (country!!.borders != null) {
            borders = ""
            for (i in country!!.borders!!) {
                borders = "$i,$borders"
            }
            borders = borders.substring(0, borders.length - 1)
        }
        if (country.capital != null) {
            for (i in country!!.capital!!) {
                capitals = "$i,$capitals"
            }

            capitals = capitals.substring(0, capitals.length - 1)
        }
        for (i in country!!.languages!!.values!!) {
            lang = "$i,$lang"
        }
        lang = lang.substring(0, lang.length - 1)

        return (CountryRoom(
            name.toString(),
            oname.toString(),
            capitals,
            region.toString(),
            subregion.toString(),
            borders,
            lang,
            country.population,
            country.flags["png"]!!,
            flag
        ))
    }
}