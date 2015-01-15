package com.vikestep.sprinklesforvanilla.asm;

import com.vikestep.sprinklesforvanilla.common.util.LogHelper;
import net.minecraft.launchwrapper.IClassTransformer;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.*;
import squeek.asmhelper.ASMHelper;
import squeek.asmhelper.ObfRemappingClassWriter;

import java.util.Arrays;

import static org.objectweb.asm.Opcodes.*;

public class SprinklesForVanillaTransformer implements IClassTransformer
{
    private static final String[] classesBeingTransformed =
            {
                    "net.minecraft.block.BlockPortal",
                    "net.minecraft.client.renderer.RenderGlobal",
                    "net.minecraft.client.renderer.tileentity.TileEntityChestRenderer",
                    "net.minecraft.entity.EntityLivingBase",
                    //Mob Griefing Classes
                    "net.minecraft.block.BlockFarmland",
                    "net.minecraft.entity.EntityLiving",
                    "net.minecraft.entity.ai.EntityAIBreakDoor",
                    "net.minecraft.entity.ai.EntityAIEatGrass",
                    "net.minecraft.entity.boss.EntityDragon",
                    "net.minecraft.entity.boss.EntityWither",
                    "net.minecraft.entity.monster.EntityCreeper",
                    "net.minecraft.entity.monster.EntityEnderman",
                    "net.minecraft.entity.monster.EntitySilverfish",
                    "net.minecraft.entity.projectile.EntityLargeFireball",
                    "net.minecraft.entity.projectile.EntityWitherSkull",
                    //End Mob Griefing Classes
                    "net.minecraft.block.Block"
            };

