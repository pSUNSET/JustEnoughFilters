package net.psunset.jef.registry

import com.google.common.collect.ImmutableList
import com.google.common.collect.ImmutableMap
import com.google.common.collect.ImmutableSet
import net.minecraft.resources.ResourceLocation

/**
 * Calling [register] is only allowed before `ClientSetupEvent`.
 * Getting [entries] is always allowed while [keys] and [values] is only allowed after `ClientSetupEvent`.
 */
class JefRegistry<R : Any> {

    /**
     * Will be `null` after `ClientSetupEvent` fired.
     */
    private var registry: LinkedHashMap<String, R>? = linkedMapOf()

    /**
     * Will be init after `ClientSetupEvent` fired.
     */
    private lateinit var readonlyRegistry: ImmutableMap<String, R>

    /**
     * Out of order
     */
    private val ids: MutableSet<String> = hashSetOf()

    /**
     * Gets real-time registered elements
     */
    val entries: Map<String, R> get() = registry ?: readonlyRegistry

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
     * @return [value]
     */
    internal fun <T : R> register(id: String, value: T): T {
        if (registry == null) {
            throw IllegalStateException("Registry is already closed.")
        }
        if (ids.contains(id)) {
            throw IllegalArgumentException("Element with id $id already exists.")
        }
        ids.add(id)
        registry!![id] = value
        return value
    }

    /**
     * @return [value]
     */
    fun <T : R> register(id: ResourceLocation, value: T): T {
        return register(id.toString(), value)
    }

    fun close() {
        readonlyRegistry = ImmutableMap.copyOf(registry!!)
        keys = readonlyRegistry.keys
        values = (readonlyRegistry.values as ImmutableList<R>)
        registry = null
    }
}