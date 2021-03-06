package io.github.vikestep.sprinklesforvanilla.common.init;

import com.google.common.math.IntMath;
import com.google.common.primitives.Ints;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import io.github.vikestep.sprinklesforvanilla.common.configuration.Settings;
import io.github.vikestep.sprinklesforvanilla.common.utils.LogHelper;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.passive.EntityAmbientCreature;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityWaterMob;
import net.minecraft.world.biome.BiomeGenBase;

import java.util.*;

public class InitMobRegistry
{
    private static final String[] commands = {"add", "remove", "modify"};

    public static Map<Class<? extends EntityLiving>, BiomeGenBase[]> modificationMap = new HashMap<Class<? extends EntityLiving>, BiomeGenBase[]>();
    public static Map<Integer, Map<EnumCreatureType, Integer>>       heightMap       = new HashMap<Integer, Map<EnumCreatureType, Integer>>();
    public static Map<Integer, Map<EnumCreatureType, Integer>>       rateMap         = new HashMap<Integer, Map<EnumCreatureType, Integer>>();

    public static int gcdPassiveSpawn = 400;

    @SuppressWarnings("unchecked")
    public static void init()
    {
        for (String entry : Settings.mobSpawnRulesModifications[1])
        {
            String[] command = entry.split(": ");
            if (command.length != 2 || !Arrays.asList(commands).contains(command[0]) || entry.startsWith("#"))
            {
                continue;
            }
            String[] args = command[1].split(", ");
            int biomeListIndex = -1;
            for (int i = 0; i < args.length; i++)
            {
                String argument = args[i];
                if (argument.startsWith("{"))
                {
                    biomeListIndex = i;
                    break;
                }
            }
            if (biomeListIndex == -1)
            {
                continue;
            }
            String[] spawnArguments = Arrays.copyOfRange(args, 0, biomeListIndex);
            String[] biomes = Arrays.copyOfRange(args, biomeListIndex, args.length);
            biomes[0] = biomes[0].substring(1, biomes[0].length());
            biomes[biomes.length-1] = biomes[biomes.length-1].substring(0, biomes[biomes.length-1].length()-1);
            if (spawnArguments.length == 4 || (command[0].equals("remove") && spawnArguments.length == 1))
            {
                try
                {
                    Class<? extends EntityLiving> entityClass = (Class) EntityList.stringToClassMapping.get(spawnArguments[0]);
                    int weight = 0;
                    int min = 0;
                    int max = 0;
                    if (!command[0].equals("remove"))
                    {
                        weight = Integer.parseInt(spawnArguments[1]);
                        min = Integer.parseInt(spawnArguments[2]);
                        max = Integer.parseInt(spawnArguments[3]);
                    }
                    EnumCreatureType creatureType;
                    if (IMob.class.isAssignableFrom(entityClass))
                    {
                        creatureType = EnumCreatureType.MONSTER;
                    }
                    else if (EntityAnimal.class.isAssignableFrom(entityClass))
                    {
                        creatureType = EnumCreatureType.CREATURE;
                    }
                    else if (EntityAmbientCreature.class.isAssignableFrom(entityClass))
                    {
                        creatureType = EnumCreatureType.AMBIENT;
                    }
                    else if (EntityWaterMob.class.isAssignableFrom(entityClass))
                    {
                        creatureType = EnumCreatureType.WATER_CREATURE;
                    }
                    else
                    {
                        continue;
                    }
                    List<BiomeGenBase> biomeGenBases = new ArrayList<BiomeGenBase>();
                    for (String biome : biomes)
                    {
                        for (BiomeGenBase biomeGenBase : BiomeGenBase.getBiomeGenArray())
                        {
                            if (biomeGenBase == null)
                            {
                                continue;
                            }
                            String b = biomeGenBase.biomeName;
                            if (b.equals(biome) || (biome.equals("Overworld") && !b.equals("Hell")))
                            {
                                biomeGenBases.add(biomeGenBase);
                            }
                        }
                    }
                    BiomeGenBase[] biomeGenBaseArray = biomeGenBases.toArray(new BiomeGenBase[biomeGenBases.size()]);
                    switch (Arrays.asList(commands).indexOf(command[0]))
                    {
                        case 0:
                            //ADD
                            EntityRegistry.addSpawn(entityClass, weight, min, max, creatureType, biomeGenBaseArray);
                            modificationMap.put(entityClass, biomeGenBaseArray);
                            break;
                        case 1:
                            //REMOVE
                            EntityRegistry.removeSpawn(entityClass, creatureType, biomeGenBaseArray);
                            break;
                        case 2:
                            //MODIFY
                            EntityRegistry.removeSpawn(entityClass, creatureType, biomeGenBaseArray);
                            EntityRegistry.addSpawn(entityClass, weight, min, max, creatureType, biomeGenBaseArray);
                            break;
                    }
                }
                catch (Exception e)
                {
                    LogHelper.warn("Invalid Spawn Setting: " + entry);
                    e.printStackTrace();
                }
            }
        }
        for (String entry : Settings.mobSpawnHeightRules[1])
        {
            try
            {
                if (entry.startsWith("#"))
                {
                    continue;
                }
                String[] a = entry.split("\\{");
                String[] b = a[0].split(", ");
                String[] c = a[1].split("\\}")[0].split(", ");
                for (String dim : c)
                {
                    Integer dimID = Integer.parseInt(dim);
                    Integer height = Integer.parseInt(b[1]);
                    EnumCreatureType type = getCreatureFromString(b[0]);
                    if (type == null)
                    {
                        LogHelper.warn(b[0] + " is an invalid mob type");
                        continue;
                    }
                    Map<EnumCreatureType, Integer> map = heightMap.get(dimID);
                    if (map == null)
                    {
                        map = new HashMap<EnumCreatureType, Integer>();
                    }
                    map.put(type, height);
                    heightMap.put(dimID, map);
                }
            }
            catch (Exception e)
            {
                LogHelper.warn("Invalid Spawn Setting: " + entry);
                e.printStackTrace();
            }
        }
        List<Integer> rates = new ArrayList<Integer>();
        for (String entry : Settings.mobSpawnRateRules[1])
        {
            try
            {
                if (entry.startsWith("#"))
                {
                    continue;
                }
                String[] a = entry.split("\\{");
                String[] b = a[0].split(", ");
                String[] c = a[1].split("\\}")[0].split(", ");
                for (String dim : c)
                {
                    Integer dimID = Integer.parseInt(dim);
                    Integer rate = Integer.parseInt(b[1]);
                    EnumCreatureType type = getCreatureFromString(b[0]);
                    if (type == null)
                    {
                        LogHelper.warn(b[0] + " is an invalid mob type");
                        continue;
                    }
                    if (type == EnumCreatureType.CREATURE)
                    {
                        rates.add(rate);
                    }
                    Map<EnumCreatureType, Integer> map = rateMap.get(dimID);
                    if (map == null)
                    {
                        map = new HashMap<EnumCreatureType, Integer>();
                    }
                    map.put(type, rate);
                    rateMap.put(dimID, map);
                }
            }
            catch (Exception e)
            {
                LogHelper.warn("Invalid Spawn Setting: " + entry);
                e.printStackTrace();
            }
        }
        if (rates.size() > 0)
        {
            int[] r = Ints.toArray(rates);
            int result = r[0];
            for(int i = 1; i < r.length; i++){
                result = IntMath.gcd(result, r[i]);
            }
            gcdPassiveSpawn = result;
        }
    }

    private static EnumCreatureType getCreatureFromString(String type)
    {
        if (type.toLowerCase().equals("creature"))
        {
            return EnumCreatureType.CREATURE;
        }
        else if (type.toLowerCase().equals("monster"))
        {
            return EnumCreatureType.MONSTER;
        }
        else if (type.toLowerCase().equals("ambient"))
        {
            return EnumCreatureType.AMBIENT;
        }
        else if (type.toLowerCase().equals("watercreature"))
        {
            return EnumCreatureType.WATER_CREATURE;
        }
        return null;
    }
}
