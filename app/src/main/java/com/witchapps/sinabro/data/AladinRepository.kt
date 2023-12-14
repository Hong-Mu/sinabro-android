package com.witchapps.sinabro.data

import com.witchapps.sinabro.api.ApiService
import com.witchapps.sinabro.api.JsoupApiService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AladinRepository @Inject constructor(
    private val apiService: ApiService,
    private val jsoupApiService: JsoupApiService
) {
    suspend fun getList(type: String, page: Int, count: Int, sortType: String) =
        apiService.getList(queryType = type,
            startPage = page,
            maxResults = count,
            sortType = sortType
        )

    suspend fun getBook(itemId: String, itemIdType: String = "ISBN") =
        apiService.getByItemId(itemId, itemIdType)

    suspend fun searchBook(keyword: String) = apiService.search(keyword)

    suspend fun getSimpleBook(itemId: String) = jsoupApiService.getItem(itemId)
}