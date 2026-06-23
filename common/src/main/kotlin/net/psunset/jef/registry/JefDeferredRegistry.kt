package net.psunset.jef.registry

import com.google.common.collect.ImmutableList
import com.google.common.collect.ImmutableMap
import com.google.common.collect.ImmutableSet
import net.minecraft.resources.Identifier
import java.util.function.Function
import java.util.function.Supplier

/**
 * Calling [register] is only allowed before `ClientSetupEvent`.
 * Getting [entries] is only allowed after `ClientSetupEvent`.
 */
class JefDeferredRegistry<R : Any> {

    /**
     * Will be `null` after `ClientSetupEvent` fired.
     */
    private var registry: LinkedHashMap<String, DeferredElement<out R>>? = linkedMapOf()

    /**
     * Will be `null` after `ClientSetupEvent` fired.
     */
    private var priorRegistry: LinkedHashMap<String, DeferredElement<out R>>? = linkedMapOf()

    /**
     * Out of order
     */
    private val ids: MutableSet<String> = hashSetOf()

    /**
     * Will be init after `ClientSetupEvent` fired.
     */
    lateinit var entries: ImmutableMap<String, R>
        private set

    /**
     * Will be init after `ClientSetupEvent` fired.
     */
    lateinit var keys: ImmutableSet<String>
        private set

    /**
     * Will be init after `ClientSetupEvent` fired.
     */
    lateinit var values: ImmutableList<R>
        private set

    /**
     * Can only be called after `ClientSetupEvent`.
     */
    operator fun get(key: String) = entries[key]

    /**
     * Can only be called after `ClientSetupEvent`.
     */
    val size: Int get() = entries.size

    /**
     * @return [DeferredElement], wrapper of the value
     */
    private fun <T : R> register(id: String, supplier: Supplier<T>): DeferredElement<T> {
        if (registry == null) {
            throw IllegalStateException("Registry is already closed.")
        }
        if (ids.contains(id)) {
            throw IllegalArgumentException("Element with id $id already exists.")
        }
        ids.add(id)
        return DeferredElement(supplier).also { registry!![id] = it }
    }

    /**
     * Register the element wrapped by [Supplier] to registry.
     * The actual value will get unwrapped after `ClientSetupEvent` fired.
     * @return [DeferredElement], wrapper of the value
     */
    fun <T : R> register(id: Identifier, supplier: Supplier<T>): DeferredElement<T> {
        return register(id.toString(), supplier)
    }

    /**
     * Register the element wrapped by [Supplier] to registry.
     * The actual value will get unwrapped after `ClientSetupEvent` fired.
     * @return [DeferredElement], wrapper of the value
     */
    fun <T : R> register(id: Identifier, func: Function<Identifier, T>): DeferredElement<T> {
        return register(id.toString()) { func.apply(id) }
    }

    /**
     * @return [DeferredElement], wrapper of the value
     */
    private fun <T : R> priorReg(id: String, supplier: Supplier<T>): DeferredElement<T> {
        ids.add(id)
        return DeferredElement(supplier).also { priorRegistry!![id] = it }
    }

    /**
     * For own use, make sure the elements got registered in head.
     * @return [DeferredElement], wrapper of the value
     */
    internal fun <T : R> priorReg(id: Identifier, supplier: Supplier<T>): DeferredElement<T> {
        return priorReg(id.toString(), supplier)
    }

    /**
     * For own use, make sure the elements got registered in head.
     * @return [DeferredElement], wrapper of the value
     */
    internal fun <T : R> priorReg(id: Identifier, func: Function<Identifier, T>): DeferredElement<T> {
        return priorReg(id.toString()) { func.apply(id) }
    }

    /**
     * Close the registry, and save all registered element into [entries].
     */
    internal fun close() {
        entries = ImmutableMap.builderWithExpectedSize<String, R>(priorRegistry!!.size + registry!!.size).apply {
            for ((id, deferred) in priorRegistry!!) put(id, deferred.invoke())
            for ((id, deferred) in registry!!) put(id, deferred.invoke())
        }.build()
        keys = entries.keys
        values = (entries.values as ImmutableList<R>)
        priorRegistry = null
        registry = null
    }
}