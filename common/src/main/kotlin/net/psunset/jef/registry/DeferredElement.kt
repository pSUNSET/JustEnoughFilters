package net.psunset.jef.registry

import java.util.function.Supplier
import kotlin.reflect.KProperty

/**
 * A value wrapper, making initialization of the element deferred.
 *
 * Implements [kotlin.jvm.functions.Function0] and [Supplier].
 *
 * Operator function [getValue] is also available,
 * meaning getting deferred value with `by` keyword is supported.
 */
class DeferredElement<T : Any>(private val supplier: Supplier<T>) : Function0<T>, Supplier<T> {

    val value: T by lazy { supplier.get() }

    override fun invoke(): T {
        return value
    }

    override fun get(): T {
        return value
    }

    operator fun getValue(any: Any?, property: KProperty<*>): T {
        return value
    }
}