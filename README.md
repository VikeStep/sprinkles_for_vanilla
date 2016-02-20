sprinkles_for_vanilla
=====================

This is a mod that adds a config for vanilla. An example config is shown in [sprinkles_for_vanilla.cfg](https://github.com/VikeStep/sprinkles_for_vanilla/blob/master/sprinkles_for_vanilla.cfg).

If you have any feature requests please make an issue on the github with the tag 'enhancement' and I will look into it. If you don't have a github account you can PM me on reddit/FTB Forums/curseforge/MC Forums.

Current Features
---------------------
This mod has config syncing which means that clients will always use the config values from the server except for client only configs

Features that will work even if the mod is not on the server (will even work if server doesnt have forge):

- Disable any sound (including from other mods)
- Option to automatically respawn on death without pressing respawn button
- Disable certain vanilla particles

Must be on server for these to work:

- Disable/Enable Ender Pearl teleportation

New Features I wish to add
---------------------
- GUI Config without needing to restart world

Features which aren't ported from 1.7.10 yet and implementation details
---------------------
- Beacon base block whitelist (Requires Forge PR, overwrite Block.isBeaconBase)
- Crops/Sapling light level requirement (Requires Forge PR, overwrite updateTick)
- Zombies/Skeletons burning in sunlight (Requires Forge PR, overwrite onLivingUpdate)
- Dimensions where water and lava make obsidian/cobble (Requires Forge PR, overwrite BlockLiquid.checkForMixing)
- Choose whether you can see the Christmas Chest texture (Access Transformer on TileEntityChestRenderer.isChristams)
- Toggle whether player keeps their hunger/XP on respawn (Store values with IExtendedEntityProperties and update on death/respawn)
- Water in the nether (Access Transformer for WorldProvider.isHellWorld)
- Punch Damage multiplier (LivingHurtEvent)
- Minimum Hunger to sprint
- Nether/End spawnpoint setting
- Default spawn points for each dimension
- Whether beacon should check for sunlight
- Flammability of Blocks
- Light value of Blocks
- Whether certain damage sources deal damage to ALL, PLAYERS, or NONE
- Individual Mob Griefing types
- Disable Nether/End teleportation.
- Zombie Pigmen spawn rate at nether portal in overworld
- Enable/Disable Sleep
- Beds setting spawn
- Sleeping in other dimensions
- Nearby mobs cancelling sleep
- Nearby mob radius for cancelling sleep
- Distance from bed cancelling sleep
- Time to sleep
- Whether certain mobs can be spawned
- Biomes, Heights, and Rates of Mob spawns
- Spawn radius around players for mobs
- Disable certain explosions
- Villager Trades

Features which aren't being ported from 1.7.10
---------------------
- Player keeps health on respawn (Don't know why I ever added this)
- Spawn Fuzz (Part of forge config)
- Radius for distance for cancelling sleeping in bed (This config is pretty much useless)