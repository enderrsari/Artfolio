package com.ender.artfolio.features.arts

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ender.artfolio.data.model.ArtsModel
import com.ender.artfolio.data.repository.ArtsRepository
import kotlinx.coroutines.launch

class ArtsViewModel : ViewModel() {
    private val repository = ArtsRepository()
    
    private val _artsList = MutableLiveData<List<ArtsModel>>()
    val artsList: LiveData<List<ArtsModel>> = _artsList
    
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading
    
    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error
    
    fun loadArts() {
        _isLoading.value = true
        _error.value = null
        
        repository.getArts(
            onSuccess = { arts ->
                _artsList.value = arts
                _isLoading.value = false
            },
            onFailure = { exception ->
                _error.value = exception.localizedMessage
                _isLoading.value = false
            }
        )
    }
    
    fun addArt(
        artworkName: String,
        artistName: String,
        year: String,
        imageUri: android.net.Uri?,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        repository.addArt(
            artworkName = artworkName,
            artistName = artistName,
            year = year,
            imageUri = imageUri,
            onSuccess = onSuccess,
            onFailure = onFailure
        )
    }
    
    fun getArtById(artId: String, onSuccess: (ArtsModel?) -> Unit, onFailure: (Exception) -> Unit) {
        repository.getArtById(
            artId = artId,
            onSuccess = onSuccess,
            onFailure = onFailure
        )
    }
}