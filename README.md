sprinkles_for_vanilla
=====================

This is a mod that adds a config for vanilla. An example config is shown in [sprinkles_for_vanilla.cfg](https://github.com/VikeStep/sprinkles_for_vanilla/blob/master/sprinkles_for_vanilla.cfg).

If you have any feature requests please make an issue on the github with the tag 'enhancement' and I will look into it. If you don't have a github account you can PM me on reddit/FTB Forums/curseforge/MC Forums.

This mod also uses ASMHelper by squeek to help me tweak some of these classes. Since this mod uses class transformers it may causes issues with other mods but I have not ran into any major compatibility issues.

This mod has features that will work if not on the serverside, but most of them will require

Current Features
---------------------
This mod has config syncing which means that clients will always use the config values from the server except for client only configs

Features that will work even if the mod is not on the server (will even work if server doesnt have forge):

- Disable any sound (including from other mods)
- Disable/Enable any particle (only from vanilla)
- Choose to hide your own potion effect particles, everyone's particles, or see everyone's
- Choose when to have christmas chest texture

Must be on server for these to work:

- Disable/Enable Ender Pearl teleportation
- Keep hunger on respawn (with minimum value)
- Keep health on respawn (with minimum value)
- Keep XP on respawn
- Disable/Enable water placement in nether
- Dimension blacklist for water and lava to make obsidian or cobblestone
- Disable/Enable Spawn fuzz
- Disable/Enable ability to spawn in nether or end
- Set default spawn in overworld, nether, or end
- Whitelist of blocks which can be used as beacon base
- Disable/Enable different types of damage sources (e.g. cactus, fire, suffocation)
- Disable/Enable the different types of mob griefing
- Disable/Enable nether teleportation
- Disable/Enable creation of portal blocks
- Set multiplier for frequency of zombie pigmen spawning from nether portal
- Disable/Enable Sleep
- Disable/Enable beds setting spawn
- Disable/Enable sleep in other dimensions
- Disable/Enable nearby mobs cancelling sleep or set the area to check for nearby mobs
- Disable/Enable having to be a certain distance from the bed or set area you have to be in
- Set time in ticks to sleep (currently max is 100 ticks which is default)
- Disable/Enable different types of mobs spawning
- Configure mob spawn rules for different biomes (add, modify or remove)
- Disable/Enable all explosions
- Configure values for different types of explosions (including from other mods)

Planned Features
---------------------
- Disable/Enable Weather overall or based on biomes
- Set Frequency/Duration of rain/thunder
- Set which biomes should snow
- Disable End
- Disable/Enable Double Chests being created
- Toggle between full block or chest models
- Disable/Enable different Worldgen features
- Disable/Enable Hunger
- Disable/Enable Sprinting
- Set hunger level required for sprinting
- Set gravity
- Disable/Enable Night
- Set duration of day/night cycle
- Disable/Enable Potion Effects
- Various EXP related configs
- Enchantment related configs
- Armor related configs
- Set Smelting/Brewing Duration
- Set fuel supplied for different items in the furnace
- Enable/Disable Achievements
- Set Mob drops and chances
- Set a Respawn Time (useful for hardcore servers)
- Ability to set speed at which spawners spawn
- Set health regeneration rate
- Disable/Enable enchanting, brewing and using the anvil
- Disable/Enable being able to press F3 or toggle what is shown in F3
- Disable/Enable ability to boost minecarts
- Ability to force a gamerule
- In-Game Config Editor and command to reload configs
- Configure flammability of blocks