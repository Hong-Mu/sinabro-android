package com.witchapps.sinabro.api

import com.witchapps.sinabro.BuildConfig
import com.witchapps.sinabro.api.response.AladinResult
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("ItemList.aspx")
    suspend fun getList(
        @Query("TTBKey") ttbkey: String = BuildConfig.ALADIN_API_TTB_KEY,
        @Query("QueryType") queryType: String = "ItemNewAll",
        @Query("MaxResults") maxResults: Int = 10,
        @Query("Start") startPage: Int = 1,
        @Query("SearchTarget") searchTarget: String = "Book",
        @Query("Output") output: String = "js",
        @Query("Version") version: String = "20131101",
        @Query("Cover") coverSize: String = "Big",
        @Query("Sort") sortType: String = "Accuracy",
    ): Response<AladinResult>

    @GET("ItemLookUp.aspx")
    suspend fun getByItemId(
        @Query("ItemId") itemId: String,
        @Query("ItemIdType") itemIdType: String = "ISBN",
        @Query("TTBKey") ttbkey: String = BuildConfig.ALADIN_API_TTB_KEY,
        @Query("Output") output: String = "js",
        @Query("Version") version: String = "20131101",
        @Query("Cover") coverSize: String = "Big",
    ): Response<AladinResult>

    @GET("ItemSearch.aspx")
    suspend fun search(
        @Query("Query") keyword: String,
        @Query("TTBKey") ttbkey: String = BuildConfig.ALADIN_API_TTB_KEY,
        @Query("QueryType") queryType: String = "Keyword",
        @Query("MaxResults") maxResults: Int = 30,
        @Query("Start") startPage: Int = 1,
        @Query("SearchTarget") target: String = "Book",
        @Query("Output") output: String = "js",
        @Query("Version") version: String = "20131101",
        @Query("Cover") coverSize: String = "Big",
    ): Response<AladinResult>
}