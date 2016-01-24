package io.github.vikestep.sprinklesforvanilla.common.modules.items;

import io.github.vikestep.sprinklesforvanilla.common.modules.DefaultProperty;
import io.github.vikestep.sprinklesforvanilla.common.modules.IProperty;
import io.github.vikestep.sprinklesforvanilla.common.modules.IProperty.Type;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class EnderPearlHandler
{
    public static final IProperty doEnderPearlsTeleport;

    static
    {
        doEnderPearlsTeleport = new DefaultProperty(Type.BOOLEAN, // type
                "doEnderPearlsTeleport", // name
                "Setting this to false will disable ender pearls from teleporting", // comment
                false, // requiresWorldRestart
                false, // requiresMcRestart
                true // defaultValue
        );
    }

    @SuppressWarnings("UnusedDeclaration")
    @SubscribeEvent
    public void onPlayerInteract(PlayerInteractEvent e)
    {
        if (!(Boolean) doEnderPearlsTeleport.getValue() && !e.isCanceled() && e.action != PlayerInteractEvent.Action.LEFT_CLICK_BLOCK)
        {
            ItemStack item = e.entityPlayer.getCurrentEquippedItem();
            if (item != null && item.getItem() == Items.ender_pearl)
            {
                e.setCanceled(true);
            }
        }
    }

}
