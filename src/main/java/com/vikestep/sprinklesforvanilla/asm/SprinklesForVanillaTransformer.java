package com.vikestep.sprinklesforvanilla.asm;

import com.vikestep.sprinklesforvanilla.common.util.LogHelper;
import net.minecraft.launchwrapper.IClassTransformer;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.*;
import squeek.asmhelper.ASMHelper;
import squeek.asmhelper.ObfRemappingClassWriter;

import static org.objectweb.asm.Opcodes.*;

import java.util.Arrays;

public class SprinklesForVanillaTransformer implements IClassTransformer
{
    public static final String[] classesBeingTransformed =
            {
                    "net.minecraft.block.BlockPortal",
                    "net.minecraft.client.renderer.RenderGlobal",
                    "net.minecraft.client.renderer.tileentity.TileEntityChestRenderer",
                    "net.minecraft.entity.EntityLivingBase"
            };

    @Override
    public byte[] transform(String name, String transformedName, byte[] transformingClass)
    {
        boolean isObfuscated = !name.equals(transformedName);
        int index = Arrays.asList(classesBeingTransformed).indexOf(transformedName);
        return index != -1 ? transform(index, transformingClass, isObfuscated) : transformingClass;
    }

    private static byte[] transform(int index, byte[] transformingClass, boolean isObfuscated)
    {
        LogHelper.log("Transforming Class: " + classesBeingTransformed[index]);
        try
        {
            ClassNode classNode = ASMHelper.readClassFromBytes(transformingClass);
            switch(index)
            {
                case 0:
                    transformBlockPortal(classNode, isObfuscated);
                    break;
                case 1:
                    transformRenderGlobal(classNode, isObfuscated);
                    break;
                case 2:
                    transformTEChestRenderer(classNode, isObfuscated);
                    break;
                case 3:
                    transformEntityLivingBase(classNode, isObfuscated);
                    //Need to write to byte array without computing frames to avoid an error. No-one knows why but it works.
                    ClassWriter writer = new ObfRemappingClassWriter(ClassWriter.COMPUTE_MAXS);
                    classNode.accept(writer);
                    return writer.toByteArray();
            }
            return ASMHelper.writeClassToBytes(classNode);
        }
        catch (Exception e)
        {
            LogHelper.log("Something went wrong trying to transform " + classesBeingTransformed[index]);
            e.printStackTrace();
        }
        return transformingClass;
    }

