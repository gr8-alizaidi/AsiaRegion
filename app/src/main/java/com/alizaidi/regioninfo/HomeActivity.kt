package com.alizaidi.regioninfo

import android.content.Intent
import android.os.Bundle
import android.os.Process
import android.util.Log
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alizaidi.regioninfo.adapter.CountryAdapter
import com.alizaidi.regioninfo.adapter.countryClicked
import com.alizaidi.regioninfo.databinding.ActivityHomeBinding
import com.alizaidi.regioninfo.db.models.Country
import com.alizaidi.regioninfo.viewmodel.HomeViewmodel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import okhttp3.Dispatcher

class HomeActivity:AppCompatActivity(),countryClicked {
    private lateinit var vm:HomeViewmodel
    private lateinit var binding: ActivityHomeBinding
    private lateinit var rv: RecyclerView
    private lateinit var CAdapter: CountryAdapter
    var item = ArrayList<Country>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_FULLSCREEN)
        binding= ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        vm= ViewModelProvider(this)[HomeViewmodel::class.java]

        binding.progressBar.isVisible = true
        binding.lifecycleOwner=this

        getData()
    }

    private fun getData() {
        if(vm.checkForInternet())
        {

            binding.netIssue.visibility=GONE
            vm.networkcall()
            vm.listN.observe(this,{
                if(it.size>0)
                {
                    binding.rvList.visibility=VISIBLE
                    binding.progressBar.visibility=GONE
                    binding.delbtn.visibility=GONE
                }
                CAdapter.updateItem(it)
            })
            }

        else
        {
            binding.delbtn.visibility=VISIBLE
            binding.rvList.visibility=VISIBLE
            binding.netIssue.visibility=VISIBLE
            binding.progressBar.visibility=GONE
            binding.delbtn.setOnClickListener(){
                vm.deleteRoomData()
                binding.rvList.visibility=GONE
            }
            item= vm.fetchdb() as ArrayList<Country>
        }
        setupRV(item)

    }

    private fun setupRV(item : ArrayList<Country>) {
        rv = findViewById(R.id.rvList)
        rv.setHasFixedSize(true)
        rv.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        CAdapter = CountryAdapter(this)
        CAdapter.items=item
        rv.adapter = CAdapter

    }

    override fun onItemClicked(item: Country) {
        Log.d("cr ha",item.toString())
        vm.insert(item)
        val i= Intent(this,CountryActivity::class.java)
        i.putExtra("Country",item)
        startActivity(i)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        moveTaskToBack(true)
        Process.killProcess(Process.myPid())
        System.exit(1)

    }
}