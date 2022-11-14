package com.padc.themovieapp.mvi.mvibase

interface MVIView<S : MVIState> {
    fun render(state: S)
}