    private static void transformBlockPortal(ClassNode blockPortalClass, boolean isObfuscated)
    {
        final String UPDATE_TICK             = isObfuscated ? "a"                             : "updateTick";
        final String RANDOM_DISPLAY_TICK     = isObfuscated ? "b"                             : "randomDisplayTick";
        final String GET_SIZE                = isObfuscated ? "e"                             : "func_150000_e";
        final String ENTITY_COLLIDE          = isObfuscated ? "a"                             : "onEntityCollidedWithBlock";
        final String UPDATE_TICK_SIG         = isObfuscated ? "(Lahb;IIILjava/util/Random;)V" : "(Lnet/minecraft/world/World;IIILjava/util/Random;)V";
        final String RANDOM_DISPLAY_TICK_SIG = isObfuscated ? "(Lahb;IIILjava/util/Random;)V" : "(Lnet/minecraft/world/World;IIILjava/util/Random;)V";
        final String GET_SIZE_SIG            = isObfuscated ? "(Lahb;III)Z"                   : "(Lnet/minecraft/world/World;III)Z";
        final String ENTITY_COLLIDE_SIG      = isObfuscated ? "(Lahb;IIILsa;)V"               : "(Lnet/minecraft/world/World;IIILnet/minecraft/entity/Entity;)V";

        for (MethodNode method : blockPortalClass.methods)
        {
            if (method.name.equals(UPDATE_TICK) && method.desc.equals(UPDATE_TICK_SIG))
            {
                /*
                FROM: if (p_149674_1_.provider.isSurfaceWorld() && p_149674_1_.getGameRules().getGameRuleBooleanValue("doMobSpawning") && p_149674_5_.nextInt(2000) < p_149674_1_.difficultySetting.getDifficultyId())
                TO: if (p_149674_1_.provider.isSurfaceWorld() && p_149674_1_.getGameRules().getGameRuleBooleanValue("doMobSpawning") && HookBlockPortal.spawnZombiePigman(p_149674_1_, p_149674_5_))
                */

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
                toInject.add(new MethodInsnNode(INVOKESTATIC, Type.getInternalName(Hooks.class), "spawnZombiePigmen", isObfuscated ? "(Lahb;Ljava/util/Random;)Z" : "(Lnet/minecraft/world/World;Ljava/util/Random;)Z", false));
                toInject.add(new JumpInsnNode(IFEQ, newConditionalEndLabel));

                method.instructions.insert(injectPoint, toInject);
            }
            else if (method.name.equals(RANDOM_DISPLAY_TICK) && method.desc.equals(RANDOM_DISPLAY_TICK_SIG))
            {
                /*
                FROM: if (p_149734_5_.nextInt(100) == 0)
                TO: if (p_149734_5_.nextInt(100) == 0 && HookBlockPortal.netherPortalPlaysSound())
                */

                //Finds the if statement we are inserting the condition into
                JumpInsnNode ifSoundNode = (JumpInsnNode) ASMHelper.findFirstInstructionWithOpcode(method, IFNE);
                //And the label we will point to
                LabelNode ifSoundEndLabel = ifSoundNode.label;

                //if(HookBlockPortal.netherPortalPlaysSound())
                InsnList toInject = new InsnList();
                toInject.add(new MethodInsnNode(INVOKESTATIC, Type.getInternalName(Hooks.class), "netherPortalPlaysSound", "()Z", false));
                toInject.add(new JumpInsnNode(IFEQ, ifSoundEndLabel));

                method.instructions.insert(ifSoundNode, toInject);
            }
            else if (method.name.equals(GET_SIZE) && method.desc.equals(GET_SIZE_SIG))
            {
                //Inserts if (!HookBlockPortal.portalBlocksAreCreated()) { return false; } on method start

                AbstractInsnNode firstInstruction = method.instructions.getFirst();
                LabelNode firstLabel = (LabelNode) ASMHelper.getOrFindLabelOrLineNumber(firstInstruction, false);

                //if (HookBlockPortal.portalBlocksAreCreated()) { return false; }
                InsnList toInject = new InsnList();
                toInject.add(new MethodInsnNode(INVOKESTATIC, Type.getInternalName(Hooks.class), "portalBlocksAreCreated", "()Z", false));
                toInject.add(new JumpInsnNode(IFNE, firstLabel));
                toInject.add(new InsnNode(ICONST_0));
                toInject.add(new InsnNode(IRETURN));

                method.instructions.insertBefore(firstInstruction, toInject);
            }
            else if (method.name.equals(ENTITY_COLLIDE) && method.desc.equals(ENTITY_COLLIDE_SIG))
            {
                /*
                FROM if(p_149670_5_.ridingEntity == null && p_149670_5_.riddenByEntity == null)
                TO if(p_149670_5_.ridingEntity == null && HookBlockPortal.netherIsAllowed() && p_149670_5_.riddenByEntity == null)
                */

                //Finds the if statement we are inserting the conditional
                AbstractInsnNode isRiding = ASMHelper.findFirstInstructionWithOpcode(method, IFNONNULL);
                //Grabs the label which our conditional will point to
                LabelNode isRidingEndLabel = ((JumpInsnNode) isRiding).label;

                //HookBlockPortal.netherIsAllowed()
                InsnList toInject = new InsnList();
                toInject.add(new MethodInsnNode(INVOKESTATIC, Type.getInternalName(Hooks.class), "netherPortalTeleports", "()Z", false));
                toInject.add(new JumpInsnNode(IFEQ, isRidingEndLabel));

                method.instructions.insert(isRiding, toInject);
            }
        }
    }

