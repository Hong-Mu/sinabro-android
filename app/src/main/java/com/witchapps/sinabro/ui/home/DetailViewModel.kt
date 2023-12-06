package com.witchapps.sinabro.ui.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.witchapps.sinabro.api.response.Book
import com.witchapps.sinabro.api.response.SimpleBook
import com.witchapps.sinabro.data.AladinRepository
import com.witchapps.sinabro.ui.util.ApiResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val aladinRepository: AladinRepository
) : ViewModel() {

    val getSimpleBookResult = MutableLiveData<ApiResult<List<SimpleBook>>>()

    fun getSimpleBook(itemId: String) = viewModelScope.launch(Dispatchers.IO) {
        var apiResult = ApiResult<List<SimpleBook>>(false, null)
        try {
            val result = aladinRepository.getSimpleBook(itemId)
            Timber.d(result.toString())

            result.series?.let {
                if(it.isNotEmpty()) {
                    apiResult.data = it
                    apiResult.isSuccessful = true
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            getSimpleBookResult.postValue(apiResult)

        }
    }

    val getBookResult =  MutableLiveData<ApiResult<List<Book>>>()

    fun getBook(ItemId: String, type: String) = viewModelScope.launch(Dispatchers.IO) {
        val apiResult = ApiResult<List<Book>>(false, null)
        try {
            val result = aladinRepository.getBook(ItemId, type)
            if(result.isSuccessful) {
                apiResult.data = result.body()?.item
                apiResult.isSuccessful = true
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            getBookResult.postValue(apiResult)
        }
    }
}