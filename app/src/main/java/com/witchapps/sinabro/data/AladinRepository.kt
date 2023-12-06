package com.witchapps.sinabro.data

import com.witchapps.sinabro.api.ApiService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AladinRepository @Inject constructor(
    private val apiService: ApiService
) {
    suspend fun getList(type: String, page: Int, count: Int) =
        apiService.getList(queryType = type, startPage = page, maxResults = count)

    suspend fun getByItemId(itemId: String, itemIdType: String) =
        apiService.getByItemId(itemId, itemIdType)

    suspend fun search(keyword: String) = apiService.search(keyword)
}