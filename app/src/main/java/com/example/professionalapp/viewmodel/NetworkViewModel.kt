package com.example.professionalapp.viewmodel

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.professionalapp.util.Status
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class NetworkViewModel :ViewModel() {
    private var _networkObserver = MutableStateFlow<Status>(Status.Unit)
    val networkObserver = _networkObserver.asStateFlow()

    fun observeNetStatus(context: Context) {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val callBack = object : ConnectivityManager.NetworkCallback(){
            override fun onAvailable(network: Network) {
                super.onAvailable(network)
                viewModelScope.launch {
                    _networkObserver.emit(Status.Available)
                }
            }

            override fun onLosing(network: Network, maxMsToLive: Int) {
                super.onLosing(network, maxMsToLive)
                viewModelScope.launch {
                    _networkObserver.emit(Status.Losing)
                }
            }

            override fun onLost(network: Network) {
                super.onLost(network)
                viewModelScope.launch {
                    _networkObserver.emit(Status.Lost)
                }
            }

            override fun onUnavailable() {
                super.onUnavailable()
                viewModelScope.launch {
                    _networkObserver.emit(Status.Unavailable)
                }
            }
        }

        connectivityManager.registerDefaultNetworkCallback(callBack)

    }



}