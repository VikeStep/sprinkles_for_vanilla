package com.vikestep.sprinklesforvanilla.asm;

import com.vikestep.sprinklesforvanilla.asm.modules.ModuleBlockPortal;
import com.vikestep.sprinklesforvanilla.asm.modules.ModuleRenderGlobal;
import net.minecraft.launchwrapper.IClassTransformer;

public class SprinklesForVanillaTransformer implements IClassTransformer
{
    private static final String BLOCK_PORTAL_DEOBF = "net.minecraft.block.BlockPortal";
    private static final String BLOCK_PORTAL_OBF   = "amp";

    private static final String RENDER_GLOBAL_DEOBF = "net.minecraft.client.renderer.RenderGlobal";
    private static final String RENDER_GLOBAL_OBF   = "bma";

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
        return transformingClass;
    }
}
