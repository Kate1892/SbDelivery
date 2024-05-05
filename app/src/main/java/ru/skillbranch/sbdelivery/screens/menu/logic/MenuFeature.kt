package ru.skillbranch.sbdelivery.screens.menu.logic

import ru.skillbranch.sbdelivery.domain.CategoryItem
import java.io.Serializable


object MenuFeature {
    const val route: String = "menu"

    fun initialState(): State = State()
    fun initialEffects(): Set<Eff> = setOf(Eff.FindCategories)

    data class State(
        val categories: List<CategoryItem> = emptyList(),
        val parentId: String? = null
    ) : Serializable {
        val parent: CategoryItem?
            get() = categories.find { it.id == parentId }

        val current: List<CategoryItem>
            get() {
                val cats = categories.filter { it.parentId == parentId }

                return parent?.icon
                    ?.let { cats.map { c -> c.copy(icon = it) } }
                    ?: cats
            }
    }

    sealed class Eff {
        object FindCategories : Eff()
    }

    sealed class Msg {
        data class ShowMenu(val categories: List<CategoryItem>) : Msg()
        data class ClickCategory(val id: String, val title: String) : Msg()
        object PopCategory : Msg()
    }
}

