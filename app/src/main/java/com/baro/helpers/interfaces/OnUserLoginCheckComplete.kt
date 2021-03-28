package com.baro.helpers.interfaces

import com.baro.models.User

interface OnUserLoginCheckComplete  {
    fun onUserLoginCheckDone(result: User?)
}