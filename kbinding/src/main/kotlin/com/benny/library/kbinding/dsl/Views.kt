package com.benny.library.kbinding.dsl

import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.benny.library.kbinding.bind.*
import com.benny.library.kbinding.view.BindableLayout
import com.benny.library.kbinding.view.ViewComponent
import org.jetbrains.anko.*
import org.jetbrains.anko.internals.AnkoInternals

/**
 * Created by benny on 12/16/15.
 */
val AnkoContext<*>.OneWay: OneWay get() = BindingMode.OneWay
val AnkoContext<*>.TwoWay: TwoWay get() = BindingMode.TwoWay
val AnkoContext<*>.OneWayToSource: OneWayToSource get() = BindingMode.OneWayToSource

fun <T> AnkoContext<T>.bindableLayout(init: BindableLayout<T>.() -> Unit): BindableLayout<T> {
    val bindableLayout = BindableLayout(this.ctx, this.owner)
    bindableLayout.init()
    AnkoInternals.addView(this, bindableLayout.view)
    return bindableLayout
}

fun AnkoContext<*>.bind(propertyBindingFactory: () -> PropertyBinding): Unit {
    if(this is BindableLayout<*>) bindingAssembler.addBinding(propertyBindingFactory())
}

fun AnkoContext<*>.wait(propertyBindingFactory: () -> PropertyBinding): Unit {
    bind(propertyBindingFactory)
}

fun <T> AnkoContext<T>.inflate(viewComponent: ViewComponent, parent: ViewGroup, prefix: String = "") : View = when(this) {
    is BindableLayout<*> -> {
        inflate(viewComponent, parent, prefix)
    }
    else -> {
        val view = ctx.UI { viewComponent.builder()() }.view
        parent.addView(view)
        view
    }
}
