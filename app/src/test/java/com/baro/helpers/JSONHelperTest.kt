package com.baro.helpers

import com.baro.constants.JSONEnum
import org.json.JSONException
import org.junit.Assert
import org.junit.Test
import java.util.*

class JSONHelperTest {
    @Test
    @Throws(JSONException::class)
    fun createJSONFromHashMapShouldReturnJSONObject() {
        val sampleHashMap = HashMap<String?, String?>()
        sampleHashMap[JSONEnum.COURSE_NAME_KEY.key] = "Course Name"
        sampleHashMap[JSONEnum.COURSE_UUID_KEY.key] = "Course UUID"
        val jsonTest = JSONHelper.createJSONFromHashMap(sampleHashMap)
        Assert.assertEquals(jsonTest!!.length().toLong(), 2)
        Assert.assertEquals(jsonTest!![JSONEnum.COURSE_NAME_KEY.key], "Course Name")
        Assert.assertEquals(jsonTest!![JSONEnum.COURSE_UUID_KEY.key], "Course UUID")
    }

    @Test
    @Throws(JSONException::class)
    fun createJSONFromStringShouldReturnJSONObjectWhenValidString() {
        val validJsonString = "{\"key1\": \"value\", \"key2\": 1}"
        val jsonTest = JSONHelper.createJSONFromString(validJsonString)
        Assert.assertEquals(jsonTest!!.length().toLong(), 2)
        Assert.assertEquals(jsonTest!!["key1"], "value")
        Assert.assertEquals(jsonTest!!["key2"], 1)
    }

    @Test
    fun createJSONFromStringShouldReturnNullWhenInvalidString() {
        val invalidJsonString = "\"key1\": "
        val jsonTest = JSONHelper.createJSONFromString(invalidJsonString)
        Assert.assertNull(jsonTest)
    }
}