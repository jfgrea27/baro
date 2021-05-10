package com.baro.helpers.interfaces

import com.baro.helpers.AsyncHelpers

interface OnUserDataFound {
    fun onUserDataReturned(userData: AsyncHelpers.LoadUserData.LoadUserDataResponse?)
}