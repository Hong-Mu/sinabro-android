package com.witchapps.sinabro.api.response

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class SeriesInfo(
    val seriesId: Int,
    val seriesLink: String,
    val seriesName: String
): Parcelable