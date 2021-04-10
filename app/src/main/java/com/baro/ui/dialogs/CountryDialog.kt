package com.baro.ui.dialogs

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.baro.R
import com.baro.adapters.CountryAdapter
import com.baro.helpers.IconSelector
import com.baro.models.Country
import java.lang.ref.WeakReference


class CountryDialog(var listener: CountrySelector) : AppCompatDialogFragment(), CountryAdapter.CountrySelectedListener{

    // UI
    private lateinit var languagesGrid: RecyclerView

    // Model
    private var countries: ArrayList<Country>? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        countries = IconSelector.loadCountries(WeakReference<Context>(context))
        val view = inflater.inflate(R.layout.dialog_language_choose, container, false)
        languagesGrid = view.findViewById(R.id.language_grid)
        val weakContext = WeakReference<Context>(context)

        languagesGrid.layoutManager = GridLayoutManager(activity, 3, GridLayoutManager.VERTICAL, false)

        var adapter = CountryAdapter(weakContext, countries!!, this)
        languagesGrid.adapter = adapter
        return view
    }


    override fun onCountrySelectedListener(view: View, position: Int) {
        listener.onCountrySelectedListener(countries?.get(position))
        dialog?.dismiss()

    }

    interface CountrySelector {
        fun onCountrySelectedListener(country: Country?)
    }


}