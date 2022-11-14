package com.padc.themovieapp.mvi.viewmodels

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.padc.themovieapp.mvi.intents.MainIntent
import com.padc.themovieapp.mvi.mvibase.MVIViewModel
import com.padc.themovieapp.mvi.processors.MainProcessor
import com.padc.themovieapp.mvi.states.MainState

class MainViewModel(override var state: MutableLiveData<MainState> = MutableLiveData(MainState.idle())):
    MVIViewModel<MainState,MainIntent>,ViewModel(){

    private val mProcessor =MainProcessor
    override fun processIntent(intent: MainIntent, lifecycleOwner: LifecycleOwner) {
        when(intent){

            //LoadHomepageData
            MainIntent.LoadAllHomePageData ->{
                state.value?.let {
                    mProcessor.loadAllHomePageData(
                        previousState = it
                    ).observe(lifecycleOwner){newState ->
                        state.postValue(newState)
                        if(newState.moviesByGenres.isEmpty()){
                            processIntent(MainIntent.LoadMoviesByGenreIntent(0),lifecycleOwner)
                        }
                    }
                }
            }

            //Load Movies By Genre
            is MainIntent.LoadMoviesByGenreIntent ->{
                state.value?.let {
                    val genreId = it.genres.getOrNull(intent.genrePosition)?.id ?: 0
                    mProcessor.loadMovieByGenre(
                        genreId =  genreId,
                        previousState = it
                    ).observe(lifecycleOwner, state:: postValue)
                }
            }
        }
    }

}