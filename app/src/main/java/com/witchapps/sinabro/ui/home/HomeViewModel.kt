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

    var selectedSortIndex = 0

    init {
        getList()
    }

    val bookList = MutableLiveData<List<Book>>()
    fun getList(sortType: String = "Accuracy") = viewModelScope.launch(Dispatchers.IO) {
        try {
            val result = aladinRepository.getList("Bestseller", 1, 100, sortType)
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