package net.psunset.jef.tool

object CatchingUtl {
    @JvmStatic
    fun isValidRegex(re: String): Boolean {
        return runCatching { Regex(re) }.isSuccess
    }

    @JvmStatic
    fun isValidClass(cls: String): Boolean {
        return runCatching { Class.forName(cls) }.isSuccess
    }
}