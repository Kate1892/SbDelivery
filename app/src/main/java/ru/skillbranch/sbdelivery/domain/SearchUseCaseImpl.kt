package ru.skillbranch.sbdelivery.domain

import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import ru.skillbranch.sbdelivery.domain.entity.DishEntity
import ru.skillbranch.sbdelivery.repository.DishesRepositoryContract
import java.util.Locale

class SearchUseCaseImpl(private val repository: DishesRepositoryContract) : SearchUseCase {

    override fun getDishes(): Single<List<DishEntity>> = repository.getCachedDishes()

//    override fun findDishesByName(searchText: String): Observable<List<DishEntity>> =
//        repository.findDishesByName(searchText)

    override fun findDishesByName(searchText: String): Observable<List<DishEntity>> =
        repository.getCachedDishes().toObservable()
            .map { dishes ->
                val dish = dishes.filter {
                    it.title.lowercase(Locale.ROOT).contains(
                        searchText.trim()
                            .lowercase(Locale.ROOT)
                    )
                }
//                if (dish.isEmpty()) throw Exception("no dishes found")
                return@map dish
            }
}