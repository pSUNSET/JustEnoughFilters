# Just Enough Filters (JEF)

A lightweight add-on for **JEI**, **REI**, and **EMI**.
Filtering exact items is just a click away!

![Gameplay Image](/docs/gallery/jei_gameplay.png)

## Downloads

You can download this mod on:
- [CurseForge](https://www.curseforge.com/minecraft/mc-mods/just-enough-filters)
- [Modrinth](https://modrinth.com/mod/just-enough-filters)

## For Developers

### Depend on JEF

Since we have no own maven repo yet, please use 
[Curse Maven](https://www.cursemaven.com/) or
[Modrinth Maven](https://support.modrinth.com/en/articles/8801191-modrinth-maven) instead.  

Also, Most API in JEF is still not mature yet, but following parts are already usable.

### Registering New Toggled Filters

You can register your own filters by implementing `IToggledFilter` and calling `JefRegistries.registerToggledFilter()`.

### Registering New Type Filters

Similar to preceding one.
Create a object implementing `IItemTypeFilter` and call `JefRegistries.registerItemTypeFilter()`.

### Refresh Proxies

JEF uses `IFilterProxy` to notify item viewers when they need to refresh their ingredient lists.
If you are integrating a new item viewer,
implement this interface and register it via `JefRegistries.registerProxy()`.

## Acknowledgements

This mod is inspired by the filter feature
in [Not Enough Updates](https://github.com/NotEnoughUpdates/NotEnoughUpdates) (NEU) mod.
