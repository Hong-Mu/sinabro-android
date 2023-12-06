package com.witchapps.sinabro.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.witchapps.sinabro.data.AladinRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val aladinRepository: AladinRepository
): ViewModel() {


}