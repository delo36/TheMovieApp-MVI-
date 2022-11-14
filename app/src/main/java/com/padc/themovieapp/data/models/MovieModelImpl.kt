package com.padc.themovieapp.data.models

import android.annotation.SuppressLint
import android.content.Context
import androidx.lifecycle.LiveData
import com.padc.themovieapp.data.vos.*
import com.padc.themovieapp.network.dataagents.MovieDataAgent
import com.padc.themovieapp.network.dataagents.RetrofitDataAgentImpl
import com.padc.themovieapp.persistence.MovieDatabase
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import retrofit2.http.Query

object MovieModelImpl : BaseModel(), MovieModel {

    @SuppressLint("CheckResult")
    override fun getNowPlayingMovies(
        onFailure: (String) -> Unit
    ): LiveData<List<MovieVO>>?{

        //Network
        mTheMovieApi.getNowPlayingMovies(page = 1)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                it.results?.forEach{ movie ->
                    movie.type = NOW_PLAYING
                }
                mMovieDatabase?.movieDao()?.insertMovies(it.results ?: listOf())

            },{
                onFailure(it.localizedMessage ?: "")
            })

        return mMovieDatabase?.movieDao()?.getMovieByTypeLive(type = NOW_PLAYING)
    }

    @SuppressLint("CheckResult")
    override fun getPopularMovies(
        onFailure: (String) -> Unit
    ): LiveData<List<MovieVO>>? {
        //Network
        mTheMovieApi.getPopularMovies(page = 1)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                it.results?.forEach { movie -> movie.type = POPULAR }
                mMovieDatabase?.movieDao()?.insertMovies(it.results ?: listOf())

            },{
                onFailure(it.localizedMessage ?: "")
            })
        return mMovieDatabase?.movieDao()?.getMovieByTypeLive(type = POPULAR)
    }
    @SuppressLint("CheckResult")
    override fun getTopRatedMovies(
        onFailure: (String) -> Unit
    ): LiveData<List<MovieVO>>? {
        //Network
       mTheMovieApi.getTopRatedMovies(page = 1)
           .subscribeOn(Schedulers.io())
           .observeOn(AndroidSchedulers.mainThread())
           .subscribe({
               it.results?.forEach { movie -> movie.type = TOP_RATED }
               mMovieDatabase?.movieDao()?.insertMovies(it.results ?: listOf())

           },{
               onFailure(it.localizedMessage ?:"")
           })
        return mMovieDatabase?.movieDao()?.getMovieByTypeLive(type = POPULAR)
    }

    @SuppressLint("CheckResult")
    override fun getActors(
        onSuccess: (List<ActorVO>) -> Unit,
        onFailure: (String) -> Unit
    ) {
       mTheMovieApi.getActors()
           .subscribeOn(Schedulers.io())
           .observeOn(AndroidSchedulers.mainThread())
           .subscribe({
                onSuccess(it.results ?: listOf())
           }, {
               onFailure(it.localizedMessage ?: "")
           })
    }
    @SuppressLint("CheckResult")
    override fun getGenres(
        onSuccess: (List<GenreVO>) -> Unit,
        onFailure: (String) -> Unit
    ) {
        mTheMovieApi.getGenres()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                onSuccess(it.genres ?: listOf())
            },{
                onFailure(it.localizedMessage ?: "")
            })
    }
    @SuppressLint("CheckResult")
    override fun getMoviesByGenre(
        genreId: String,
        onSuccess: (List<MovieVO>) -> Unit,
        onFailure: (String) -> Unit
    ) {
      mTheMovieApi.getMoviesByGenre(genreId = genreId)
          .subscribeOn(Schedulers.io())
          .observeOn(AndroidSchedulers.mainThread())
          .subscribe({
              onSuccess(it.results ?: listOf())
          }, {
              onFailure(it.localizedMessage ?: "")
          })
    }
    @SuppressLint("CheckResult")
    override fun getMovieDetail(
        movieId: String,
        onFailure: (String) -> Unit
    ): LiveData<MovieVO?>?  {

        //NetWork
        mTheMovieApi.getMovieDetail(movieId = movieId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                val movieFromDatabaseToSync =
                    mMovieDatabase?.movieDao()?.getMovieByOneTime(movieId = movieId.toInt())
                it.type = movieFromDatabaseToSync?.type
                mMovieDatabase?.movieDao()?.insetSingleMovie(it)

            },{
                onFailure(it.localizedMessage ?: "")
            })

        return  mMovieDatabase?.movieDao()?.getMovieById(movieId = movieId.toInt())
    }
    @SuppressLint("CheckResult")
    override fun getCreditsByMovie(
        movieId: String,
        onSuccess: (Pair<List<ActorVO>, List<ActorVO>>) -> Unit,
        onFailure: (String) -> Unit
    ) {
        mTheMovieApi.getCreditsByMovie(movieId = movieId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                onSuccess(Pair(it.cast ?: listOf(), it.cast ?: listOf()))
            },{
                onFailure(it.localizedMessage ?: "")
            })
    }
    @SuppressLint("CheckResult")
    override fun searchMovie(query: String): Observable<List<MovieVO>>{
        return mTheMovieApi
            .searchMovie(query = query)
            .map{it.results ?: listOf() }
            .onErrorResumeNext { Observable.just(listOf()) }
            .subscribeOn(Schedulers.io())
    }

    override fun getNowPlayingMoviesObservable(): Observable<List<MovieVO>>? {
        mTheMovieApi.getNowPlayingMovies(page = 1)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe{
                it.results?.forEach{ movie ->
                    movie.type = NOW_PLAYING
                }
                mMovieDatabase?.movieDao()?.insertMovies(it.results ?: listOf())

            }

        return mMovieDatabase?.movieDao()?.getMovieByTypeFlowable(type = NOW_PLAYING)?.toObservable()
    }

    override fun getPopularMoviesObservable(): Observable<List<MovieVO>>? {
        mTheMovieApi.getPopularMovies(page = 1)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe{
                it.results?.forEach { movie -> movie.type = POPULAR }
                mMovieDatabase?.movieDao()?.insertMovies(it.results ?: listOf())

            }
        return mMovieDatabase?.movieDao()?.getMovieByTypeFlowable(type = POPULAR)?.toObservable()
    }

    override fun getTopRatedMoviesObservable(): Observable<List<MovieVO>>? {
        mTheMovieApi.getTopRatedMovies(page = 1)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe{
                it.results?.forEach { movie -> movie.type = TOP_RATED }
                mMovieDatabase?.movieDao()?.insertMovies(it.results ?: listOf())

            }
        return mMovieDatabase?.movieDao()?.getMovieByTypeFlowable(type = POPULAR)?.toObservable()
    }

    override fun getGenresObservable(): Observable<List<GenreVO>>? {
        return mTheMovieApi.getGenres()
            .map { it.genres ?: listOf() }
            .subscribeOn(Schedulers.io())
    }

    override fun getActorsObservable(): Observable<List<ActorVO>>? {
        return mTheMovieApi.getActors()
            .map { it.results ?: listOf() }
            .subscribeOn(Schedulers.io())
    }

    override fun getMoviesByGenresObservable(genreId: String): Observable<List<MovieVO>>? {
        return mTheMovieApi.getMoviesByGenre(genreId = genreId)
            .map { it.results ?: listOf() }
            .subscribeOn(Schedulers.io())
    }

    override fun getMoviesByIdObservable(movieId: Int): Observable<MovieVO?>? {
        mTheMovieApi.getMovieDetail(movieId = movieId.toString())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe{
                val movieFromDatabaseToSync =
                    mMovieDatabase?.movieDao()?.getMovieByOneTime(movieId = movieId.toInt())
                it.type = movieFromDatabaseToSync?.type
                mMovieDatabase?.movieDao()?.insetSingleMovie(it)

            }

        return  mMovieDatabase?.movieDao()?.getMovieByIdFlowable(movieId = movieId)?.toObservable()
    }


    override fun getCreditsByMovieObservable(movieId: Int): Observable<Pair<List<ActorVO>, List<ActorVO>>>? {
        return  mTheMovieApi.getCreditsByMovie(movieId = movieId.toString())
            .map {Pair(it.cast ?: listOf(), it.cast ?: listOf()) }
            .subscribeOn(Schedulers.io())
    }


}
