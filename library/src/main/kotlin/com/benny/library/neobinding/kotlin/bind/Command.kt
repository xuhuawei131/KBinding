package com.benny.library.neobinding.kotlin.bind

import rx.Observable
import rx.Observer
import rx.subjects.BehaviorSubject

/**
 * Created by benny on 11/17/15.
 */

public class Command<T> {
    public val observable: Observable<T>
    public val onSuccess: (t: T) -> Unit
    public val  onError: (e: Throwable) -> Unit

    constructor(observable: Observable<T>, onSuccess: (t: T) -> Unit = {}, onError: (e: Throwable) -> Unit = {}) {
        this.observable = observable
        this.onSuccess = onSuccess
        this.onError = onError
    }

    constructor(action: () -> T, onSuccess: (t: T) -> Unit = {}, onError: (e: Throwable) -> Unit = {}) {
        this.onSuccess = onSuccess
        this.onError = onError
        observable = Observable.create {
            it.onNext(action())
            it.onCompleted()
        }
    }
    /*val _canExecute: BehaviorSubject<Boolean> = BehaviorSubject.create()
        get() {
            if(field.hasCompleted()) field = BehaviorSubject.create()
            return field
        }

    public val canExecute: Observable<Boolean>
        get() = _canExecute

    public val execute : Observable<T>
        get() = observable.doOnSubscribe{ _canExecute.onNext(false) }.doOnTerminate{ _canExecute.onNext(true) }*/

    public fun execute(canExecute: (canExecute: Boolean) -> Unit) {
        observable.doOnSubscribe{ canExecute(false) }.doOnTerminate{ canExecute(true) }.subscribe(onSuccess, onError)
    }

}