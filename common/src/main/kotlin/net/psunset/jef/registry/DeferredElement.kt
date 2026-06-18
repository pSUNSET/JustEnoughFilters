package net.psunset.jef.registry

import java.util.function.Supplier
import kotlin.reflect.KProperty

class DeferredElement<T : Any>(val supplier: Supplier<T>) : Function0<T>, Supplier<T> {
    private var value: T? = null
    val nonnullValue get() = value!!

    override fun invoke(): T {
        return value ?: supplier.get().also { value = it }
    }

    override fun get(): T {
        return invoke()
    }

    operator fun getValue(any: Any?, property: KProperty<*>): T {
        return invoke()
    }
}