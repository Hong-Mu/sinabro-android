package com.witchapps.sinabro.ui.explore

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.witchapps.sinabro.api.response.Book
import com.witchapps.sinabro.data.AladinRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import io.grpc.internal.JsonUtil.getList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ExploreViewModel  @Inject constructor(
    private val aladinRepository: AladinRepository
) : ViewModel() {

    val bookList = MutableLiveData<List<Book>>()
    fun searchBook(keyword: String) = viewModelScope.launch(Dispatchers.IO) {
        try {
            val result = aladinRepository.searchBook(keyword)
            if(result.isSuccessful) {
                val list = result.body()?.item?: listOf()
                // list.forEach { it.cover = it.cover.replace("coversum", "cover") }
                bookList.postValue(list)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}