package com.baro.adapters

import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.baro.R
import com.baro.helpers.IconSelector
import com.baro.models.Country
import java.lang.ref.WeakReference
import java.util.*
import kotlin.collections.ArrayList


class CountryAdapter internal constructor(var context: WeakReference<Context>, var mCountry: ArrayList<Country>, var listener: CountrySelectedListener) : RecyclerView.Adapter<CountryAdapter.ViewHolder>() {


    private val mInflater: LayoutInflater = LayoutInflater.from(context.get())

    @NonNull
    override fun onCreateViewHolder(@NonNull parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = mInflater.inflate(R.layout.language_cell, parent, false)
        return ViewHolder(view)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onBindViewHolder(@NonNull holder: ViewHolder, position: Int) {

        holder.languageText.text = mCountry[position].getCountryName(context)
        holder.languageImageButton.setImageResource(mCountry[position].getImageResource(context))

        holder.languageImageButton.setOnClickListener{
            listener.onCountrySelectedListener(it, position )
        }
    }

    override fun getItemCount(): Int {
        return mCountry.size
    }
    fun getItem(id: Int): Country {
        return mCountry[id]
    }


    inner class ViewHolder internal constructor(itemView: View) : RecyclerView.ViewHolder(itemView){
        var languageText: TextView = itemView.findViewById(R.id.language_name)
        var languageImageButton: ImageButton = itemView.findViewById(R.id.btn_language)
    }



    interface CountrySelectedListener {
        fun onCountrySelectedListener(view: View, position: Int)
    }

}