package net.psunset.jef.registry

/**
 * Registry is always open.
 */
@Suppress("UNCHECKED_CAST")
class JefNoKeyRegistry<T : Any> {

    private val registry: MutableSet<T> = hashSetOf()

    /**
     * Gets real-time registered elements
     */
    val entries: Set<T> get() = registry

    /**
     * @return [value]
     */
    fun register(value: T): T {
        registry.add(value)
        return value
    }
}