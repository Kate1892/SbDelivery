package ru.skillbranch.sbdelivery.screens.menu.ui

import android.util.Log
import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.unit.dp
import coil.annotation.ExperimentalCoilApi
import ru.skillbranch.sbdelivery.screens.components.LazyGrid
import ru.skillbranch.sbdelivery.screens.menu.logic.MenuFeature
import ru.skillbranch.sbdelivery.screens.root.logic.Msg

@ExperimentalCoilApi
@ExperimentalFoundationApi
@ExperimentalComposeUiApi
@Composable
fun MenuScreen(state: MenuFeature.State, accept: (Msg) -> Unit) {

    val dispatcher = LocalOnBackPressedDispatcherOwner.current!!.onBackPressedDispatcher
    val backCallback = remember {
        object : OnBackPressedCallback(false) {
            override fun handleOnBackPressed() {
                Log.e("MenuScreen", "press back")
                MenuFeature.Msg.PopCategory
                    .let(Msg::Menu)
                    .also(accept)
            }
        }
    }

    SideEffect {
        Log.e("MenuScreen", "side effect")
        backCallback.isEnabled = state.parent != null
    }

    DisposableEffect(key1 = dispatcher) {
        Log.e("MenuScreen", "add back callback")
        dispatcher.addCallback(backCallback)

        onDispose {
            Log.e("MenuScreen", "remove back callback")
            backCallback.remove()
        }
    }

    LazyGrid(
        items = state.current,
        cols = 3,
        cellsPadding = 16.dp
    ) { category ->
        MenuItem(item = category, onClick = {
            MenuFeature.Msg.ClickCategory(category.id, category.title)
                .let(Msg::Menu)
                .also(accept)
        })
    }
}