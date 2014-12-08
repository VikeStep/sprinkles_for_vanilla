package com.vikestep.sprinklesforvanilla.common.reference;

public class Settings
{
    //Overhauls
    public static boolean overhaulSleep = true;

    //Sleep
    public static boolean sleepIsEnabled = true;
    public static boolean bedSetsSpawn = true;
    public static boolean nearbyMobsCancelSleep = true;
    public static boolean dayCancelsSleep = true;
    public static boolean playerMustSleepInOverworld = true;
    public static boolean distanceFromBedCancelsSleep = true;
    public static int[] nearbyMobDistance = {8, 5, 8}; //{x, y, z}
    public static int[] distanceFromBed = {3, 2, 3}; //{x, y, z}
    public static int timeToSleep = 100; //Max is 100 Ticks
}
