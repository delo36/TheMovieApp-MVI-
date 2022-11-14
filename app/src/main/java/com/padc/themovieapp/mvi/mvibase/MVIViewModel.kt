package com.padc.themovieapp.mvi.mvibase

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import com.padc.themovieapp.mvi.intents.MainIntent

interface MVIViewModel <S :MVIState, I:MainIntent>{
    val state: MutableLiveData<S>
    fun processIntent(intent: I,lifecycleOwner: LifecycleOwner)
}