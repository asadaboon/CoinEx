package com.example.restacar.common

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody
import okhttp3.ResponseBody.Companion.toResponseBody
import java.io.BufferedInputStream
import java.io.ByteArrayOutputStream
import java.lang.reflect.Type
import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.nio.file.Paths

object JsonFactory {
    fun getStringFromJsonTestResource(path: String): String? {
        val source = javaClass.getResourceAsStream("/$path") ?: return null
        val bis = BufferedInputStream(source)
        val buf = ByteArrayOutputStream()
        var result = bis.read()
        while (result != -1) {
            buf.write(result.toByte().toInt())
            result = bis.read()
        }
        return buf.toString("UTF-8")
    }

    inline fun <reified T> parseToClass(fileName: String): T {
        val path = "src/test/resources/"
        val encoded = Files.readAllBytes(Paths.get(path + fileName))
        val stringJson = String(encoded, StandardCharsets.UTF_8)
        return Gson().fromJson<T>(stringJson, T::class.java)
    }

    inline fun <reified T> parseArray(json: JsonObject, typeToken: Type): T {
        return GsonBuilder().create().fromJson(json, typeToken)
    }

    private fun getJsonString(fileName: String): String {
        val path = "src/test/resources/"
        val encoded = Files.readAllBytes(Paths.get(path + fileName))
        return String(encoded, StandardCharsets.UTF_8)
    }
}