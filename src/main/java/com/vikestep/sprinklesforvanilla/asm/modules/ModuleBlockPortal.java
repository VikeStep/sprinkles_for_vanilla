package com.vikestep.sprinklesforvanilla.asm.modules;

import com.vikestep.sprinklesforvanilla.asm.hooks.HookBlockPortal;
import com.vikestep.sprinklesforvanilla.common.util.LogHelper;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.*;
import squeek.asmhelper.ASMHelper;

import static org.objectweb.asm.Opcodes.*;

public class ModuleBlockPortal
{
    private static final String UPDATE_TICK_DEOBF     = "updateTick";
    private static final String UPDATE_TICK_OBF       = "a";
    private static final String UPDATE_TICK_DEOBF_SIG = "(Lnet/minecraft/world/World;IIILjava/util/Random;)V";
    private static final String UPDATE_TICK_OBF_SIG   = "(Lahb;IIILjava/util/Random;)V";

    private static final String RANDOM_DISPLAY_TICK_DEOBF     = "randomDisplayTick";
    private static final String RANDOM_DISPLAY_TICK_OBF       = "b";
    private static final String RANDOM_DISPLAY_TICK_DEOBF_SIG = "(Lnet/minecraft/world/World;IIILjava/util/Random;)V";
    private static final String RANDOM_DISPLAY_TICK_OBF_SIG   = "(Lahb;IIILjava/util/Random;)V";

    private static final String GET_SIZE_DEOBF     = "func_150000_e";
    private static final String GET_SIZE_OBF       = "e";
    private static final String GET_SIZE_DEOBF_SIG = "(Lnet/minecraft/world/World;III)Z";
    private static final String GET_SIZE_OBF_SIG   = "(Lahb;III)Z";

    private static final String ENTITY_COLLIDE_DEOBF     = "onEntityCollidedWithBlock";
    private static final String ENTITY_COLLIDE_OBF       = "a";
    private static final String ENTITY_COLLIDE_DEOBF_SIG = "(Lnet/minecraft/world/World;IIILnet/minecraft/entity/Entity;)V";
    private static final String ENTITY_COLLIDE_OBF_SIG   = "(Lahb;IIILsa;)V";

    public static byte[] transform(byte[] portalClass, boolean isObfuscated)
    {
        LogHelper.log("Transforming Portal Class");

        ClassNode classNode = ASMHelper.readClassFromBytes(portalClass);

        MethodNode methodNode = ASMHelper.findMethodNodeOfClass(classNode, isObfuscated ? UPDATE_TICK_OBF : UPDATE_TICK_DEOBF, isObfuscated ? UPDATE_TICK_OBF_SIG : UPDATE_TICK_DEOBF_SIG);
        if (methodNode != null)
        {
            transformUpdateTick(methodNode, isObfuscated);
        }

        methodNode = ASMHelper.findMethodNodeOfClass(classNode, isObfuscated ? RANDOM_DISPLAY_TICK_OBF : RANDOM_DISPLAY_TICK_DEOBF, isObfuscated ? RANDOM_DISPLAY_TICK_OBF_SIG : RANDOM_DISPLAY_TICK_DEOBF_SIG);
        if (methodNode != null)
        {
            transformRandomDisplayTick(methodNode, isObfuscated);
        }

        methodNode = ASMHelper.findMethodNodeOfClass(classNode, isObfuscated ? GET_SIZE_OBF : GET_SIZE_DEOBF, isObfuscated ? GET_SIZE_OBF_SIG : GET_SIZE_DEOBF_SIG);
        if (methodNode != null)
        {
            transformGetSize(methodNode, isObfuscated);
        }

        methodNode = ASMHelper.findMethodNodeOfClass(classNode, isObfuscated ? ENTITY_COLLIDE_OBF : ENTITY_COLLIDE_DEOBF, isObfuscated ? ENTITY_COLLIDE_OBF_SIG : ENTITY_COLLIDE_DEOBF_SIG);
        if (methodNode != null)
        {
            transformEntityCollide(methodNode, isObfuscated);
        }

        return ASMHelper.writeClassToBytes(classNode);
    }

