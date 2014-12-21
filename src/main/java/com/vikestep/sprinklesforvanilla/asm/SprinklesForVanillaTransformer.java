package com.vikestep.sprinklesforvanilla.asm;

import com.vikestep.sprinklesforvanilla.asm.modules.ModuleBlockPortal;
import com.vikestep.sprinklesforvanilla.asm.modules.ModuleEntityLivingBase;
import com.vikestep.sprinklesforvanilla.asm.modules.ModuleRenderGlobal;
import com.vikestep.sprinklesforvanilla.asm.modules.ModuleTileEntityChestRenderer;
import net.minecraft.launchwrapper.IClassTransformer;

public class SprinklesForVanillaTransformer implements IClassTransformer
{
    private static final String BLOCK_PORTAL_DEOBF = "net.minecraft.block.BlockPortal";
    private static final String BLOCK_PORTAL_OBF   = "amp";

    private static final String RENDER_GLOBAL_DEOBF = "net.minecraft.client.renderer.RenderGlobal";
    private static final String RENDER_GLOBAL_OBF   = "bma";

    private static final String CHEST_RENDERER_DEOBF = "net.minecraft.client.renderer.tileentity.TileEntityChestRenderer";
    private static final String CHEST_RENDERER_OBF   = "bmm";

    private static final String ENTITY_LIVING_BASE_DEOBF = "net.minecraft.entity.EntityLivingBase";
    private static final String ENTITY_LIVING_BASE_OBF   = "sv";

    @Override
    public byte[] transform(String name, String transformedName, byte[] transformingClass)
    {
        boolean isObfuscated = !name.equals(transformedName);
        if (name.equals(BLOCK_PORTAL_DEOBF) || name.equals(BLOCK_PORTAL_OBF))
        {
            transformingClass = ModuleBlockPortal.transform(transformingClass, isObfuscated);
        }
        else if (name.equals(RENDER_GLOBAL_DEOBF) || name.equals(RENDER_GLOBAL_OBF))
        {
            transformingClass = ModuleRenderGlobal.transform(transformingClass, isObfuscated);
        }
        else if (name.equals(CHEST_RENDERER_DEOBF) || name.equals(CHEST_RENDERER_OBF))
        {
            transformingClass = ModuleTileEntityChestRenderer.transform(transformingClass, isObfuscated);
        }
        else if (name.equals(ENTITY_LIVING_BASE_DEOBF) || name.equals(ENTITY_LIVING_BASE_OBF))
        {
            transformingClass = ModuleEntityLivingBase.transform(transformingClass, isObfuscated);
        }
        return transformingClass;
    }
}
