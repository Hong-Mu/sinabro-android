package com.witchapps.sinabro.ui.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.witchapps.sinabro.api.response.Book
import com.witchapps.sinabro.data.AladinRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val aladinRepository: AladinRepository
) : ViewModel() {

    init {
        getList()
    }

    val bookList = MutableLiveData<List<Book>>()
    fun getList() = viewModelScope.launch(Dispatchers.IO) {
        try {
            val result = aladinRepository.getList("Bestseller", 1, 50)
            if(result.isSuccessful) {
                val list = result.body()?.item?: listOf()
                // list.forEach { it.cover = it.cover.replace("coversum", "cover") }
                bookList.postValue(list)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun search(keyword: String) = viewModelScope.launch(Dispatchers.IO) {
        try {
            val result = aladinRepository.searchBook(keyword)
            if(result.isSuccessful) {
                val list = result.body()?.item?: listOf()
                bookList.postValue(list)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}