package ru.skillbranch.sbdelivery.ui.search

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.delay
import ru.skillbranch.sbdelivery.core.BaseViewModel
import ru.skillbranch.sbdelivery.core.adapter.ProductItemState
import ru.skillbranch.sbdelivery.domain.SearchUseCase
import ru.skillbranch.sbdelivery.domain.entity.DishEntity
import ru.skillbranch.sbdelivery.repository.error.EmptyDishesError
import ru.skillbranch.sbdelivery.repository.mapper.DishesMapper
import ru.skillbranch.sbdelivery.ui.main.MainState
import java.util.concurrent.TimeUnit

class SearchViewModel(
    private val useCase: SearchUseCase,
    private val mapper: DishesMapper
) : BaseViewModel() {
    private val defaultState = SearchState.Loading
    private val action = MutableLiveData<SearchState>()
    val state: LiveData<SearchState>
        get() = action

    fun initState() {
        useCase.getDishes()
            .doOnSubscribe { action.value = defaultState }
            .map { dishes -> mapper.mapDtoToState(dishes) }
            .subscribe({
                val newState = SearchState.Result(it)
                action.value = newState
            }, {
                if (it is EmptyDishesError) {
                    action.value = SearchState.Error(it.messageDishes)
                } else {
                    action.value = SearchState.Error("Что то пошло не по плану")
                }
                it.printStackTrace()
            }).track()
    }

    fun setSearchEvent(searchEvent: Observable<String>) {
        searchEvent
            .debounce(800L, TimeUnit.MILLISECONDS)
            .distinctUntilChanged()
            .observeOn(AndroidSchedulers.mainThread())
            .doOnNext { action.value = defaultState }
            .observeOn(Schedulers.io())
            .switchMap { useCase.findDishesByName(it) }
            .delay(2, TimeUnit.SECONDS)
            .map { mapper.mapDtoToState(it) }
            .observeOn(AndroidSchedulers.mainThread())
//            .onErrorResumeNext { error ->
//                if (error is EmptyDishesError) {
//                      action.value = SearchState.Error(error.messageDishes)
//                    Observable.just(emptyList<ProductItemState>())
//                } else {
//                    Observable.error(error)
//                }
//            }
            .subscribe({
                if (it.isNotEmpty()) {
                    val newState = SearchState.Result(it)
                    action.value = newState
                }
            }, {
                if (it is EmptyDishesError) {
                    action.value = SearchState.Error(it.messageDishes)
                } else {
                    action.value = SearchState.Error("Что то пошло не по плану")
                }
                it.printStackTrace()
            }
            ).track()
    }


}