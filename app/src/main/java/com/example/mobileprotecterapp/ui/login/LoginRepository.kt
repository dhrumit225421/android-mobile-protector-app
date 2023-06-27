package com.example.mobileprotecterapp.ui.login

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.mobileprotecterapp.api.APIClient
import com.example.mobileprotecterapp.api.APIInterface
import com.example.mobileprotecterapp.model.ResponseLogin
import com.example.mobileprotecterapp.model.User
import com.example.mobileprotecterapp.utils.API_ERROR_MSG
import com.shreejipackaging.data.ResponseManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class LoginRepository(application: Application) {

    private val apiClient: APIInterface = APIClient.getClientWithoutToken(application)

    suspend fun userLogin(userName: User): LiveData<ResponseManager<ResponseLogin>> {

        val liveData = MutableLiveData<ResponseManager<ResponseLogin>>()

        try {
            val response =
                withContext(Dispatchers.IO) { apiClient.userLogin(userName) }

            withContext(Dispatchers.Main) {
                if (response.password.isNullOrEmpty() && response.username.isNullOrEmpty()) {
                    liveData.value =
                        ResponseManager.Success(
                            response
                        )
                } else {
                    liveData.value =
                        ResponseManager.Error(
                            if (!response.password.isNullOrEmpty()) response.password[0] else if (!response.username.isNullOrEmpty()) response.username[0] else API_ERROR_MSG
                        )
                }
            }

        } catch (e: Exception) {
            withContext(Dispatchers.Main) {
                ResponseManager.manageException(
                    liveData,
                    e
                )
            }
        }

        return liveData
    }

}