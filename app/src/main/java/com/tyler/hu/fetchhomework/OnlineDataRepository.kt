package com.tyler.hu.fetchhomework

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONArray
import java.net.URL

class OnlineDataRepository {

    data class FetchItem(val id: Int, val listId: Int, val name: String)

    suspend fun getItems(): Map<Int, List<FetchItem>> = withContext(Dispatchers.IO) {
        val jsonText = URL("https://fetch-hiring.s3.amazonaws.com/hiring.json").readText()
        val jsonArray = JSONArray(jsonText)
        val fetchItems = mutableListOf<FetchItem>()

        for (i in 0 until jsonArray.length()) {
            val jsonObject = jsonArray.getJSONObject(i)
            val id = jsonObject.getInt("id")
            val listId = jsonObject.getInt("listId")
            val name = jsonObject.getString("name")
            // Only add to the list if the name is not blank or null
            println("TYLER:: ID: $id, ListID: $listId, Name: $name")
            println("TYLER:: name.isNotBlank(): ${name.isNotBlank()} && name.lowercase(): ${name.lowercase()}")
            if (name.isNotBlank() && name.lowercase() != "null") {
                val fetchItem = FetchItem(id, listId, name)
                fetchItems.add(fetchItem)
            }
        }

        // Separate items by listId via groupBy and sort them by keys:
        val groupedItems = fetchItems.groupBy { it.listId }.toSortedMap()

        // Sort each list by names
        val sortedItems = groupedItems.mapValues { (_, items) ->
            items.sortedBy { it.name }
        }

        sortedItems.forEach { (listId, items) ->
            println("TYLER:: ListID: $listId")
            items.forEach { item ->
                println("TYLER:: ID: ${item.id}, Name: ${item.name}")
            }
            println()
        }
        sortedItems
    }
}