package com.vikestep.sprinklesforvanilla.asm.modules;

import com.vikestep.sprinklesforvanilla.asm.hooks.HookEntityLivingBase;
import com.vikestep.sprinklesforvanilla.common.util.LogHelper;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.*;
import squeek.asmhelper.ASMHelper;
import squeek.asmhelper.ObfRemappingClassWriter;

import static org.objectweb.asm.Opcodes.*;

public class ModuleEntityLivingBase
{
    private static final String UPDATE_POTION_EFFECTS_DEOBF = "updatePotionEffects";
    private static final String UPDATE_POTION_EFFECTS_OBF = "aO";

    public static byte[] transform(byte[] entityLivingBaseClass, boolean isObfuscated)
    {
        LogHelper.log("Transforming EntityLivingBase Class");

        ClassNode classNode = ASMHelper.readClassFromBytes(entityLivingBaseClass);

        MethodNode methodNode = ASMHelper.findMethodNodeOfClass(classNode, isObfuscated ? UPDATE_POTION_EFFECTS_OBF : UPDATE_POTION_EFFECTS_DEOBF, "()V");
        if (methodNode != null)
        {
            transformUpdatePotionEffects(methodNode, isObfuscated);
        }

        //Don't Compute Frames: http://www.minecraftforum.net/forums/mapping-and-modding/minecraft-mods/modification-development/1435725
        ClassWriter writer = new ObfRemappingClassWriter(ClassWriter.COMPUTE_MAXS);
        classNode.accept(writer);
        return writer.toByteArray();
    }

    //Inserts HookEntityLivingBase.particleSpawnedFromEntity(this, flag ? "mobSpellAmbient" : "mobSpell") after if statement
    private static void transformUpdatePotionEffects(MethodNode method, boolean isObfuscated)
    {
        JumpInsnNode ifSpawnParticleNode = (JumpInsnNode) ASMHelper.findLastInstructionWithOpcode(method, IFLE);

        InsnList toInject = new InsnList();
        toInject.add(new VarInsnNode(ALOAD, 0)); //this
        toInject.add(new VarInsnNode(ILOAD, 3)); //flag
        LabelNode particleTypeEnd = new LabelNode();
        toInject.add(new JumpInsnNode(IFEQ, particleTypeEnd));
        toInject.add(new LdcInsnNode("mobSpellAmbient"));
        LabelNode startHook = new LabelNode();
        toInject.add(new JumpInsnNode(GOTO, startHook));
        toInject.add(particleTypeEnd);
        toInject.add(new LdcInsnNode("mobSpell"));
        toInject.add(startHook);
        toInject.add(new MethodInsnNode(INVOKESTATIC, Type.getInternalName(HookEntityLivingBase.class), "particleSpawnedFromEntity", isObfuscated ? "(Lsv;Ljava/lang/String;)V" : "(Lnet/minecraft/entity/EntityLivingBase;Ljava/lang/String;)V", false));

        method.instructions.insert(ifSpawnParticleNode, toInject);
    }
}
