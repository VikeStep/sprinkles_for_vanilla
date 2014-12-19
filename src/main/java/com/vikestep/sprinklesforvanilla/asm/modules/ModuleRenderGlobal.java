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

    private static final String PLAY_AUX_SFX_DEOBF     = "playAuxSFX";
    private static final String PLAY_AUX_SFX_OBF       = "a";
    private static final String PLAY_AUX_SFX_DEOBF_SIG = "(Lnet/minecraft/entity/player/EntityPlayer;IIIII)V";
    private static final String PLAY_AUX_SFX_OBF_SIG   = "(Lyz;IIIII)V";

    public static byte[] transform(byte[] renderGlobalClass, boolean isObfuscated)
    {
        LogHelper.log("Transforming RenderGlobal Class");

        ClassNode classNode = ASMHelper.readClassFromBytes(renderGlobalClass);

        MethodNode methodNode = ASMHelper.findMethodNodeOfClass(classNode, isObfuscated ? SPAWN_PARTICLE_OBF : SPAWN_PARTICLE_DEOBF, isObfuscated ? SPAWN_PARTICLE_OBF_SIG : SPAWN_PARTICLE_DEOBF_SIG);
        if (methodNode != null)
        {
            transformDoSpawnParticle(methodNode, isObfuscated);
        }

        methodNode = ASMHelper.findMethodNodeOfClass(classNode, isObfuscated ? PLAY_AUX_SFX_OBF : PLAY_AUX_SFX_DEOBF, isObfuscated ? PLAY_AUX_SFX_OBF_SIG : PLAY_AUX_SFX_DEOBF_SIG);
        if (methodNode != null)
        {
            transformPlayAusSFX(methodNode, isObfuscated);
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

    private static void transformPlayAusSFX(MethodNode method, boolean isObfuscated)
    {
        //This will be (ALOAD, 0) so we inject before it
        AbstractInsnNode injectNode = ASMHelper.findPreviousInstructionWithOpcode(ASMHelper.findFirstInstructionWithOpcode(method, ISHR), GETFIELD).getPrevious().getPrevious();
        LabelNode ifParticleNode = new LabelNode();
        AbstractInsnNode endIfNode = ASMHelper.findNextInstructionWithOpcode(injectNode, INVOKEVIRTUAL);

        InsnList toInject = new InsnList();
        toInject.add(new LdcInsnNode("blockBreak"));
        toInject.add(new MethodInsnNode(INVOKESTATIC, Type.getInternalName(HookRenderGlobal.class), "particleIsAllowed", "(Ljava/lang/String;)Z", false));
        toInject.add(new JumpInsnNode(IFEQ, ifParticleNode));

        method.instructions.insertBefore(injectNode, toInject);
        method.instructions.insert(endIfNode, ifParticleNode);
    }
}
