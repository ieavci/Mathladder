// jsondatabase.kt
package com.example.arcore.database

import android.content.Context
import org.json.JSONArray
import org.json.JSONObject
import java.io.File
import java.io.FileWriter

object JsonDatabase {

    private const val DATABASE_FILE_NAME = "database.json"
    private lateinit var databaseFile: File

    private const val USERS_KEY = "users"
    private const val PRICES_KEY = "prices"
    private const val PAYMENTS_KEY = "payments"

    fun initialize(context: Context) {
        databaseFile = File(context.filesDir, DATABASE_FILE_NAME)
        if (!databaseFile.exists()) {
            val initialData = JSONObject().apply {
                put(USERS_KEY, JSONArray())
                put(PRICES_KEY, createInitialPrices())
                put(PAYMENTS_KEY, JSONArray())
            }
            saveDatabase(initialData)
        }
    }

    private fun loadDatabase(): JSONObject {
        return if (databaseFile.exists()) {
            val content = databaseFile.readText()
            JSONObject(content)
        } else {
            JSONObject().apply {
                put(USERS_KEY, JSONArray())
                put(PRICES_KEY, createInitialPrices())
                put(PAYMENTS_KEY, JSONArray())
            }
        }
    }

    private fun saveDatabase(database: JSONObject) {
        FileWriter(databaseFile).use {
            it.write(database.toString(4))
        }
    }

    private fun createInitialPrices(): JSONArray {
        return JSONArray().apply {
            put(JSONObject().apply {
                put("type", "free")
                put("price", 0)
            })
            put(JSONObject().apply {
                put("type", "unlimited")
                put("price", 50)
            })
        }
    }
    // Add this method to JsonDatabase
    fun processPayment(cardNumber: String, cvv: String, expiryDate: String): Boolean {
        return cardNumber.isNotEmpty() && cvv.isNotEmpty() && expiryDate.isNotEmpty()
    }



    fun addUser(username: String, password: String): Boolean {
        val database = loadDatabase()
        val users = database.getJSONArray(USERS_KEY)

        for (i in 0 until users.length()) {
            val user = users.getJSONObject(i)
            if (user.getString("username") == username) {
                return false
            }
        }

        val newUser = JSONObject().apply {
            put("username", username)
            put("password", password)
        }
        users.put(newUser)
        saveDatabase(database)
        return true
    }

    fun authenticateUser(username: String, password: String): Boolean {
        val database = loadDatabase()
        val users = database.getJSONArray(USERS_KEY)

        for (i in 0 until users.length()) {
            val user = users.getJSONObject(i)
            if (user.getString("username") == username && user.getString("password") == password) {
                return true
            }
        }
        return false
    }

    fun savePayment(username: String, cardNumber: String, cvv: String, expiryDate: String): Boolean {
        val database = loadDatabase()
        val payments = database.getJSONArray(PAYMENTS_KEY)

        val payment = JSONObject().apply {
            put("username", username)
            put("cardNumber", cardNumber)
            put("cvv", cvv)
            put("expiryDate", expiryDate)
        }

        payments.put(payment)
        saveDatabase(database)
        return true
    }

    fun getPrices(): List<Pair<String, Double>> {
        val database = loadDatabase()
        val prices = database.getJSONArray(PRICES_KEY)
        val priceList = mutableListOf<Pair<String, Double>>()

        for (i in 0 until prices.length()) {
            val price = prices.getJSONObject(i)
            priceList.add(Pair(price.getString("type"), price.getDouble("price")))
        }
        return priceList
    }

}
