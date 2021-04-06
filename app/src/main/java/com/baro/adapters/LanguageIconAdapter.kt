package com.baro.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import com.baro.R
import com.baro.models.Language


class LanguageIconAdapter internal constructor(context: Context?, var mLanguages: ArrayList<Language>) : RecyclerView.Adapter<LanguageIconAdapter.ViewHolder>() {
    private val mInflater: LayoutInflater = LayoutInflater.from(context)
    private var mClickListener: ItemClickListener? = null

    // inflates the cell layout from xml when needed
    @NonNull
    override fun onCreateViewHolder(@NonNull parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = mInflater.inflate(R.layout.language_cell, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(@NonNull holder: ViewHolder, position: Int) {
        holder.languageText.text = mLanguages[position].getLanguageText()
        holder.languageImageButton.setImageResource(mLanguages[position].getImageResourse())
    }

    // total number of cells
    override fun getItemCount(): Int {
        return mLanguages.size
    }

    // stores and recycles views as they are scrolled off screen
    inner class ViewHolder internal constructor(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        var languageText: TextView = itemView.findViewById(R.id.language_name)
        var languageImageButton: ImageButton = itemView.findViewById(R.id.btn_language)

        override fun onClick(view: View) {
            if (mClickListener != null) mClickListener!!.onItemClick(view, adapterPosition)
        }

        init {
            itemView.setOnClickListener(this)
        }
    }

    // convenience method for getting data at click position
    fun getItem(id: Int): Language {
        return mLanguages[id]
    }

    // allows clicks events to be caught
    fun setClickListener(itemClickListener: ItemClickListener?) {
        mClickListener = itemClickListener
    }

    // parent activity will implement this method to respond to click events
    interface ItemClickListener {
        fun onItemClick(view: View?, position: Int)
    }

}