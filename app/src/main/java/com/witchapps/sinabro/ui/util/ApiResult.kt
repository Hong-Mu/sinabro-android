package com.witchapps.sinabro.ui.util

data class ApiResult<T>(
    var isSuccessful: Boolean,
    var data: T? = null,
)
