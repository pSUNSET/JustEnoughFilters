package net.psunset.jef.registry

/**
 * Registry is always open.
 */
@Suppress("UNCHECKED_CAST")
class JefNoKeyRegistry<R : Any> {

    private val registry: MutableSet<R> = hashSetOf()

    /**
     * Gets real-time registered elements
     */
    val entries: Set<R> get() = registry

    /**
     * @return [value]
     */
    fun <T : R> register(value: T): T {
        registry.add(value)
        return value
    }
}