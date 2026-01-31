# Just Enough Filters (JEF)

A lightweight add-on for **JEI**, **REI**, and **EMI**.
Filtering exact items is just a click away!

![Gameplay Image](/docs/gallery/jei_gameplay.png)

## For Developers

### Depend on JEF

Since we have no own maven repo yet, please use 
[Curse Maven](https://www.cursemaven.com/) or
[Modrinth Maven](https://support.modrinth.com/en/articles/8801191-modrinth-maven) instead.  

Also, Most API in JEF is still not mature yet, but following parts are already usable.

### Registering New Filters

You can register your own filters by extending `ToggledFilter` and calling `FilterManager.registerFilter()`.

### Refresh Proxies

JEF uses `IFilterProxy` to notify item viewers when they need to refresh their ingredient lists.
If you are integrating a new item viewer,
implement this interface and register it via `FilterManager.registerProxy()`.

## Acknowledgements

This mod is inspired by the filter feature
in [Not Enough Updates](https://github.com/NotEnoughUpdates/NotEnoughUpdates) (NEU) mod.
