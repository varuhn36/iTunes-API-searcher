package com.example.itunes_api_searcher.utils

import com.example.itunes_api_searcher.data.model.ItunesItemMetaData
import org.json.JSONObject

object ItunesSearchParser {

    fun parse(json: String): List<ItunesItemMetaData> {
        val root = JSONObject(json)
        val resultsArray = root.optJSONArray("results") ?: return emptyList()

        val items = mutableListOf<ItunesItemMetaData>()

        for (i in 0 until resultsArray.length()) {
            val obj = resultsArray.optJSONObject(i) ?: continue

            items.add(parseItem(obj))
        }

        return items
    }

    private fun parseItem(obj: JSONObject): ItunesItemMetaData {
        return ItunesItemMetaData(

            wrapperType = obj.optString("wrapperType", null),
            kind = obj.optString("kind", null),
            trackId = optLongOrNull(obj, "trackId"),
            collectionId = optLongOrNull(obj, "collectionId"),

            artistName = obj.optString("artistName", null),
            trackName = obj.optString("trackName", null),
            collectionName = obj.optString("collectionName", null),

            artworkUrl30 = obj.optString("artworkUrl30", null),
            artworkUrl60 = obj.optString("artworkUrl60", null),
            artworkUrl100 = obj.optString("artworkUrl100", null),

            shortDescription = obj.optString("shortDescription", null),
            longDescription = obj.optString("longDescription", null),
            description = obj.optString("description", null),

            releaseDate = obj.optString("releaseDate", null),
            primaryGenreName = obj.optString("primaryGenreName", null),
            contentAdvisoryRating = obj.optString("contentAdvisoryRating", null),
            trackTimeMillis = optLongOrNull(obj, "trackTimeMillis"),

            trackPrice = optDoubleOrNull(obj, "trackPrice"),
            collectionPrice = optDoubleOrNull(obj, "collectionPrice"),
            trackRentalPrice = optDoubleOrNull(obj, "trackRentalPrice"),
            currency = obj.optString("currency", null),

            country = obj.optString("country", null)
        )
    }

    private fun optLongOrNull(obj: JSONObject, key: String): Long? =
        if (obj.has(key)) obj.optLong(key) else null

    private fun optDoubleOrNull(obj: JSONObject, key: String): Double? =
        if (obj.has(key)) obj.optDouble(key) else null
}

