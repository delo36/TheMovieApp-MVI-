package com.padc.themovieapp.mvi.states

import com.padc.themovieapp.data.vos.ActorVO
import com.padc.themovieapp.data.vos.GenreVO
import com.padc.themovieapp.data.vos.MovieVO
import com.padc.themovieapp.mvi.mvibase.MVIState

data class MainState (
    val isLoading: Boolean = false,
    val errorMessage: String = "",
    val nowPlayingMovies: List<MovieVO>,
    val popularMovies: List<MovieVO>,
    val topRatedMovies: List<MovieVO>,
    val genres: List<GenreVO>,
    val moviesByGenres : List<MovieVO>,
    val actors: List<ActorVO>
):MVIState{
    companion object{
        fun idle(): MainState = MainState(
            isLoading = false,
            errorMessage = "",
            nowPlayingMovies = listOf(),
            popularMovies =  listOf(),
            topRatedMovies = listOf(),
            genres = listOf(),
            moviesByGenres = listOf(),
            actors = listOf()
        )
    }

}