    private static byte[] transform(int index, byte[] transformingClass, boolean isObfuscated)
    {
        LogHelper.log("Transforming Class: " + classesBeingTransformed[index]);
        try
        {
            ClassNode classNode = ASMHelper.readClassFromBytes(transformingClass);
            switch (index)
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
                case 4:
                    transformBlockFarmland(classNode, isObfuscated);
                    break;
                case 5:
                    transformEntityLiving(classNode, isObfuscated);
                    break;
                case 6:
                    transformEntityAIBreakDoor(classNode, isObfuscated);
                    break;
                case 7:
                    transformEntityAIEatGrass(classNode, isObfuscated);
                    break;
                case 8:
                    transformEntityDragon(classNode, isObfuscated);
                    break;
                case 9:
                    transformEntityWither(classNode, isObfuscated);
                    break;
                case 10:
                    transformEntityCreeper(classNode, isObfuscated);
                    break;
                case 11:
                    transformEntityEnderman(classNode, isObfuscated);
                    break;
                case 12:
                    transformEntitySilverfish(classNode, isObfuscated);
                    break;
                case 13:
                    transformEntityLargeFireball(classNode, isObfuscated);
                    break;
                case 14:
                    transformEntityWitherSkull(classNode, isObfuscated);
                    break;
                case 15:
                    transformBlock(classNode, isObfuscated);
                    break;
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
        final String UPDATE_TICK = isObfuscated ? "a" : "updateTick";
        final String GET_SIZE = isObfuscated ? "e" : "func_150000_e";
        final String ENTITY_COLLIDE = isObfuscated ? "a" : "onEntityCollidedWithBlock";
        final String UPDATE_TICK_DESC = isObfuscated ? "(Lahb;IIILjava/util/Random;)V" : "(Lnet/minecraft/world/World;IIILjava/util/Random;)V";
        final String GET_SIZE_DESC = isObfuscated ? "(Lahb;III)Z" : "(Lnet/minecraft/world/World;III)Z";
        final String ENTITY_COLLIDE_DESC = isObfuscated ? "(Lahb;IIILsa;)V" : "(Lnet/minecraft/world/World;IIILnet/minecraft/entity/Entity;)V";

        for (MethodNode method : blockPortalClass.methods)
        {
            if (method.name.equals(UPDATE_TICK) && method.desc.equals(UPDATE_TICK_DESC))
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
            else if (method.name.equals(GET_SIZE) && method.desc.equals(GET_SIZE_DESC))
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
            else if (method.name.equals(ENTITY_COLLIDE) && method.desc.equals(ENTITY_COLLIDE_DESC))
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
        final String SPAWN_PARTICLE = isObfuscated ? "b" : "doSpawnParticle";
        final String PLAY_AUX_SFX = isObfuscated ? "a" : "playAuxSFX";
        final String SPAWN_PARTICLE_DESC = isObfuscated ? "(Ljava/lang/String;DDDDDD)Lbkm;" : "(Ljava/lang/String;DDDDDD)Lnet/minecraft/client/particle/EntityFX;";
        final String PLAY_AUX_SFX_DESC = isObfuscated ? "(Lyz;IIIII)V" : "(Lnet/minecraft/entity/player/EntityPlayer;IIIII)V";

        for (MethodNode method : renderGlobalClass.methods)
        {
            if (method.name.equals(SPAWN_PARTICLE) && method.desc.equals(SPAWN_PARTICLE_DESC))
            {
                /*
                FROM: if (this.mc != null && this.mc.renderViewEntity != null && this.mc.effectRenderer != null)
                TO: if (this.mc != null && HooksClient.particleIsAllowed(p_72726_1_) && this.mc.renderViewEntity != null && this.mc.effectRenderer != null)
                */

                //This will grab the first IFNULL JumpInsnNode
                JumpInsnNode injectNode = (JumpInsnNode) ASMHelper.findFirstInstructionWithOpcode(method, IFNULL);
                LabelNode injectNodeLabel = injectNode.label;

                //HooksClient.particleIsAllowed(p_72726_1_)
                InsnList toInject = new InsnList();
                toInject.add(new VarInsnNode(ALOAD, 1));
                toInject.add(new MethodInsnNode(INVOKESTATIC, Type.getInternalName(HooksClient.class), "particleIsAllowed", "(Ljava/lang/String;)Z", false));
                toInject.add(new JumpInsnNode(IFEQ, injectNodeLabel));

                method.instructions.insert(injectNode, toInject);
            }
            else if (method.name.equals(PLAY_AUX_SFX) && method.desc.equals(PLAY_AUX_SFX_DESC))
            {
                /*
                FROM: this.mc.effectRenderer.addBlockDestroyEffects(p_72706_3_, p_72706_4_, p_72706_5_, block, p_72706_6_ >> 12 & 255);
                TO: if (HooksClient.particleIsAllowed("blockBreak") {
                        this.mc.effectRenderer.addBlockDestroyEffects(p_72706_3_, p_72706_4_, p_72706_5_, block, p_72706_6_ >> 12 & 255);
                    }
                */

                //This will be (ALOAD, 0) so we inject before it
                AbstractInsnNode injectNode = ASMHelper.findPreviousInstructionWithOpcode(ASMHelper.findFirstInstructionWithOpcode(method, ISHR), GETFIELD).getPrevious().getPrevious();
                LabelNode ifParticleNode = new LabelNode();
                AbstractInsnNode endIfNode = ASMHelper.findNextInstructionWithOpcode(injectNode, INVOKEVIRTUAL);

                //if (HooksClient.particleIsAllowed("blockBreak")
                InsnList toInject = new InsnList();
                toInject.add(new LdcInsnNode("blockBreak"));
                toInject.add(new MethodInsnNode(INVOKESTATIC, Type.getInternalName(HooksClient.class), "particleIsAllowed", "(Ljava/lang/String;)Z", false));
                toInject.add(new JumpInsnNode(IFEQ, ifParticleNode));

                method.instructions.insertBefore(injectNode, toInject);
                method.instructions.insert(endIfNode, ifParticleNode);
            }
        }
    }

    private static void transformTEChestRenderer(ClassNode TEChestRendererClass, boolean isObfuscated)
    {
        final String INIT_METHOD = "<init>";
        final String INIT_METHOD_DESC = "()V";

        for (MethodNode method : TEChestRendererClass.methods)
        {
            if (method.name.equals(INIT_METHOD) && method.desc.equals(INIT_METHOD_DESC))
            {
                //Replaces the christmas chest functionality with HooksClient.isChristmasChest();

                AbstractInsnNode calendarStartNode = ASMHelper.findFirstInstructionWithOpcode(method, INVOKESTATIC).getPrevious();
                JumpInsnNode ifStatementNode = (JumpInsnNode) ASMHelper.findFirstInstructionWithOpcode(method, IF_ICMPGT);
                LabelNode ifStatementEndLabel = ifStatementNode.label;

                ASMHelper.removeFromInsnListUntil(method.instructions, calendarStartNode.getNext(), ifStatementNode.getNext());

                InsnList toInject = new InsnList();
                toInject.add(new MethodInsnNode(INVOKESTATIC, Type.getInternalName(HooksClient.class), "isChristmasChest", "()Z", false));
                toInject.add(new JumpInsnNode(IFEQ, ifStatementEndLabel));

                method.instructions.insert(calendarStartNode, toInject);
            }
        }
    }

    private static void transformEntityLivingBase(ClassNode entityLivingBaseClass, boolean isObfuscated)
    {
        final String UPDATE_POTION_EFFECTS = isObfuscated ? "aO" : "updatePotionEffects";
        final String UPDATE_POTION_EFFECTS_DESC = "()V";

        for (MethodNode method : entityLivingBaseClass.methods)
        {
            if (method.name.equals(UPDATE_POTION_EFFECTS) && method.desc.equals(UPDATE_POTION_EFFECTS_DESC))
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

    private static void transformBlockFarmland(ClassNode blockFarmlandClass, boolean isObfuscated)
    {
        final String ON_FALLEN_UPON = isObfuscated ? "a" : "onFallenUpon";
        final String ON_FALLEN_UPON_DESC = isObfuscated ? "(Lahb;IIILsa;F)V" : "(Lnet/minecraft/world/World;IIILnet/minecraft/entity/Entity;F)V";

        for (MethodNode method : blockFarmlandClass.methods)
        {
            if (method.name.equals(ON_FALLEN_UPON) && method.desc.equals(ON_FALLEN_UPON_DESC))
            {
                AbstractInsnNode getGameRuleStart = ASMHelper.findNextInstructionWithOpcode(ASMHelper.findFirstInstructionWithOpcode(method, INSTANCEOF), ALOAD);
                AbstractInsnNode getGameRuleEnd = ASMHelper.findNextInstructionWithOpcode(getGameRuleStart, IFNE).getPrevious();

                ASMHelper.removeFromInsnListUntil(method.instructions, getGameRuleStart.getNext(), getGameRuleEnd.getNext());

                InsnList toInject = new InsnList();
                toInject.add(new LdcInsnNode("fallenOnFarmland"));
                toInject.add(new MethodInsnNode(INVOKESTATIC, Type.getInternalName(Hooks.class), "canMobGrief", isObfuscated ? "(Lahb;Ljava/lang/String;)Z" : "(Lnet/minecraft/world/World;Ljava/lang/String;)Z", false));

                method.instructions.insert(getGameRuleStart, toInject);
            }
        }
    }

    private static void transformEntityLiving(ClassNode entityLivingClass, boolean isObfuscated)
    {
        final String ON_LIVING_UPDATE = isObfuscated ? "e" : "onLivingUpdate";
        final String ON_LIVING_UPDATE_DESC = "()V";

        for (MethodNode method : entityLivingClass.methods)
        {
            if (method.name.equals(ON_LIVING_UPDATE) && method.desc.equals(ON_LIVING_UPDATE_DESC))
            {
                AbstractInsnNode getGameRuleStart = ASMHelper.findNextInstructionWithOpcode(ASMHelper.findFirstInstructionWithOpcode(method, IFEQ), INVOKEVIRTUAL).getPrevious();
                AbstractInsnNode getGameRuleEnd = ASMHelper.findNextInstructionWithOpcode(getGameRuleStart, IFEQ).getPrevious();

                ASMHelper.removeFromInsnListUntil(method.instructions, getGameRuleStart.getNext(), getGameRuleEnd.getNext());

                InsnList toInject = new InsnList();
                toInject.add(new LdcInsnNode("mobPickUpLoot"));
                toInject.add(new MethodInsnNode(INVOKESTATIC, Type.getInternalName(Hooks.class), "canMobGrief", isObfuscated ? "(Lahb;Ljava/lang/String;)Z" : "(Lnet/minecraft/world/World;Ljava/lang/String;)Z", false));

                method.instructions.insert(getGameRuleStart, toInject);
            }
        }
    }

    private static void transformEntityAIBreakDoor(ClassNode entityAIBreakDoorClass, boolean isObfuscated)
    {
        final String SHOULD_EXECUTE = isObfuscated ? "a" : "shouldExecute";
        final String SHOULD_EXECUTE_DESC = "()Z";

        for (MethodNode method : entityAIBreakDoorClass.methods)
        {
            if (method.name.equals(SHOULD_EXECUTE) && method.desc.equals(SHOULD_EXECUTE_DESC))
            {
                AbstractInsnNode getGameRuleStart = ASMHelper.findFirstInstructionWithOpcode(method, GETFIELD).getNext();
                AbstractInsnNode getGameRuleEnd = ASMHelper.findNextInstructionWithOpcode(getGameRuleStart, IFNE).getPrevious();

                ASMHelper.removeFromInsnListUntil(method.instructions, getGameRuleStart.getNext(), getGameRuleEnd.getNext());

                InsnList toInject = new InsnList();
                toInject.add(new LdcInsnNode("mobBreakDoor"));
                toInject.add(new MethodInsnNode(INVOKESTATIC, Type.getInternalName(Hooks.class), "canMobGrief", isObfuscated ? "(Lahb;Ljava/lang/String;)Z" : "(Lnet/minecraft/world/World;Ljava/lang/String;)Z", false));

                method.instructions.insert(getGameRuleStart, toInject);
            }
        }
    }

    private static void transformEntityAIEatGrass(ClassNode entityAIEatGrassClass, boolean isObfuscated)
    {
        final String UPDATE_TASK = isObfuscated ? "e" : "updateTask";
        final String UPDATE_TASK_DESC = "()V";

        for (MethodNode method : entityAIEatGrassClass.methods)
        {
            if (method.name.equals(UPDATE_TASK) && method.desc.equals(UPDATE_TASK_DESC))
            {
                AbstractInsnNode getGameRuleStart = ASMHelper.findNextInstructionWithOpcode(ASMHelper.findFirstInstructionWithOpcode(method, IF_ACMPNE), GETFIELD);
                AbstractInsnNode getGameRuleEnd = ASMHelper.findNextInstructionWithOpcode(getGameRuleStart, IFEQ).getPrevious();

                ASMHelper.removeFromInsnListUntil(method.instructions, getGameRuleStart.getNext(), getGameRuleEnd.getNext());

                InsnList toInject = new InsnList();
                toInject.add(new LdcInsnNode("mobEatTallGrass"));
                toInject.add(new MethodInsnNode(INVOKESTATIC, Type.getInternalName(Hooks.class), "canMobGrief", isObfuscated ? "(Lahb;Ljava/lang/String;)Z" : "(Lnet/minecraft/world/World;Ljava/lang/String;)Z", false));

                method.instructions.insert(getGameRuleStart, toInject);

                getGameRuleStart = ASMHelper.findNextInstructionWithOpcode(ASMHelper.findNextInstructionWithOpcode(getGameRuleStart, IF_ACMPNE), GETFIELD);
                getGameRuleEnd = ASMHelper.findNextInstructionWithOpcode(getGameRuleStart, IFEQ).getPrevious();

                ASMHelper.removeFromInsnListUntil(method.instructions, getGameRuleStart.getNext(), getGameRuleEnd.getNext());

                toInject = new InsnList();
                toInject.add(new LdcInsnNode("mobEatGrassBlock"));
                toInject.add(new MethodInsnNode(INVOKESTATIC, Type.getInternalName(Hooks.class), "canMobGrief", isObfuscated ? "(Lahb;Ljava/lang/String;)Z" : "(Lnet/minecraft/world/World;Ljava/lang/String;)Z", false));

                method.instructions.insert(getGameRuleStart, toInject);
            }
        }
    }

    private static void transformEntityDragon(ClassNode entityDragonClass, boolean isObfuscated)
    {
        final String DESTROY_BLOCKS_AABB = isObfuscated ? "a" : "destroyBlocksInAABB";
        final String DESTROY_BLOCKS_AABB_DESC = isObfuscated ? "(Lazt;)Z" : "(Lnet/minecraft/util/AxisAlignedBB;)Z";

        for (MethodNode method : entityDragonClass.methods)
        {
            if (method.name.equals(DESTROY_BLOCKS_AABB) && method.desc.equals(DESTROY_BLOCKS_AABB_DESC))
            {
                AbstractInsnNode getGameRuleStart = ASMHelper.findNextInstructionWithOpcode(ASMHelper.findFirstInstructionWithOpcode(method, INVOKEVIRTUAL), IFEQ).getNext().getNext();
                AbstractInsnNode getGameRuleEnd = ASMHelper.findNextInstructionWithOpcode(getGameRuleStart, IFEQ).getPrevious();

                ASMHelper.removeFromInsnListUntil(method.instructions, getGameRuleStart.getNext(), getGameRuleEnd.getNext());

                InsnList toInject = new InsnList();
                toInject.add(new LdcInsnNode("enderDragonBreakBlock"));
                toInject.add(new MethodInsnNode(INVOKESTATIC, Type.getInternalName(Hooks.class), "canMobGrief", isObfuscated ? "(Lahb;Ljava/lang/String;)Z" : "(Lnet/minecraft/world/World;Ljava/lang/String;)Z", false));

                method.instructions.insert(getGameRuleStart, toInject);
            }
        }
    }

    private static void transformEntityWither(ClassNode entityWitherClass, boolean isObfuscated)
    {
        final String UPDATE_AI_TASKS = isObfuscated ? "bn" : "updateAITasks";
        final String UPDATE_AI_TASKS_DESC = "()V";

        for (MethodNode method : entityWitherClass.methods)
        {
            if (method.name.equals(UPDATE_AI_TASKS) && method.desc.equals(UPDATE_AI_TASKS_DESC))
            {
                AbstractInsnNode getGameRuleStart = ASMHelper.findFirstInstructionWithOpcode(method, ICONST_0).getNext().getNext();
                AbstractInsnNode getGameRuleEnd = getGameRuleStart.getNext().getNext().getNext();

                ASMHelper.removeFromInsnListUntil(method.instructions, getGameRuleStart.getNext(), getGameRuleEnd.getNext());

                InsnList toInject = new InsnList();
                toInject.add(new LdcInsnNode("witherExplode"));
                toInject.add(new MethodInsnNode(INVOKESTATIC, Type.getInternalName(Hooks.class), "canMobGrief", isObfuscated ? "(Lahb;Ljava/lang/String;)Z" : "(Lnet/minecraft/world/World;Ljava/lang/String;)Z", false));

                method.instructions.insert(getGameRuleStart, toInject);

                getGameRuleStart = ASMHelper.findPreviousInstructionWithOpcode(ASMHelper.findFirstInstructionWithOpcode(method, ICONST_M1), IFNE).getNext().getNext();
                getGameRuleEnd = getGameRuleStart.getNext().getNext().getNext();

                ASMHelper.removeFromInsnListUntil(method.instructions, getGameRuleStart.getNext(), getGameRuleEnd.getNext());

                toInject = new InsnList();
                toInject.add(new LdcInsnNode("witherBreakBlock"));
                toInject.add(new MethodInsnNode(INVOKESTATIC, Type.getInternalName(Hooks.class), "canMobGrief", isObfuscated ? "(Lahb;Ljava/lang/String;)Z" : "(Lnet/minecraft/world/World;Ljava/lang/String;)Z", false));

                method.instructions.insert(getGameRuleStart, toInject);
            }
        }
    }

    private static void transformEntityCreeper(ClassNode entityCreeperClass, boolean isObfuscated)
    {
        final String EXPLODE = isObfuscated ? "ce" : "func_146077_cc";
        final String EXPLODE_DESC = "()V";

        for (MethodNode method : entityCreeperClass.methods)
        {
            if (method.name.equals(EXPLODE) && method.desc.equals(EXPLODE_DESC))
            {
                AbstractInsnNode getGameRuleStart = ASMHelper.findFirstInstructionWithOpcode(method, INVOKEVIRTUAL).getPrevious();
                AbstractInsnNode getGameRuleEnd = getGameRuleStart.getNext().getNext().getNext();

                ASMHelper.removeFromInsnListUntil(method.instructions, getGameRuleStart.getNext(), getGameRuleEnd.getNext());

                InsnList toInject = new InsnList();
                toInject.add(new LdcInsnNode("creeperExplosion"));
                toInject.add(new MethodInsnNode(INVOKESTATIC, Type.getInternalName(Hooks.class), "canMobGrief", isObfuscated ? "(Lahb;Ljava/lang/String;)Z" : "(Lnet/minecraft/world/World;Ljava/lang/String;)Z", false));

                method.instructions.insert(getGameRuleStart, toInject);
            }
        }
    }

    private static void transformEntityEnderman(ClassNode entityEndermanClass, boolean isObfuscated)
    {
        final String ON_LIVING_UPDATE = isObfuscated ? "e" : "onLivingUpdate";
        final String ON_LIVING_UPDATE_DESC = "()V";

        for (MethodNode method : entityEndermanClass.methods)
        {
            if (method.name.equals(ON_LIVING_UPDATE) && method.desc.equals(ON_LIVING_UPDATE_DESC))
            {
                AbstractInsnNode getGameRuleStart = ASMHelper.findFirstInstructionWithOpcode(method, IFNE).getNext().getNext();
                AbstractInsnNode getGameRuleEnd = getGameRuleStart.getNext().getNext().getNext();

                ASMHelper.removeFromInsnListUntil(method.instructions, getGameRuleStart.getNext(), getGameRuleEnd.getNext());

                InsnList toInject = new InsnList();
                toInject.add(new LdcInsnNode("endermanStealBlock"));
                toInject.add(new MethodInsnNode(INVOKESTATIC, Type.getInternalName(Hooks.class), "canMobGrief", isObfuscated ? "(Lahb;Ljava/lang/String;)Z" : "(Lnet/minecraft/world/World;Ljava/lang/String;)Z", false));

                method.instructions.insert(getGameRuleStart, toInject);
            }
        }
    }

    private static void transformEntitySilverfish(ClassNode entitySilverfishClass, boolean isObfuscated)
    {
        final String UPDATE_ACTION_STATE = isObfuscated ? "bq" : "updateEntityActionState";
        final String UPDATE_ACTION_STATE_DESC = "()V";

        for (MethodNode method : entitySilverfishClass.methods)
        {
            if (method.name.equals(UPDATE_ACTION_STATE) && method.desc.equals(UPDATE_ACTION_STATE_DESC))
            {
                AbstractInsnNode getGameRuleStart = ASMHelper.findNextInstructionWithOpcode(ASMHelper.findFirstInstructionWithOpcode(method, IF_ACMPNE), GETFIELD);
                AbstractInsnNode getGameRuleEnd = getGameRuleStart.getNext().getNext().getNext();

                ASMHelper.removeFromInsnListUntil(method.instructions, getGameRuleStart.getNext(), getGameRuleEnd.getNext());

                InsnList toInject = new InsnList();
                toInject.add(new LdcInsnNode("silverfishBreakBlock"));
                toInject.add(new MethodInsnNode(INVOKESTATIC, Type.getInternalName(Hooks.class), "canMobGrief", isObfuscated ? "(Lahb;Ljava/lang/String;)Z" : "(Lnet/minecraft/world/World;Ljava/lang/String;)Z", false));

                method.instructions.insert(getGameRuleStart, toInject);
            }
        }
    }

    private static void transformEntityLargeFireball(ClassNode entityLargeFireballClass, boolean isObfuscated)
    {
        final String ON_IMPACT = isObfuscated ? "a" : "onImpact";
        final String ON_IMPACT_DESC = isObfuscated ? "(Lazu;)V" : "(Lnet/minecraft/util/MovingObjectPosition;)V";

        for (MethodNode method : entityLargeFireballClass.methods)
        {
            if (method.name.equals(ON_IMPACT) && method.desc.equals(ON_IMPACT_DESC))
            {
                AbstractInsnNode getGameRuleStart = ASMHelper.findLastInstructionWithOpcode(method, GETFIELD);
                AbstractInsnNode getGameRuleEnd = getGameRuleStart.getNext().getNext().getNext();

                ASMHelper.removeFromInsnListUntil(method.instructions, getGameRuleStart.getNext(), getGameRuleEnd.getNext());

                InsnList toInject = new InsnList();
                toInject.add(new LdcInsnNode("largeFireballExplosion"));
                toInject.add(new MethodInsnNode(INVOKESTATIC, Type.getInternalName(Hooks.class), "canMobGrief", isObfuscated ? "(Lahb;Ljava/lang/String;)Z" : "(Lnet/minecraft/world/World;Ljava/lang/String;)Z", false));

                method.instructions.insert(getGameRuleStart, toInject);
            }
        }
    }

    private static void transformEntityWitherSkull(ClassNode entityWitherSkullClass, boolean isObfuscated)
    {
        final String ON_IMPACT = isObfuscated ? "a" : "onImpact";
        final String ON_IMPACT_DESC = isObfuscated ? "(Lazu;)V" : "(Lnet/minecraft/util/MovingObjectPosition;)V";

        for (MethodNode method : entityWitherSkullClass.methods)
        {
            if (method.name.equals(ON_IMPACT) && method.desc.equals(ON_IMPACT_DESC))
            {
                AbstractInsnNode getGameRuleStart = ASMHelper.findLastInstructionWithOpcode(method, GETFIELD);
                AbstractInsnNode getGameRuleEnd = getGameRuleStart.getNext().getNext().getNext();

                ASMHelper.removeFromInsnListUntil(method.instructions, getGameRuleStart.getNext(), getGameRuleEnd.getNext());

                InsnList toInject = new InsnList();
                toInject.add(new LdcInsnNode("witherSkullExplosion"));
                toInject.add(new MethodInsnNode(INVOKESTATIC, Type.getInternalName(Hooks.class), "canMobGrief", isObfuscated ? "(Lahb;Ljava/lang/String;)Z" : "(Lnet/minecraft/world/World;Ljava/lang/String;)Z", false));

                method.instructions.insert(getGameRuleStart, toInject);
            }
        }
    }

    private static void transformBlock(ClassNode blockClass, boolean isObfuscated)
    {
        final String IS_BEACON_BASE = "isBeaconBase";
        final String IS_BEACON_BASE_DESC = isObfuscated ? "(Lahl;IIIIII)Z" : "(Lnet/minecraft/world/IBlockAccess;IIIIII)Z";

        for (MethodNode method : blockClass.methods)
        {
            if (method.name.equals(IS_BEACON_BASE) && method.desc.equals(IS_BEACON_BASE_DESC))
            {
                /*
                Replacing:
                    return this == Blocks.emerald_block || this == Blocks.gold_block || this == Blocks.diamond_block || this == Blocks.iron_block;
                With:
                    return Hooks.isBeaconBase(this, worldObj, x, y, z);

                We check for the metal blocks in the Hooks method
                 */

                AbstractInsnNode startNode = ASMHelper.findFirstInstructionWithOpcode(method, ALOAD);
                AbstractInsnNode endNode = ASMHelper.findNextInstructionWithOpcode(startNode, IRETURN);

                ASMHelper.removeFromInsnListUntil(method.instructions, startNode, endNode);

                InsnList toInject = new InsnList();
                toInject.add(new VarInsnNode(ALOAD, 0));
                toInject.add(new VarInsnNode(ALOAD, 1));
                toInject.add(new VarInsnNode(ILOAD, 2));
                toInject.add(new VarInsnNode(ILOAD, 3));
                toInject.add(new VarInsnNode(ILOAD, 4));
                toInject.add(new MethodInsnNode(INVOKESTATIC, Type.getInternalName(Hooks.class), "isBeaconBase", isObfuscated ? "(Laji;Lahl;III)Z" : "(Lnet/minecraft/block/Block;Lnet/minecraft/world/IBlockAccess;III)Z", false));

                method.instructions.insertBefore(endNode, toInject);
            }
        }
    }

    @Override
    public byte[] transform(String name, String transformedName, byte[] transformingClass)
    {
        boolean isObfuscated = !name.equals(transformedName);
        int index = Arrays.asList(classesBeingTransformed).indexOf(transformedName);
        return index != -1 ? transform(index, transformingClass, isObfuscated) : transformingClass;
    }
}
