package net.psunset.jef.config

import com.google.common.reflect.TypeToken
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.stream.JsonReader
import net.psunset.jef.config.element.CustomFilter
import net.psunset.jef.core.FilterManager
import net.psunset.jef.tool.PathUtl
import java.io.FileReader
import java.nio.file.Files
import java.nio.file.Path
import kotlin.io.path.notExists

object ConfigManager {

    @JvmStatic
    private val _customFilters: MutableList<CustomFilter> = mutableListOf()

    @JvmStatic
    val customFilters: List<CustomFilter> get() = _customFilters

    @JvmStatic
    val customFilterEntries: Map<String, CustomFilter> get() = _customFilters.associateBy { it.id.toString() }

    @JvmField
    val CUSTOM_FILTERS_CONFIG_FILE: Path = PathUtl.jefConfigDir().resolve("custom_filters.json")

    @JvmField
    val ACTIVE_FILTERS_CONFIG_FILE: Path = PathUtl.jefConfigDir().resolve("active_filters.json")

    private const val DEFAULT_CUSTOM_FILTERS_CONTENT = "[]"

    private const val DEFAULT_ACTIVE_FILTERS_CONTENT = """[
    "justenoughfilters:entity_blocks",
    "justenoughfilters:food",
    "justenoughfilters:fuels",
    "justenoughfilters:tools",
    "justenoughfilters:armor",
    "justenoughfilters:enchanted_books"
]"""

    @JvmField
    val GSON: Gson = GsonBuilder()
        .setPrettyPrinting()
        .enableComplexMapKeySerialization()
        .registerTypeAdapter(object : TypeToken<CustomFilter>() {}.type, CustomFilter.Adapter())
        .create()

    /**
     * Fired when the mod is loading.
     * Only fired once when mod got init.
     */
    @JvmStatic
    fun onLoading() {
        createMissingFiles()
        updateCustomFilters()
        updateActiveFilters()
    }

    @JvmStatic
    fun saveCustomFilters(newCustomFilters: Collection<CustomFilter>) {
        val type = object : TypeToken<Array<CustomFilter>>() {}.type
        val customFiltersContent = GSON.toJson(newCustomFilters.toTypedArray(), type)
        Files.writeString(CUSTOM_FILTERS_CONFIG_FILE, customFiltersContent)

        updateCustomFilters()
        updateActiveFilters()  // may influence active filters, so update it as well
    }

    @JvmStatic
    fun saveActiveFilters(newActiveFilters: Collection<String>) {
        val type = object : TypeToken<Array<String>>() {}.type
        val activeFiltersContent = GSON.toJson(newActiveFilters.toTypedArray(), type)
        Files.writeString(ACTIVE_FILTERS_CONFIG_FILE, activeFiltersContent)

        updateActiveFilters()
    }

    @JvmStatic
    internal fun createMissingFiles() {
        if (PathUtl.jefConfigDir().notExists()) {
            Files.createDirectory(PathUtl.jefConfigDir())
        }

        if (CUSTOM_FILTERS_CONFIG_FILE.notExists()) {
            Files.writeString(CUSTOM_FILTERS_CONFIG_FILE, DEFAULT_CUSTOM_FILTERS_CONTENT)
        }

        if (ACTIVE_FILTERS_CONFIG_FILE.notExists()) {
            Files.writeString(ACTIVE_FILTERS_CONFIG_FILE, DEFAULT_ACTIVE_FILTERS_CONTENT)
        }
    }

    @JvmStatic
    fun updateCustomFilters() {
        val type = object : TypeToken<Array<CustomFilter>>() {}.type

        val filters = GSON.fromJson<Array<CustomFilter>>(
            JsonReader(FileReader(CUSTOM_FILTERS_CONFIG_FILE.toFile())),
            type
        )

        _customFilters.clear()

        for (filter in filters) {
            if (_customFilters.none { it.id == filter.id }) {
                _customFilters.add(filter)
            }
        }
    }

    @JvmStatic
    fun readActiveFilters(): Array<String> {
        val type = object : TypeToken<Array<String>>() {}.type

        val filters = GSON.fromJson<Array<String>>(
            JsonReader(FileReader(ACTIVE_FILTERS_CONFIG_FILE.toFile())),
            type
        )

        return filters
    }

    @JvmStatic
    fun updateActiveFilters() {
        FilterManager.activateToggledFilters(readActiveFilters())
    }
}