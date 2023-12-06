package com.witchapps.sinabro.api.response

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Book(
    val adult: Boolean,
    val author: String,
    val categoryId: Int,
    val categoryName: String,
    var cover: String,
    val customerReviewRank: Int,
    val description: String,
    val fixedPrice: Boolean,
    val isbn: String,
    val isbn13: String,
    val itemId: Int,
    val link: String,
    val mallType: String,
    val mileage: Int,
    val priceSales: Int,
    val priceStandard: Int,
    val pubDate: String,
    val publisher: String,
    val salesPoint: Int,
    val seriesInfo: SeriesInfo?,
    val stockStatus: String,
    val title: String,
): Parcelable