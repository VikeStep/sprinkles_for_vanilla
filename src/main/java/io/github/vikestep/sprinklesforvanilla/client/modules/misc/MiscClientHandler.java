package io.github.vikestep.sprinklesforvanilla.client.modules.misc;

import io.github.vikestep.sprinklesforvanilla.common.modules.DefaultProperty;
import io.github.vikestep.sprinklesforvanilla.common.modules.IProperty;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGameOver;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class MiscClientHandler
{
    public static final IProperty shouldSkipRespawnScreen;

    static
    {
        shouldSkipRespawnScreen = new DefaultProperty(IProperty.Type.BOOLEAN, // type
                "shouldSkipRespawnScreenOnDeath", // name
                "Set to true to automatically respawn on death, and false to show the respawn screen to manually respawn", // comment
                false, // requiresWorldRestart
                false, // requiresMcRestart
                false // defaultValue
        );
    }

    private static boolean hasClicked = false;

    @SuppressWarnings("UnusedDeclaration")
    @SubscribeEvent
    public void onGuiOpen(GuiOpenEvent event)
    {
        if (!(Boolean)shouldSkipRespawnScreen.getValue())
        {
            return;
        }
        GuiScreen gui = event.gui;
        boolean isGameOver = gui instanceof GuiGameOver;
        if (isGameOver && !hasClicked)
        {
            Minecraft mc = Minecraft.getMinecraft();
            if (!mc.theWorld.getWorldInfo().isHardcoreModeEnabled())
            {
                hasClicked = true;
                mc.thePlayer.respawnPlayer();
                mc.displayGuiScreen(null);
                event.setCanceled(true);
            }
        }
        else if (!isGameOver && hasClicked)
        {
            hasClicked = false;
        }
    }
}
