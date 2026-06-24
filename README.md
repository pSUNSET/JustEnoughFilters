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

All available registries are in `JefRegistries`.  
You can register the element with `JefRegistries.ONE_OF_REGISTRIES.register()` function.  
Registration must be completed when your mod get initialized.

## Acknowledgements

This mod is inspired by the filter feature
in [Not Enough Updates](https://github.com/NotEnoughUpdates/NotEnoughUpdates) (NEU) mod.