    /*
    FROM: if (p_149674_1_.provider.isSurfaceWorld() && p_149674_1_.getGameRules().getGameRuleBooleanValue("doMobSpawning") && p_149674_5_.nextInt(2000) < p_149674_1_.difficultySetting.getDifficultyId())
    TO: if (p_149674_1_.provider.isSurfaceWorld() && p_149674_1_.getGameRules().getGameRuleBooleanValue("doMobSpawning") && HookBlockPortal.spawnZombiePigman(p_149674_1_, p_149674_5_))
    */
    private static void transformUpdateTick(MethodNode method, boolean isObfuscated)
    {
        //This returns the node before we load Random. Should be IFEQ. This gives us a reference point to inject new conditional
        AbstractInsnNode injectPoint = ASMHelper.findFirstInstructionWithOpcode(method, SIPUSH).getPrevious().getPrevious();
        //This returns the node at the end of the conditional we are removing
        AbstractInsnNode endNodeRemoved = ASMHelper.findFirstInstructionWithOpcode(method, IF_ICMPGE);

        //Remove the conditional. The second parameter is not inclusive so we do a getNext() to include our endNode we want removed
        ASMHelper.removeFromInsnListUntil(method.instructions, injectPoint.getNext(), endNodeRemoved.getNext());

        //This is where our replacement conditional will point
        LabelNode newConditionalEndLabel = ((JumpInsnNode) injectPoint).label;

        //HookBlockPortal.spawnZombiePigman(p_149674_1_, p_149674_5_)
        InsnList toInject = new InsnList();
        toInject.add(new VarInsnNode(ALOAD, 1));
        toInject.add(new VarInsnNode(ALOAD, 5));
        toInject.add(new MethodInsnNode(INVOKESTATIC, Type.getInternalName(HookBlockPortal.class), "spawnZombiePigmen", isObfuscated ? "(Lahb;Ljava/util/Random;)Z" : "(Lnet/minecraft/world/World;Ljava/util/Random;)Z", false));
        toInject.add(new JumpInsnNode(IFEQ, newConditionalEndLabel));

        method.instructions.insert(injectPoint, toInject);
    }

    /*
    This function both handles nether sounds and particles
    FROM: if (p_149734_5_.nextInt(100) == 0)
    TO: if (p_149734_5_.nextInt(100) == 0 && HookBlockPortal.netherPortalPlaysSound())

    Then after that conditional we add if (!HookBlockPortal.netherPortalHasParticles()) { return; }
    */
    private static void transformRandomDisplayTick(MethodNode method, boolean isObfuscated)
    {
        //Finds the if statement we are inserting the condition into
        JumpInsnNode ifSoundNode = (JumpInsnNode) ASMHelper.findFirstInstructionWithOpcode(method, IFNE);
        //And the label we will point to
        LabelNode ifSoundEndLabel = ifSoundNode.label;

        //if(HookBlockPortal.netherPortalPlaysSound())
        InsnList toInject = new InsnList();
        toInject.add(new MethodInsnNode(INVOKESTATIC, Type.getInternalName(HookBlockPortal.class), "netherPortalPlaysSound", "()Z", false));
        toInject.add(new JumpInsnNode(IFEQ, ifSoundEndLabel));

        method.instructions.insert(ifSoundNode, toInject);

        //This is the for loop we are inserting before. This ensure it will be after the label node from the previous statement
        AbstractInsnNode forLoopStartNode = ASMHelper.findFirstInstructionWithOpcode(method, ISTORE).getPrevious().getPrevious();

        //if (!HookBlockPortal.netherPortalHasParticles()) { return; }
        toInject = new InsnList();
        toInject.add(new MethodInsnNode(INVOKESTATIC, Type.getInternalName(HookBlockPortal.class), "netherPortalHasParticles", "()Z", false));
        LabelNode newLabel = new LabelNode();
        toInject.add(new JumpInsnNode(IFNE, newLabel));
        toInject.add(new InsnNode(RETURN));
        //This is where the new if statement will jump to if the nether portal does show particles
        toInject.add(newLabel);

        method.instructions.insertBefore(forLoopStartNode, toInject);
    }

    //Inserts if (!HookBlockPortal.portalBlocksAreCreated()) { return false; } on start
    private static void transformGetSize(MethodNode method, boolean isObfuscated)
    {
        AbstractInsnNode firstInstruction = method.instructions.getFirst();
        LabelNode firstLabel = (LabelNode) ASMHelper.getOrFindLabelOrLineNumber(firstInstruction, false);

        //if (HookBlockPortal.portalBlocksAreCreated()) { return false; }
        InsnList toInject = new InsnList();
        toInject.add(new MethodInsnNode(INVOKESTATIC, Type.getInternalName(HookBlockPortal.class), "portalBlocksAreCreated", "()Z", false));
        toInject.add(new JumpInsnNode(IFNE, firstLabel));
        toInject.add(new InsnNode(ICONST_0));
        toInject.add(new InsnNode(IRETURN));

        method.instructions.insertBefore(firstInstruction, toInject);
    }

    /*
    FROM if(p_149670_5_.ridingEntity == null && p_149670_5_.riddenByEntity == null)
    TO if(p_149670_5_.ridingEntity == null && HookBlockPortal.netherIsAllowed() && p_149670_5_.riddenByEntity == null)
    */
    private static void transformEntityCollide(MethodNode method, boolean isObfuscated)
    {
        //Finds the if statement we are inserting the conditional
        AbstractInsnNode isRiding = ASMHelper.findFirstInstructionWithOpcode(method, IFNONNULL);
        //Grabs the label which our conditional will point to
        LabelNode isRidingEndLabel = ((JumpInsnNode) isRiding).label;

        //HookBlockPortal.netherIsAllowed()
        InsnList toInject = new InsnList();
        toInject.add(new MethodInsnNode(INVOKESTATIC, Type.getInternalName(HookBlockPortal.class), "netherPortalTeleports", "()Z", false));
        toInject.add(new JumpInsnNode(IFEQ, isRidingEndLabel));

        method.instructions.insert(isRiding, toInject);
    }
}
