package com.isidroid.a18

import org.json.JSONArray
import org.json.JSONObject

data class ErrorParser(val response: String?) {
    fun errors(): MutableMap<String, MutableList<String>> {
        val result = mutableMapOf<String, MutableList<String>>()
        try {
            val root = JSONObject(response!!)
            parseObject(root, result)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return result
    }

    private fun parseObject(
        root: JSONObject,
        result: MutableMap<String, MutableList<String>>
    ) {
        try {
            root.keys().forEach { key ->
                when (val data = root.get(key)) {
                    is JSONObject -> parseObject(data, result)
                    is JSONArray -> parseArray(data, result, key)
                    is String -> addValue(key = key, value = data, result = result)
                }
            }
        } catch (e: Exception) {
        }
    }

    private fun addValue(
        key: String,
        value: String,
        result: MutableMap<String, MutableList<String>>
    ) {
        if (!result.containsKey(key)) result[key] = mutableListOf()
        result[key]!!.add(value)
    }

    private fun parseArray(
        data: JSONArray,
        result: MutableMap<String, MutableList<String>>,
        key: String
    ) {
        try {
            val value = data[0]
            if (value is String) addValue(key = key, value = value, result = result)
            else {
                when (value) {
                    is JSONObject -> parseObject(value, result)
                    is JSONArray -> parseArray(value, result, key)
                }
            }

        } catch (e: Exception) {
        }
    }
}