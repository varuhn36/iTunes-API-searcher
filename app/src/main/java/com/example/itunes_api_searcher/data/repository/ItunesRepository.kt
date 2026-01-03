package com.example.itunes_api_searcher.data.repository

import com.example.itunes_api_searcher.data.model.ItunesItemMetaData
import com.example.itunes_api_searcher.utils.ItunesSearchParser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder

class ItunesRepository {

    suspend fun search(term: String, limit: Int): List<ItunesItemMetaData> {
        return withContext(Dispatchers.IO) {
            val encodedTerm = URLEncoder.encode(term, "UTF-8")
            val url = "https://itunes.apple.com/search?term=$encodedTerm&limit=$limit"
            val connection = URL(url).openConnection() as HttpURLConnection
            connection.requestMethod = "GET"

            try {
                val response = connection.inputStream.bufferedReader().use { it.readText() }
                ItunesSearchParser.parse(response)
            } finally {
                connection.disconnect()
            }
        }
    }
}