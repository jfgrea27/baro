package com.baro.helpers

import android.content.Context
import com.baro.R
import com.baro.models.Country
import org.json.JSONException

import org.json.JSONObject
import java.io.InputStream
import java.lang.ref.WeakReference
import java.util.*
import kotlin.collections.ArrayList


object IconSelector {

    fun getMipmapResId(context: Context?, drawableName: String): Int {
        if (context == null) {
            return -1
        }
        return context?.resources.getIdentifier(
                drawableName.toLowerCase(Locale.ENGLISH), "raw", context?.packageName)
    }

    fun loadCountries(weakContext: WeakReference<Context>): ArrayList<Country>? {
        val context = weakContext.get() ?: return null

        var resourceFlagJson = R.raw.json_flags

        val stream: InputStream = context.resources.openRawResource(resourceFlagJson)

        try {
            val jsonCountries =  JSONObject(convertStreamToString(stream))
            val countries: ArrayList<Country> = ArrayList()
            val iter = jsonCountries.keys()
            while (iter.hasNext()) {
                val key = iter.next()
                try {
                    val value = jsonCountries[key] as String
                    countries.add(Country(key))
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }
            return countries

        } catch (e: JSONException) {
            e.printStackTrace()
        }

        return null
    }

    private fun convertStreamToString(`is`: InputStream?): String? {
        val s = Scanner(`is`).useDelimiter("\\A")
        return if (s.hasNext()) s.next() else ""
    }
}