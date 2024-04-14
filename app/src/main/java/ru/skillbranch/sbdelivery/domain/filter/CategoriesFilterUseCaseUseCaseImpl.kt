package ru.skillbranch.sbdelivery.domain.filter

import io.reactivex.rxjava3.core.Single
import ru.skillbranch.sbdelivery.domain.entity.DishEntity
import ru.skillbranch.sbdelivery.repository.DishesRepositoryContract
import ru.skillbranch.sbdelivery.repository.error.EmptyDishesError

class CategoriesFilterUseCaseUseCaseImpl(private val repository: DishesRepositoryContract) :
    CategoriesFilterUseCase {
//    override fun categoryFilterDishes(categoryId: String): Single<List<DishEntity>> =
//        repository.getCachedDishes().flatMap { itemList ->
//            Single.just(
//                if (categoryId.isEmpty()) itemList
//                else itemList.filter { it.categoryId == categoryId })
//        }

    override fun categoryFilterDishes(categoryId: String): Single<List<DishEntity>> =
        repository.getCachedDishes().flatMap { itemList ->
            Single.create { emitter ->
                if (categoryId.isEmpty()) emitter.onSuccess(itemList)
                else {
                    val filtered = itemList.filter { it.categoryId == categoryId }
                    if (filtered.isEmpty()) emitter.onError(EmptyDishesError("List is empty"))
                    else emitter.onSuccess(filtered)
                }
            }
        }
}