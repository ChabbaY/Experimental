package cloud.englert.experimental.ui.api

import android.util.Log

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

import cloud.englert.experimental.api.JokeRetrofitInstance

import kotlinx.coroutines.launch

class ApiViewModel : ViewModel() {
    private val _text = MutableLiveData<String>().apply {
        value = "no joke fetched yet"
    }
    val text: LiveData<String> = _text

    fun update() {
        viewModelScope.launch {
            Log.d(TAG, "update called")
            try {
                val response = JokeRetrofitInstance.api.getJoke()
                _text.value = "${response.setup}\n${response.punchline}"
                Log.d(TAG, "Joke: ${response.id} (${response.type}); " +
                        "${response.setup}, ${response.punchline}")
            } catch (e: Exception) {
                Log.e(TAG, "update failed", e)
            }
        }
    }

    companion object {
        private val TAG = ApiViewModel::class.java.simpleName
    }
}