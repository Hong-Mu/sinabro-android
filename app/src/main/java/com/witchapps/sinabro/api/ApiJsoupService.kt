package com.witchapps.sinabro.api

import com.witchapps.sinabro.api.response.JsoupItemResult
import com.witchapps.sinabro.api.response.SimpleBook
import org.jsoup.Jsoup
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class JsoupApiService @Inject constructor() {
    suspend fun getItem(itemId: String): JsoupItemResult {
        val doc = Jsoup.connect("https://www.aladin.co.kr/shop/wproduct.aspx?ItemId=${itemId}").get()

        // 시리즈가 여러 개일 경우
        // val titles = doc.select(".conts_info_list4 dd > a")
        // val images = doc.select(".conts_info_list4 a.bottom img")

        // 시리즈의 일부만 가져옴
        val titles = doc.select("#ulSeriesBook0 dd > a")
        val images = doc.select("#ulSeriesBook0 a.bottom img")


        val list = (titles zip images).map { pair ->
            val itemId = pair.first.attr("href").split("=")[1]
            val title = pair.first.text().trim()
            val cover = pair.second.attr("src")
            SimpleBook(itemId, title, cover)
        }

        return JsoupItemResult(list)
    }
}