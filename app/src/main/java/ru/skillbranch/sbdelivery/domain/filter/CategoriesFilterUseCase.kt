package ru.skillbranch.sbdelivery.domain.filter

import io.reactivex.rxjava3.core.Single
import ru.skillbranch.sbdelivery.domain.entity.DishEntity

interface CategoriesFilterUseCase {
    fun categoryFilterDishes(categoryId: String): Single<List<DishEntity>>
}