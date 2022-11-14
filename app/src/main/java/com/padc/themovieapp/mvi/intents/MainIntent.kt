package com.padc.themovieapp.mvi.intents

import com.padc.themovieapp.mvi.mvibase.MVIIntent

sealed class MainIntent {
    class LoadMoviesByGenreIntent(val genrePosition: Int): MainIntent()
    object LoadAllHomePageData : MainIntent()
}