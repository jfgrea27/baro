package com.baro.helpers

import com.baro.models.User

interface OnUserCheckComplete  {
    fun onTaskDone(result: User?);
}