    private static void transformRenderGlobal(ClassNode renderGlobalClass, boolean isObfuscated)
    {
        final String SPAWN_PARTICLE     = isObfuscated ? "b"                               : "doSpawnParticle";
        final String PLAY_AUX_SFX       = isObfuscated ? "a"                               : "playAuxSFX";
        final String SPAWN_PARTICLE_SIG = isObfuscated ? "(Ljava/lang/String;DDDDDD)Lbkm;" : "(Ljava/lang/String;DDDDDD)Lnet/minecraft/client/particle/EntityFX;";
        final String PLAY_AUX_SFX_SIG   = isObfuscated ? "(Lyz;IIIII)V"                    : "(Lnet/minecraft/entity/player/EntityPlayer;IIIII)V";

        for (MethodNode method : renderGlobalClass.methods)
        {
            if (method.name.equals(SPAWN_PARTICLE) && method.desc.equals(SPAWN_PARTICLE_SIG))
            {
                /*
                FROM: if (this.mc != null && this.mc.renderViewEntity != null && this.mc.effectRenderer != null)
                TO: if (this.mc != null && Hooks.isParticleAllowed(p_72726_1_) && this.mc.renderViewEntity != null && this.mc.effectRenderer != null)
                */

                //This will grab the first IFNULL JumpInsnNode
                JumpInsnNode injectNode = (JumpInsnNode) ASMHelper.findFirstInstructionWithOpcode(method, IFNULL);
                LabelNode injectNodeLabel = injectNode.label;

                //Hooks.particleIsAllowed(p_72726_1_)
                InsnList toInject = new InsnList();
                toInject.add(new VarInsnNode(ALOAD, 1));
                toInject.add(new MethodInsnNode(INVOKESTATIC, Type.getInternalName(Hooks.class), "particleIsAllowed", "(Ljava/lang/String;)Z", false));
                toInject.add(new JumpInsnNode(IFEQ, injectNodeLabel));

                method.instructions.insert(injectNode, toInject);
            }
            else if (method.name.equals(PLAY_AUX_SFX) && method.desc.equals(PLAY_AUX_SFX_SIG))
            {
                /*
                FROM: this.mc.effectRenderer.addBlockDestroyEffects(p_72706_3_, p_72706_4_, p_72706_5_, block, p_72706_6_ >> 12 & 255);
                TO: if (Hooks.particleIsAllowed("blockBreak") {
                        this.mc.effectRenderer.addBlockDestroyEffects(p_72706_3_, p_72706_4_, p_72706_5_, block, p_72706_6_ >> 12 & 255);
                    }
                */

                //This will be (ALOAD, 0) so we inject before it
                AbstractInsnNode injectNode = ASMHelper.findPreviousInstructionWithOpcode(ASMHelper.findFirstInstructionWithOpcode(method, ISHR), GETFIELD).getPrevious().getPrevious();
                LabelNode ifParticleNode = new LabelNode();
                AbstractInsnNode endIfNode = ASMHelper.findNextInstructionWithOpcode(injectNode, INVOKEVIRTUAL);

                //if (Hooks.particleIsAllowed("blockBreak")
                InsnList toInject = new InsnList();
                toInject.add(new LdcInsnNode("blockBreak"));
                toInject.add(new MethodInsnNode(INVOKESTATIC, Type.getInternalName(Hooks.class), "particleIsAllowed", "(Ljava/lang/String;)Z", false));
                toInject.add(new JumpInsnNode(IFEQ, ifParticleNode));

                method.instructions.insertBefore(injectNode, toInject);
                method.instructions.insert(endIfNode, ifParticleNode);
            }
        }
    }

    private static void transformTEChestRenderer(ClassNode TEChestRendererClass, boolean isObfuscated)
    {
        final String INIT_METHOD     = "<init>";
        final String INIT_METHOD_SIG = "()V";

        for (MethodNode method : TEChestRendererClass.methods)
        {
            if (method.name.equals(INIT_METHOD) && method.desc.equals(INIT_METHOD_SIG))
            {
                //Replaces the christmas chest functionality with Hooks.isChristmasChest();

                AbstractInsnNode calendarStartNode = ASMHelper.findFirstInstructionWithOpcode(method, INVOKESTATIC).getPrevious();
                JumpInsnNode ifStatementNode = (JumpInsnNode) ASMHelper.findFirstInstructionWithOpcode(method, IF_ICMPGT);
                LabelNode ifStatementEndLabel = ifStatementNode.label;

                ASMHelper.removeFromInsnListUntil(method.instructions, calendarStartNode.getNext(), ifStatementNode.getNext());

                InsnList toInject = new InsnList();
                toInject.add(new MethodInsnNode(INVOKESTATIC, Type.getInternalName(Hooks.class), "isChristmasChest", "()Z", false));
                toInject.add(new JumpInsnNode(IFEQ, ifStatementEndLabel));

                method.instructions.insert(calendarStartNode, toInject);
            }
        }
    }

    private static void transformEntityLivingBase(ClassNode entityLivingBaseClass, boolean isObfuscated)
    {
        final String UPDATE_POTION_EFFECTS     = isObfuscated ? "aO" : "updatePotionEffects";
        final String UPDATE_POTION_EFFECTS_SIG = "()V";

        for (MethodNode method : entityLivingBaseClass.methods)
        {
            if (method.name.equals(UPDATE_POTION_EFFECTS) && method.desc.equals(UPDATE_POTION_EFFECTS_SIG))
            {
                /*
                FROM: if (flag && i > 0)
                TO: if (flag && i > 0 && Hooks.particleSpawnedFromEntity(this, flag ? "mobSpellAmbient" : "mobSpell"))
                */

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
                toInject.add(new MethodInsnNode(INVOKESTATIC, Type.getInternalName(Hooks.class), "particleSpawnedFromEntity", isObfuscated ? "(Lsv;Ljava/lang/String;)V" : "(Lnet/minecraft/entity/EntityLivingBase;Ljava/lang/String;)V", false));

                method.instructions.insert(ifSpawnParticleNode, toInject);
            }
        }
    }
}
