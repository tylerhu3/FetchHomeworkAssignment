package com.tyler.hu.fetchhomework

import org.json.JSONArray
import java.net.URL

class OnlineDataRepository {

    data class FetchItem(val id: Int, val listId: Int, val name: String)

    fun getItems(): List<FetchItem> {
        val jsonText = URL("https://fetch-hiring.s3.amazonaws.com/hiring.json").readText()
        val jsonArray = JSONArray(jsonText)
        val fetchItems = mutableListOf<FetchItem>()

        for (i in 0 until jsonArray.length()) {
            val jsonObject = jsonArray.getJSONObject(i)
            val id = jsonObject.getInt("id")
            val listId = jsonObject.getInt("listId")
            val name = jsonObject.getString("name")
            val fetchItem = FetchItem(id, listId, name)
            fetchItems.add(fetchItem)
        }

        fetchItems.forEach { item ->
            println("ID: ${item.id}, ListID: ${item.listId}, Name: ${item.name}")
        }
        return fetchItems
    }

}