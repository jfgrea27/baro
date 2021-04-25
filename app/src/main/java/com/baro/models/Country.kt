package com.baro.models

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import com.baro.helpers.IconSelector
import java.lang.ref.WeakReference
import java.util.*

class Country(var isoCountryCode: String) {
    fun getIsoCode(): String? {
        return isoCountryCode
    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun getCountryName(context: WeakReference<Context>): String?{
        val deviceLanguage = context.get()?.resources?.configuration?.locales?.get(0).toString()
        return Locale(deviceLanguage, isoCountryCode).displayCountry
    }

    fun getImageResource(context: WeakReference<Context>): Int {
        val drawableName = isoCountryCode?.toLowerCase(Locale.ENGLISH) + "_flag"
        return IconSelector.getMipmapResId(context.get(), drawableName)
    }
}
