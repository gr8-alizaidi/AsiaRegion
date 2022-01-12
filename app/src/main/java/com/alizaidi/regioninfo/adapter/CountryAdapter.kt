package com.alizaidi.regioninfo.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.alizaidi.regioninfo.R
import com.alizaidi.regioninfo.db.models.Country
import com.bumptech.glide.Glide
import com.squareup.picasso.Picasso

class CountryAdapter(private val listener: countryClicked):  RecyclerView.Adapter<CountryViewHolder>() {
     var items: ArrayList<Country> = ArrayList()
    private lateinit var context: Context
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CountryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.country_listview, parent, false)
        context=parent.context
        val viewHolder = CountryViewHolder(view)
        view.setOnClickListener{
            listener.onItemClicked(items[viewHolder.adapterPosition])
        }
        return viewHolder
    }

    override fun onBindViewHolder(holder: CountryViewHolder, position: Int) {
        val ci=items!![position]
        holder.name.text = ci.name.common
        Glide.with(context).load(ci.flags["png"]).into(holder.flag)
    }



    override fun getItemCount(): Int {
        return items!!.size
    }
        fun updateItem(it:ArrayList<Country>){
            items=it
            notifyDataSetChanged()
        }
}

class CountryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    val name: TextView = itemView.findViewById(R.id.CName)
    val flag: ImageView = itemView.findViewById(R.id.CImage)
}
interface countryClicked {
    fun onItemClicked(item: Country)
}