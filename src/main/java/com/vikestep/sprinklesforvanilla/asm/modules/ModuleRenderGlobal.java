package com.vikestep.sprinklesforvanilla.asm.modules;

import com.vikestep.sprinklesforvanilla.asm.hooks.HookRenderGlobal;
import com.vikestep.sprinklesforvanilla.common.util.LogHelper;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.*;
import squeek.asmhelper.ASMHelper;

import static org.objectweb.asm.Opcodes.*;

public class ModuleRenderGlobal
{
    private static final String SPAWN_PARTICLE_DEOBF     = "doSpawnParticle";
    private static final String SPAWN_PARTICLE_OBF       = "b";
    private static final String SPAWN_PARTICLE_DEOBF_SIG = "(Ljava/lang/String;DDDDDD)Lnet/minecraft/client/particle/EntityFX;";
    private static final String SPAWN_PARTICLE_OBF_SIG   = "(Ljava/lang/String;DDDDDD)Lbkm;";

    public static byte[] transform(byte[] portalClass, boolean isObfuscated)
    {
        LogHelper.log("Transforming RenderGlobal Class");

        ClassNode classNode = ASMHelper.readClassFromBytes(portalClass);

        MethodNode methodNode = ASMHelper.findMethodNodeOfClass(classNode, isObfuscated ? SPAWN_PARTICLE_OBF : SPAWN_PARTICLE_DEOBF, isObfuscated ? SPAWN_PARTICLE_OBF_SIG : SPAWN_PARTICLE_DEOBF_SIG);
        if (methodNode != null)
        {
            transformDoSpawnParticle(methodNode, isObfuscated);
        }

        return ASMHelper.writeClassToBytes(classNode);
    }

    /*
    FROM: if (this.mc != null && this.mc.renderViewEntity != null && this.mc.effectRenderer != null)
    TO: if (this.mc != null && HookRenderGlobal.isParticleAllowed(p_72726_1_) && this.mc.renderViewEntity != null && this.mc.effectRenderer != null)
     */
    private static void transformDoSpawnParticle(MethodNode method, boolean isObfuscated)
    {
        //This will grab the first IFNULL JumpInsnNode
        JumpInsnNode injectNode = (JumpInsnNode) ASMHelper.findFirstInstructionWithOpcode(method, IFNULL);
        LabelNode injectNodeLabel = injectNode.label;

        //HookRenderGlobal.particleIsAllowed(p_72726_1_)
        InsnList toInject = new InsnList();
        toInject.add(new VarInsnNode(ALOAD, 1));
        toInject.add(new MethodInsnNode(INVOKESTATIC, Type.getInternalName(HookRenderGlobal.class), "particleIsAllowed", "(Ljava/lang/String;)Z", false));
        toInject.add(new JumpInsnNode(IFEQ, injectNodeLabel));

        method.instructions.insert(injectNode, toInject);
    }
}
