package com.baro.helpers.interfaces

import com.baro.helpers.AsyncHelpers

interface OnUriSavedComplete {
    fun onUriSaved(result: AsyncHelpers.SaveUriToPath.OnUriSaved?)

}