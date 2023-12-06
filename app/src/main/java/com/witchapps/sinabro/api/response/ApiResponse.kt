package com.witchapps.sinabro.api.response

data class ApiResponse(
    val item: List<Book>?,
    val itemsPerPage: Int?,
    val searchCategoryId: Int?,
    val searchCategoryName: String?,
    val startIndex: Int?,
    val title: String?,
    val totalResults: Int?,
    val errorCode: Int?,
    val errorMessage: String?,
)