package io.github.vikestep.sprinklesforvanilla.asm;

import static org.objectweb.asm.Opcodes.*;
import io.github.vikestep.sprinklesforvanilla.common.utils.LogHelper;
import net.minecraft.launchwrapper.IClassTransformer;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.*;
import squeek.asmhelper.io.github.vikestep.sprinklesforvanilla.ASMHelper;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("unused")
public class SprinklesForVanillaTransformer implements IClassTransformer
{
    private static Map<String, Method> classToTransformMethodMap = new HashMap<String, Method>();

    static
    {
        try
        {
            //Replace with lambda expressions when we move to a requirement of java 8
            classToTransformMethodMap.put("net.minecraft.block.BlockPortal", SprinklesForVanillaTransformer.class.getMethod("transformBlockPortal", ClassNode.class, boolean.class));
            classToTransformMethodMap.put("net.minecraft.client.renderer.RenderGlobal", SprinklesForVanillaTransformer.class.getMethod("transformRenderGlobal", ClassNode.class, boolean.class));
            classToTransformMethodMap.put("net.minecraft.client.renderer.tileentity.TileEntityChestRenderer", SprinklesForVanillaTransformer.class.getMethod("transformTileEntityChestRenderer", ClassNode.class, boolean.class));
            classToTransformMethodMap.put("net.minecraft.entity.EntityLivingBase", SprinklesForVanillaTransformer.class.getMethod("transformEntityLivingBase", ClassNode.class, boolean.class));
            classToTransformMethodMap.put("net.minecraft.block.BlockFarmland", SprinklesForVanillaTransformer.class.getMethod("transformBlockFarmland", ClassNode.class, boolean.class));
            classToTransformMethodMap.put("net.minecraft.entity.EntityLiving", SprinklesForVanillaTransformer.class.getMethod("transformEntityLiving", ClassNode.class, boolean.class));
            classToTransformMethodMap.put("net.minecraft.entity.ai.EntityAIBreakDoor", SprinklesForVanillaTransformer.class.getMethod("transformEntityAIBreakDoor", ClassNode.class, boolean.class));
            classToTransformMethodMap.put("net.minecraft.entity.ai.EntityAIEatGrass", SprinklesForVanillaTransformer.class.getMethod("transformEntityAIEatGrass", ClassNode.class, boolean.class));
            classToTransformMethodMap.put("net.minecraft.entity.boss.EntityDragon", SprinklesForVanillaTransformer.class.getMethod("transformEntityDragon", ClassNode.class, boolean.class));
            classToTransformMethodMap.put("net.minecraft.entity.boss.EntityWither", SprinklesForVanillaTransformer.class.getMethod("transformEntityWither", ClassNode.class, boolean.class));
            classToTransformMethodMap.put("net.minecraft.entity.monster.EntityCreeper", SprinklesForVanillaTransformer.class.getMethod("transformEntityCreeper", ClassNode.class, boolean.class));
            classToTransformMethodMap.put("net.minecraft.entity.monster.EntityEnderman$AIPlaceBlock", SprinklesForVanillaTransformer.class.getMethod("transformEntityEnderman", ClassNode.class, boolean.class));
            classToTransformMethodMap.put("net.minecraft.entity.monster.EntitySilverfish$AISummonSilverfish", SprinklesForVanillaTransformer.class.getMethod("transformEntitySilverfish", ClassNode.class, boolean.class));
            classToTransformMethodMap.put("net.minecraft.entity.projectile.EntityLargeFireball", SprinklesForVanillaTransformer.class.getMethod("transformEntityLargeFireball", ClassNode.class, boolean.class));
            classToTransformMethodMap.put("net.minecraft.entity.projectile.EntityWitherSkull", SprinklesForVanillaTransformer.class.getMethod("transformEntityWitherSkull", ClassNode.class, boolean.class));
            classToTransformMethodMap.put("net.minecraft.block.Block", SprinklesForVanillaTransformer.class.getMethod("transformBlock", ClassNode.class, boolean.class));
//            classToTransformMethodMap.put("net.minecraft.block.BlockBed", SprinklesForVanillaTransformer.class.getMethod("transformBlockBed", ClassNode.class, boolean.class));
            classToTransformMethodMap.put("net.minecraft.world.WorldProviderHell", SprinklesForVanillaTransformer.class.getMethod("transformWorldProviderHell", ClassNode.class, boolean.class));
            classToTransformMethodMap.put("net.minecraft.world.WorldProviderEnd", SprinklesForVanillaTransformer.class.getMethod("transformWorldProviderEnd", ClassNode.class, boolean.class));
            classToTransformMethodMap.put("net.minecraft.item.ItemBucket", SprinklesForVanillaTransformer.class.getMethod("transformItemBucket", ClassNode.class, boolean.class));
            classToTransformMethodMap.put("net.minecraft.block.BlockLiquid", SprinklesForVanillaTransformer.class.getMethod("transformBlockLiquid", ClassNode.class, boolean.class));
//            classToTransformMethodMap.put("cofh.asmhooks.block.BlockWater", SprinklesForVanillaTransformer.class.getMethod("transformCoFHBlockWater", ClassNode.class, boolean.class));
//            classToTransformMethodMap.put("cofh.asmhooks.block.BlockTickingWater", SprinklesForVanillaTransformer.class.getMethod("transformCoFHBlockWater", ClassNode.class, boolean.class));
            classToTransformMethodMap.put("net.minecraft.server.management.ServerConfigurationManager", SprinklesForVanillaTransformer.class.getMethod("transformServerConfigurationManager", ClassNode.class, boolean.class));
            classToTransformMethodMap.put("net.minecraft.tileentity.TileEntityBeacon", SprinklesForVanillaTransformer.class.getMethod("transformTileEntityBeacon", ClassNode.class, boolean.class));
            classToTransformMethodMap.put("net.minecraft.world.WorldServer", SprinklesForVanillaTransformer.class.getMethod("transformWorldServer", ClassNode.class, boolean.class));
            classToTransformMethodMap.put("net.minecraft.block.BlockSapling", SprinklesForVanillaTransformer.class.getMethod("transformBlockSapling", ClassNode.class, boolean.class));
            classToTransformMethodMap.put("net.minecraft.block.BlockCrops", SprinklesForVanillaTransformer.class.getMethod("transformBlockCrops", ClassNode.class, boolean.class));
            classToTransformMethodMap.put("net.minecraft.world.SpawnerAnimals", SprinklesForVanillaTransformer.class.getMethod("transformSpawnerAnimals", ClassNode.class, boolean.class));
            classToTransformMethodMap.put("net.minecraft.item.ItemEnderEye", SprinklesForVanillaTransformer.class.getMethod("transformItemEnderEye", ClassNode.class, boolean.class));
            classToTransformMethodMap.put("net.minecraft.block.BlockEndPortal", SprinklesForVanillaTransformer.class.getMethod("transformBlockEndPortal", ClassNode.class, boolean.class));
            classToTransformMethodMap.put("net.minecraft.entity.monster.EntitySkeleton", SprinklesForVanillaTransformer.class.getMethod("transformEntitySkeleton", ClassNode.class, boolean.class));
            classToTransformMethodMap.put("net.minecraft.entity.monster.EntityZombie", SprinklesForVanillaTransformer.class.getMethod("transformEntityZombie", ClassNode.class, boolean.class));
            classToTransformMethodMap.put("net.minecraft.client.entity.EntityPlayerSP", SprinklesForVanillaTransformer.class.getMethod("transformEntityPlayerSP", ClassNode.class, boolean.class));
        }
        catch (NoSuchMethodException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public byte[] transform(String name, String transformedName, byte[] clazz)
    {
        Method transformMethod = classToTransformMethodMap.get(transformedName);
        if (transformMethod == null)
        {
            return clazz;
        }
        LogHelper.info("Transforming Class: " + transformedName);
        try
        {
            ClassNode classNode = ASMHelper.readClassFromBytes(clazz);
            transformMethod.invoke(null, classNode, SprinklesForVanillaPlugin.isObf);
            return ASMHelper.writeClassToBytes(classNode, ClassWriter.COMPUTE_MAXS | (transformedName.equals("net.minecraft.entity.EntityLivingBase") ? 0 : ClassWriter.COMPUTE_FRAMES));
        }
        catch (Exception e)
        {
            LogHelper.warn("Something went wrong trying to transform " + transformedName);
            e.printStackTrace();
        }
        return clazz;
    }

    public static void transformBlockPortal(ClassNode classNode, boolean isObf)
    {
        final String UPDATE_TICK = isObf ? "b" : "updateTick";
        final String GET_SIZE = isObf ? "e" : "func_176548_d";
        final String ENTITY_COLLIDE = isObf ? "a" : "onEntityCollidedWithBlock";
        final String UPDATE_TICK_DESC = isObf ? "(Ladm;Lcj;Lalz;Ljava/util/Random;)V" : "(Lnet/minecraft/world/World;Lnet/minecraft/util/BlockPos;Lnet/minecraft/block/state/IBlockState;Ljava/util/Random;)V";
        final String GET_SIZE_DESC = isObf ? "(Ladm;Lcj;)Z" : "(Lnet/minecraft/world/World;Lnet/minecraft/util/BlockPos;)Z";
        final String ENTITY_COLLIDE_DESC = isObf ? "(Ladm;Lcj;Lalz;Lpk;)V" : "(Lnet/minecraft/world/World;Lnet/minecraft/util/BlockPos;Lnet/minecraft/block/state/IBlockState;Lnet/minecraft/entity/Entity;)V";

        for (MethodNode method : classNode.methods)
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
                ASMHelper.skipInstructions(method.instructions, injectPoint.getNext(), endNodeRemoved.getNext());

                //This is where our replacement conditional will point
                LabelNode newConditionalEndLabel = ((JumpInsnNode) injectPoint).label;

                //HookBlockPortal.spawnZombiePigman(p_149674_1_, p_149674_5_)
                InsnList toInject = new InsnList();
                toInject.add(new VarInsnNode(ALOAD, 1));
                toInject.add(new VarInsnNode(ALOAD, 4));
                toInject.add(new MethodInsnNode(INVOKESTATIC, Type.getInternalName(Hooks.class), "spawnZombiePigmen", isObf ? "(Ladm;Ljava/util/Random;)Z" : "(Lnet/minecraft/world/World;Ljava/util/Random;)Z", false));
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

    public static void transformRenderGlobal(ClassNode classNode, boolean isObf)
    {
        final String SPAWN_PARTICLE = isObf ? "b" : "spawnEntityFX";
        final String SPAWN_PARTICLE_DESC = isObf ? "(IZDDDDDD[I)Lbeb;" : "(IZDDDDDD[I)Lnet/minecraft/client/particle/EntityFX;";

        for (MethodNode method : classNode.methods)
        {
            if (method.name.equals(SPAWN_PARTICLE) && method.desc.equals(SPAWN_PARTICLE_DESC))
            {
                /*
                FROM: if (this.mc != null && this.mc.renderViewEntity != null && this.mc.effectRenderer != null)
                TO: if (this.mc != null && HooksClient.particleIsAllowed(p_174974_1_) && this.mc.renderViewEntity != null && this.mc.effectRenderer != null)
                */

                //This will grab the first IFNULL JumpInsnNode
                AbstractInsnNode ifNode = ASMHelper.findFirstInstructionWithOpcode(method, IFNULL);

                //HooksClient.particleIsAllowed(p_72726_1_)
                InsnList toInject = new InsnList();
                toInject.add(new VarInsnNode(ILOAD, 1));
                toInject.add(new MethodInsnNode(INVOKESTATIC, Type.getInternalName(HooksClient.class), "particleIsAllowed", "(I)Z", false));
                toInject.add(new JumpInsnNode(IFEQ, ((JumpInsnNode)ifNode).label));

                method.instructions.insertBefore(ifNode.getPrevious().getPrevious(), toInject);
            }
        }
    }

    public static void transformTileEntityChestRenderer(ClassNode classNode, boolean isObf)
    {
        final String INIT_METHOD = "<init>";
        final String INIT_METHOD_DESC = "()V";

        for (MethodNode method : classNode.methods)
        {
            if (method.name.equals(INIT_METHOD) && method.desc.equals(INIT_METHOD_DESC))
            {
                //Replaces the christmas chest functionality with HooksClient.isChristmasChest();

                AbstractInsnNode calendarStartNode = ASMHelper.findFirstInstructionWithOpcode(method, INVOKESTATIC).getPrevious();
                JumpInsnNode ifStatementNode = (JumpInsnNode) ASMHelper.findFirstInstructionWithOpcode(method, IF_ICMPGT);
                LabelNode ifStatementEndLabel = ifStatementNode.label;

                ASMHelper.skipInstructions(method.instructions, calendarStartNode.getNext(), ifStatementNode.getNext());

                InsnList toInject = new InsnList();
                toInject.add(new MethodInsnNode(INVOKESTATIC, Type.getInternalName(HooksClient.class), "isChristmasChest", "()Z", false));
                toInject.add(new JumpInsnNode(IFEQ, ifStatementEndLabel));

                method.instructions.insert(calendarStartNode, toInject);
            }
        }
    }

    public static void transformEntityLivingBase(ClassNode classNode, boolean isObf)
    {
        final String UPDATE_POTION_EFFECTS = isObf ? "bi" : "updatePotionEffects";
        final String UPDATE_POTION_EFFECTS_DESC = "()V";

        for (MethodNode method : classNode.methods)
        {
            if (method.name.equals(UPDATE_POTION_EFFECTS) && method.desc.equals(UPDATE_POTION_EFFECTS_DESC))
            {
                method.maxStack += 3;
                /*
                FROM: if (flag && i > 0)
                TO: if (flag && i > 0 && Hooks.particleSpawnedFromEntity(this))
                */

                JumpInsnNode ifSpawnParticleNode = (JumpInsnNode) ASMHelper.findLastInstructionWithOpcode(method, IFLE);

                InsnList toInject = new InsnList();
                toInject.add(new VarInsnNode(ALOAD, 0)); //this
                toInject.add(new MethodInsnNode(INVOKESTATIC, Type.getInternalName(Hooks.class), "particleSpawnedFromEntity", isObf ? "(Lpr;)V" : "(Lnet/minecraft/entity/EntityLivingBase;)V", false));

                method.instructions.insert(ifSpawnParticleNode, toInject);
            }
        }
    }

    public static void transformBlockFarmland(ClassNode classNode, boolean isObf)
    {
        final String ON_FALLEN_UPON = isObf ? "a" : "onFallenUpon";
        final String ON_FALLEN_UPON_DESC = isObf ? "(Ladm;Lcj;Lpk;F)V" : "(Lnet/minecraft/world/World;Lnet/minecraft/util/BlockPos;Lnet/minecraft/entity/Entity;F)V";

        for (MethodNode method : classNode.methods)
        {
            if (method.name.equals(ON_FALLEN_UPON) && method.desc.equals(ON_FALLEN_UPON_DESC))
            {
                AbstractInsnNode getGameRuleStart = ASMHelper.findPreviousInstructionWithOpcode(ASMHelper.findFirstInstructionWithOpcode(method, RETURN), ALOAD);
                AbstractInsnNode getGameRuleEnd = ASMHelper.findNextInstructionWithOpcode(getGameRuleStart, IFNE).getPrevious();

                ASMHelper.skipInstructions(method.instructions, getGameRuleStart.getNext(), getGameRuleEnd.getNext());

                InsnList toInject = new InsnList();
                toInject.add(new LdcInsnNode("fallenOnFarmland"));
                toInject.add(new MethodInsnNode(INVOKESTATIC, Type.getInternalName(Hooks.class), "canMobGrief", isObf ? "(Ladm;Ljava/lang/String;)Z" : "(Lnet/minecraft/world/World;Ljava/lang/String;)Z", false));

                method.instructions.insert(getGameRuleStart, toInject);
            }
        }
    }

    public static void transformEntityLiving(ClassNode classNode, boolean isObf)
    {
        final String ON_LIVING_UPDATE = isObf ? "m" : "onLivingUpdate";
        final String ON_LIVING_UPDATE_DESC = "()V";

        for (MethodNode method : classNode.methods)
        {
            if (method.name.equals(ON_LIVING_UPDATE) && method.desc.equals(ON_LIVING_UPDATE_DESC))
            {
                AbstractInsnNode getGameRuleStart = ASMHelper.findNextInstructionWithOpcode(ASMHelper.findFirstInstructionWithOpcode(method, IFEQ), INVOKEVIRTUAL).getPrevious();
                AbstractInsnNode getGameRuleEnd = ASMHelper.findNextInstructionWithOpcode(getGameRuleStart, IFEQ).getPrevious();

                ASMHelper.skipInstructions(method.instructions, getGameRuleStart.getNext(), getGameRuleEnd.getNext());

                InsnList toInject = new InsnList();
                toInject.add(new LdcInsnNode("mobPickUpLoot"));
                toInject.add(new MethodInsnNode(INVOKESTATIC, Type.getInternalName(Hooks.class), "canMobGrief", isObf ? "(Ladm;Ljava/lang/String;)Z" : "(Lnet/minecraft/world/World;Ljava/lang/String;)Z", false));

                method.instructions.insert(getGameRuleStart, toInject);
            }
        }
    }

    public static void transformEntityAIBreakDoor(ClassNode classNode, boolean isObf)
    {
        final String SHOULD_EXECUTE = isObf ? "a" : "shouldExecute";
        final String SHOULD_EXECUTE_DESC = "()Z";

        for (MethodNode method : classNode.methods)
        {
            if (method.name.equals(SHOULD_EXECUTE) && method.desc.equals(SHOULD_EXECUTE_DESC))
            {
                AbstractInsnNode getGameRuleStart = ASMHelper.findFirstInstructionWithOpcode(method, GETFIELD).getNext();
                AbstractInsnNode getGameRuleEnd = ASMHelper.findNextInstructionWithOpcode(getGameRuleStart, IFNE).getPrevious();

                ASMHelper.skipInstructions(method.instructions, getGameRuleStart.getNext(), getGameRuleEnd.getNext());

                InsnList toInject = new InsnList();
                toInject.add(new LdcInsnNode("mobBreakDoor"));
                toInject.add(new MethodInsnNode(INVOKESTATIC, Type.getInternalName(Hooks.class), "canMobGrief", isObf ? "(Ladm;Ljava/lang/String;)Z" : "(Lnet/minecraft/world/World;Ljava/lang/String;)Z", false));

                method.instructions.insert(getGameRuleStart, toInject);
            }
        }
    }

    public static void transformEntityAIEatGrass(ClassNode classNode, boolean isObf)
    {
        final String UPDATE_TASK = isObf ? "e" : "updateTask";
        final String UPDATE_TASK_DESC = "()V";

        for (MethodNode method : classNode.methods)
        {
            if (method.name.equals(UPDATE_TASK) && method.desc.equals(UPDATE_TASK_DESC))
            {
                AbstractInsnNode getGameRuleStart = ASMHelper.findNextInstructionWithOpcode(ASMHelper.findFirstInstructionWithOpcode(method, INVOKEINTERFACE), GETFIELD);
                AbstractInsnNode getGameRuleEnd = ASMHelper.findNextInstructionWithOpcode(getGameRuleStart, IFEQ).getPrevious();

                ASMHelper.skipInstructions(method.instructions, getGameRuleStart.getNext(), getGameRuleEnd.getNext());

                InsnList toInject = new InsnList();
                toInject.add(new LdcInsnNode("mobEatTallGrass"));
                toInject.add(new MethodInsnNode(INVOKESTATIC, Type.getInternalName(Hooks.class), "canMobGrief", isObf ? "(Ladm;Ljava/lang/String;)Z" : "(Lnet/minecraft/world/World;Ljava/lang/String;)Z", false));

                method.instructions.insert(getGameRuleStart, toInject);

                getGameRuleStart = ASMHelper.findNextInstructionWithOpcode(ASMHelper.findNextInstructionWithOpcode(getGameRuleStart, IF_ACMPNE), GETFIELD);
                getGameRuleEnd = ASMHelper.findNextInstructionWithOpcode(getGameRuleStart, IFEQ).getPrevious();

                ASMHelper.skipInstructions(method.instructions, getGameRuleStart.getNext(), getGameRuleEnd.getNext());

                toInject = new InsnList();
                toInject.add(new LdcInsnNode("mobEatGrassBlock"));
                toInject.add(new MethodInsnNode(INVOKESTATIC, Type.getInternalName(Hooks.class), "canMobGrief", isObf ? "(Ladm;Ljava/lang/String;)Z" : "(Lnet/minecraft/world/World;Ljava/lang/String;)Z", false));

                method.instructions.insert(getGameRuleStart, toInject);
            }
        }
    }

    public static void transformEntityDragon(ClassNode classNode, boolean isObf)
    {
        final String DESTROY_BLOCKS_AABB = isObf ? "b" : "destroyBlocksInAABB";
        final String DESTROY_BLOCKS_AABB_DESC = isObf ? "(Laug;)Z" : "(Lnet/minecraft/util/AxisAlignedBB;)Z";

        for (MethodNode method : classNode.methods)
        {
            if (method.name.equals(DESTROY_BLOCKS_AABB) && method.desc.equals(DESTROY_BLOCKS_AABB_DESC))
            {
                AbstractInsnNode getGameRuleStart = ASMHelper.findNextInstructionWithOpcode(ASMHelper.findFirstInstructionWithOpcode(method, INVOKEVIRTUAL), IFEQ).getNext().getNext();
                AbstractInsnNode getGameRuleEnd = ASMHelper.findNextInstructionWithOpcode(getGameRuleStart, IFEQ).getPrevious();

                ASMHelper.skipInstructions(method.instructions, getGameRuleStart.getNext(), getGameRuleEnd.getNext());

                InsnList toInject = new InsnList();
                toInject.add(new LdcInsnNode("enderDragonBreakBlock"));
                toInject.add(new MethodInsnNode(INVOKESTATIC, Type.getInternalName(Hooks.class), "canMobGrief", isObf ? "(Ladm;Ljava/lang/String;)Z" : "(Lnet/minecraft/world/World;Ljava/lang/String;)Z", false));

                method.instructions.insert(getGameRuleStart, toInject);
            }
        }
    }

    public static void transformEntityWither(ClassNode classNode, boolean isObf)
    {
        final String UPDATE_AI_TASKS = isObf ? "E" : "updateAITasks";
        final String UPDATE_AI_TASKS_DESC = "()V";

        for (MethodNode method : classNode.methods)
        {
            if (method.name.equals(UPDATE_AI_TASKS) && method.desc.equals(UPDATE_AI_TASKS_DESC))
            {
                AbstractInsnNode getGameRuleStart = ASMHelper.findFirstInstructionWithOpcode(method, ICONST_0).getNext().getNext();
                AbstractInsnNode getGameRuleEnd = getGameRuleStart.getNext().getNext().getNext();

                ASMHelper.skipInstructions(method.instructions, getGameRuleStart.getNext(), getGameRuleEnd.getNext());

                InsnList toInject = new InsnList();
                toInject.add(new LdcInsnNode("witherExplode"));
                toInject.add(new MethodInsnNode(INVOKESTATIC, Type.getInternalName(Hooks.class), "canMobGrief", isObf ? "(Ladm;Ljava/lang/String;)Z" : "(Lnet/minecraft/world/World;Ljava/lang/String;)Z", false));

                method.instructions.insert(getGameRuleStart, toInject);

                getGameRuleStart = ASMHelper.findPreviousInstructionWithOpcode(ASMHelper.findFirstInstructionWithOpcode(method, ICONST_M1), IFNE).getNext().getNext();
                getGameRuleEnd = getGameRuleStart.getNext().getNext().getNext();

                ASMHelper.skipInstructions(method.instructions, getGameRuleStart.getNext(), getGameRuleEnd.getNext());

                toInject = new InsnList();
                toInject.add(new LdcInsnNode("witherBreakBlock"));
                toInject.add(new MethodInsnNode(INVOKESTATIC, Type.getInternalName(Hooks.class), "canMobGrief", isObf ? "(Ladm;Ljava/lang/String;)Z" : "(Lnet/minecraft/world/World;Ljava/lang/String;)Z", false));

                method.instructions.insert(getGameRuleStart, toInject);
            }
        }
    }

    public static void transformEntityCreeper(ClassNode classNode, boolean isObf)
    {
        final String EXPLODE = isObf ? "cr" : "explode";
        final String EXPLODE_DESC = "()V";

        for (MethodNode method : classNode.methods)
        {
            if (method.name.equals(EXPLODE) && method.desc.equals(EXPLODE_DESC))
            {
                AbstractInsnNode iStoreNode = ASMHelper.findFirstInstructionWithOpcode(method, ISTORE);

                InsnList toInject = new InsnList();
                toInject.add(new VarInsnNode(ILOAD, 1));
                toInject.add(new VarInsnNode(ALOAD, 0));
                toInject.add(new FieldInsnNode(GETFIELD, isObf ? "vn" : "net/minecraft/entity/monster/EntityCreeper", isObf ? "o" : "worldObj", isObf ? "Ladm;" : "Lnet/minecraft/world/World;"));
                toInject.add(new LdcInsnNode("creeperExplosion"));
                toInject.add(new MethodInsnNode(INVOKESTATIC, Type.getInternalName(Hooks.class), "canMobGrief", isObf ? "(ILadm;Ljava/lang/String;)Z" : "(ILnet/minecraft/world/World;Ljava/lang/String;)Z", false));
                toInject.add(new VarInsnNode(ISTORE, 1));

                method.instructions.insert(iStoreNode, toInject);
            }
        }
    }

    public static void transformEntityEnderman(ClassNode classNode, boolean isObf)
    {
        final String ON_LIVING_UPDATE = isObf ? "a" : "shouldExecute";
        final String ON_LIVING_UPDATE_DESC = "()Z";

        for (MethodNode method : classNode.methods)
        {
            if (method.name.equals(ON_LIVING_UPDATE) && method.desc.equals(ON_LIVING_UPDATE_DESC))
            {
                AbstractInsnNode getGameRuleStart = ASMHelper.findFirstInstructionWithOpcode(method, GETFIELD).getNext();
                AbstractInsnNode getGameRuleEnd = getGameRuleStart.getNext().getNext().getNext();

                ASMHelper.skipInstructions(method.instructions, getGameRuleStart.getNext(), getGameRuleEnd.getNext());

                InsnList toInject = new InsnList();
                toInject.add(new LdcInsnNode("endermanStealBlock"));
                toInject.add(new MethodInsnNode(INVOKESTATIC, Type.getInternalName(Hooks.class), "canMobGrief", isObf ? "(Ladm;Ljava/lang/String;)Z" : "(Lnet/minecraft/world/World;Ljava/lang/String;)Z", false));

                method.instructions.insert(getGameRuleStart, toInject);
            }
        }
    }

    public static void transformEntitySilverfish(ClassNode classNode, boolean isObf)
    {
        final String UPDATE_ACTION_STATE = isObf ? "e" : "updateTask";
        final String UPDATE_ACTION_STATE_DESC = "()V";

        for (MethodNode method : classNode.methods)
        {
            if (method.name.equals(UPDATE_ACTION_STATE) && method.desc.equals(UPDATE_ACTION_STATE_DESC))
            {
                AbstractInsnNode getGameRuleStart = ASMHelper.findLastInstructionWithOpcode(method, LDC).getPrevious().getPrevious();
                AbstractInsnNode getGameRuleEnd = getGameRuleStart.getNext().getNext().getNext();

                ASMHelper.skipInstructions(method.instructions, getGameRuleStart.getNext(), getGameRuleEnd.getNext());

                InsnList toInject = new InsnList();
                toInject.add(new LdcInsnNode("silverfishBreakBlock"));
                toInject.add(new MethodInsnNode(INVOKESTATIC, Type.getInternalName(Hooks.class), "canMobGrief", isObf ? "(Ladm;Ljava/lang/String;)Z" : "(Lnet/minecraft/world/World;Ljava/lang/String;)Z", false));

                method.instructions.insert(getGameRuleStart, toInject);
            }
        }
    }

    public static void transformEntityLargeFireball(ClassNode classNode, boolean isObf)
    {
        final String ON_IMPACT = isObf ? "a" : "onImpact";
        final String ON_IMPACT_DESC = isObf ? "(Lauh;)V" : "(Lnet/minecraft/util/MovingObjectPosition;)V";

        for (MethodNode method : classNode.methods)
        {
            if (method.name.equals(ON_IMPACT) && method.desc.equals(ON_IMPACT_DESC))
            {
                /*AbstractInsnNode ifNodeStart = ASMHelper.findFirstInstructionWithOpcode(method, IFNE);

                InsnList toInject = new InsnList();
                toInject.add(new VarInsnNode(ALOAD, 0));
                toInject.add(new MethodInsnNode(INVOKESTATIC, Type.getInternalName(Hooks.class), "fireballExplode", isObf ? "(Lzg;)V" : "(Lnet/minecraft/entity/projectile/EntityLargeFireball;)V", false));

                method.instructions.insert(ifNodeStart, toInject);*/

                AbstractInsnNode getGameRuleEnd = ASMHelper.findFirstInstructionWithOpcode(method, ISTORE).getPrevious();
                AbstractInsnNode getGameRuleStart = getGameRuleEnd.getPrevious().getPrevious().getPrevious();

                ASMHelper.skipInstructions(method.instructions, getGameRuleStart.getNext(), getGameRuleEnd.getNext());

                InsnList toInject = new InsnList();
                toInject.add(new LdcInsnNode("largeFireballExplosion"));
                toInject.add(new MethodInsnNode(INVOKESTATIC, Type.getInternalName(Hooks.class), "canMobGrief", isObf ? "(Ladm;Ljava/lang/String;)Z" : "(Lnet/minecraft/world/World;Ljava/lang/String;)Z", false));

                method.instructions.insert(getGameRuleStart, toInject);
            }
        }
    }

    public static void transformEntityWitherSkull(ClassNode classNode, boolean isObf)
    {
        final String ON_IMPACT = isObf ? "a" : "onImpact";
        final String ON_IMPACT_DESC = isObf ? "(Lauh;)V" : "(Lnet/minecraft/util/MovingObjectPosition;)V";

        for (MethodNode method : classNode.methods)
        {
            if (method.name.equals(ON_IMPACT) && method.desc.equals(ON_IMPACT_DESC))
            {
                AbstractInsnNode getGameRuleStart = ASMHelper.findLastInstructionWithOpcode(method, GETFIELD);
                AbstractInsnNode getGameRuleEnd = getGameRuleStart.getNext().getNext().getNext();

                ASMHelper.skipInstructions(method.instructions, getGameRuleStart.getNext(), getGameRuleEnd.getNext());

                InsnList toInject = new InsnList();
                toInject.add(new LdcInsnNode("witherSkullExplosion"));
                toInject.add(new MethodInsnNode(INVOKESTATIC, Type.getInternalName(Hooks.class), "canMobGrief", isObf ? "(Ladm;Ljava/lang/String;)Z" : "(Lnet/minecraft/world/World;Ljava/lang/String;)Z", false));

                method.instructions.insert(getGameRuleStart, toInject);
            }
        }
    }

    public static void transformBlock(ClassNode classNode, boolean isObf)
    {
        final String IS_BEACON_BASE = "isBeaconBase";
        final String IS_BEACON_BASE_DESC = isObf ? "(Ladq;Lcj;Lcj;)Z" : "(Lnet/minecraft/world/IBlockAccess;Lnet/minecraft/util/BlockPos;Lnet/minecraft/util/BlockPos;)Z";

        for (MethodNode method : classNode.methods)
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

                ASMHelper.skipInstructions(method.instructions, startNode, endNode);

                InsnList toInject = new InsnList();
                toInject.add(new VarInsnNode(ALOAD, 0));
                toInject.add(new VarInsnNode(ALOAD, 1));
                toInject.add(new VarInsnNode(ALOAD, 2));
                toInject.add(new MethodInsnNode(INVOKESTATIC, Type.getInternalName(Hooks.class), "isBeaconBase", isObf ? "(Lafh;Ladq;Lcj;)Z" : "(Lnet/minecraft/block/Block;Lnet/minecraft/world/IBlockAccess;Lnet/minecraft/util/BlockPos;)Z", false));

                method.instructions.insertBefore(endNode, toInject);
            }
        }
    }

//    public static void transformBlockBed(ClassNode classNode, boolean isObf)
//    {
//        final String ON_BLOCK_ACTIVATED = isObf ? "a" : "onBlockActivated";
//        final String ON_BLOCK_ACTIVATED_DESC = isObf ? "(Ladm;Lcj;Lalz;Lwn;Lcq;FFF)Z" : "(Lnet/minecraft/world/World;Lnet/minecraft/util/BlockPos;Lnet/minecraft/block/state/IBlockState;Lnet/minecraft/entity/player/EntityPlayer;Lnet/minecraft/util/EnumFacing;FFF)Z";
//
//        for (MethodNode method : classNode.methods)
//        {
//            if (method.name.equals(ON_BLOCK_ACTIVATED) && method.desc.equals(ON_BLOCK_ACTIVATED_DESC))
//            {
//                InsnList toInject = new InsnList();
//                toInject.add(new MethodInsnNode(INVOKESTATIC, Type.getInternalName(Hooks.class), "allowOtherDimensions", "()Z", false));
//                if (!isObf)
//                {
//                    AbstractInsnNode endIfNode = ASMHelper.findNextInstructionWithOpcode(ASMHelper.findFirstInstructionWithOpcode(method, IF_ACMPEQ), IF_ACMPEQ);
//                    AbstractInsnNode startIfNode = ASMHelper.findPreviousInstructionWithOpcode(endIfNode, GETFIELD).getPrevious();
//
//                    LabelNode jumpLabel = new LabelNode();
//                    toInject.add(new JumpInsnNode(IFEQ, jumpLabel));
//
//                    method.instructions.insertBefore(startIfNode, toInject);
//                    method.instructions.insert(endIfNode, jumpLabel);
//                }
//                else
//                {
//                    AbstractInsnNode endIfNode = ASMHelper.findFirstInstructionWithOpcode(method, IF_ACMPNE);
//                    AbstractInsnNode startIfNode = ASMHelper.findPreviousInstructionWithOpcode(endIfNode, GETFIELD).getPrevious();
//
//                    toInject.add(new JumpInsnNode(IFEQ, ((JumpInsnNode) endIfNode).label));
//
//                    method.instructions.insertBefore(startIfNode, toInject);
//                }
//            }
//        }
//    }

    public static void transformWorldProviderHell(ClassNode classNode, boolean isObf)
    {
        final String CAN_RESPAWN_HERE = isObf ? "e" : "canRespawnHere";
        final String CAN_RESPAWN_HERE_DESC = "()Z";

        for (MethodNode method : classNode.methods)
        {
            if (method.name.equals(CAN_RESPAWN_HERE) && method.desc.equals(CAN_RESPAWN_HERE_DESC))
            {
                AbstractInsnNode falseNode = ASMHelper.findFirstInstructionWithOpcode(method, ICONST_0);
                method.instructions.insert(falseNode, new MethodInsnNode(INVOKESTATIC, Type.getInternalName(Hooks.class), "respawnInNether", "()Z", false));
                ASMHelper.skipInstructions(method.instructions, falseNode, falseNode.getNext());
            }
        }
    }

    public static void transformWorldProviderEnd(ClassNode classNode, boolean isObf)
    {
        final String CAN_RESPAWN_HERE = isObf ? "e" : "canRespawnHere";
        final String CAN_RESPAWN_HERE_DESC = "()Z";

        for (MethodNode method : classNode.methods)
        {
            if (method.name.equals(CAN_RESPAWN_HERE) && method.desc.equals(CAN_RESPAWN_HERE_DESC))
            {
                AbstractInsnNode falseNode = ASMHelper.findFirstInstructionWithOpcode(method, ICONST_0);
                method.instructions.insert(falseNode, new MethodInsnNode(INVOKESTATIC, Type.getInternalName(Hooks.class), "respawnInEnd", "()Z", false));
                ASMHelper.skipInstructions(method.instructions, falseNode, falseNode.getNext());
            }
        }
    }

    public static void transformItemBucket(ClassNode classNode, boolean isObf)
    {
        final String TRY_PLACE_LIQUID = isObf ? "a" : "tryPlaceContainedLiquid";
        final String TRY_PLACE_LIQUID_DESC = isObf ? "(Ladm;Lcj;)Z" : "(Lnet/minecraft/world/World;Lnet/minecraft/util/BlockPos;)Z";

        for (MethodNode method : classNode.methods)
        {
            if (method.name.equals(TRY_PLACE_LIQUID) && method.desc.equals(TRY_PLACE_LIQUID_DESC))
            {
                JumpInsnNode ifNode = (JumpInsnNode) ASMHelper.findFirstInstructionWithOpcode(method, IFEQ);

                InsnList toInject = new InsnList();
                toInject.add(new MethodInsnNode(INVOKESTATIC, Type.getInternalName(Hooks.class), "allowWater", "()Z", false));
                toInject.add(new JumpInsnNode(IFNE, ifNode.label));

                method.instructions.insert(ifNode, toInject);
            }
        }
    }

    public static void transformBlockLiquid(ClassNode classNode, boolean isObf)
    {
        final String CHECK_FOR_MIXING = isObf ? "e" : "checkForMixing";
        final String CHECK_FOR_MIXING_DESC = isObf ? "(Ladm;Lcj;Lalz;)Z" : "(Lnet/minecraft/world/World;Lnet/minecraft/util/BlockPos;Lnet/minecraft/block/state/IBlockState;)Z";

        for (MethodNode method : classNode.methods)
        {
            if (method.name.equals(CHECK_FOR_MIXING) && method.desc.equals(CHECK_FOR_MIXING_DESC))
            {
                JumpInsnNode cobbleIfNode = (JumpInsnNode) ASMHelper.findLastInstructionWithOpcode(method, IF_ICMPGT);
                JumpInsnNode obsidianIfNode = (JumpInsnNode) ASMHelper.findPreviousInstructionWithOpcode(cobbleIfNode, IFNE);

                InsnList toInject = new InsnList();
                toInject.add(new VarInsnNode(ALOAD, 1));
                toInject.add(new MethodInsnNode(INVOKESTATIC, Type.getInternalName(Hooks.class), "createObsidian", isObf ? "(Ladm;)Z" : "(Lnet/minecraft/world/World;)Z", false));
                toInject.add(new JumpInsnNode(IFEQ, obsidianIfNode.label));

                method.instructions.insert(obsidianIfNode, toInject);

                toInject = new InsnList();
                toInject.add(new VarInsnNode(ALOAD, 1));
                toInject.add(new MethodInsnNode(INVOKESTATIC, Type.getInternalName(Hooks.class), "createCobblestone", isObf ? "(Ladm;)Z" : "(Lnet/minecraft/world/World;)Z", false));
                toInject.add(new JumpInsnNode(IFEQ, cobbleIfNode.label));

                method.instructions.insert(cobbleIfNode, toInject);
            }
        }
    }

//    public static void transformCoFHBlockWater(ClassNode classNode, boolean isObf)
//        {
//            final String ON_BLOCK_ADDED = "onBlockAdded";
//            final String ON_BLOCK_ADDED_DESC = isObf ? "(Lahb;III)V" : "(Lnet/minecraft/world/World;III)V";
//
//            for (MethodNode method : classNode.methods)
//            {
//                if (method.name.equals(ON_BLOCK_ADDED) && method.desc.equals(ON_BLOCK_ADDED_DESC))
//                {
//                    JumpInsnNode ifNode = (JumpInsnNode) ASMHelper.findFirstInstructionWithOpcode(method, IFEQ);
//                    VarInsnNode aLoadNode = (VarInsnNode) ASMHelper.findPreviousInstructionWithOpcode(ifNode, ALOAD);
//
//                    InsnList toInject = new InsnList();
//                    toInject.add(new MethodInsnNode(INVOKESTATIC, Type.getInternalName(Hooks.class), "allowWater", "()Z", false));
//                    toInject.add(new JumpInsnNode(IFNE, ifNode.label));
//
//                    method.instructions.insertBefore(aLoadNode, toInject);
//                }
//            }
//        }

    public static void transformServerConfigurationManager(ClassNode classNode, boolean isObf)
    {
        final String RESPAWN_PLAYER = isObf ? "a" : "recreatePlayerEntity";
        final String RESPAWN_PLAYER_DESC = isObf ? "(Llf;IZ)Llf;" : "(Lnet/minecraft/entity/player/EntityPlayerMP;IZ)Lnet/minecraft/entity/player/EntityPlayerMP;";

        for (MethodNode method : classNode.methods)
        {
            if (method.name.equals(RESPAWN_PLAYER) && method.desc.equals(RESPAWN_PLAYER_DESC))
            {
                AbstractInsnNode aStoreNode = ASMHelper.findLastInstructionWithOpcode(method, ASTORE);

                InsnList toInject = new InsnList();
                String desc = isObf ? "(Lcj;Llf;)Lcj;" : "(Lnet/minecraft/util/BlockPos;Lnet/minecraft/entity/player/EntityPlayerMP;)Lnet/minecraft/util/BlockPos;";
                toInject.add(new VarInsnNode(ALOAD, 10));
                toInject.add(new VarInsnNode(ALOAD, 8));
                toInject.add(new MethodInsnNode(INVOKESTATIC, Type.getInternalName(Hooks.class), "getSpawnPoint", desc, false));
                toInject.add(new VarInsnNode(ASTORE, 10));

                method.instructions.insert(aStoreNode, toInject);
            }
        }
    }

    public static void transformTileEntityBeacon(ClassNode classNode, boolean isObf)
    {
        final String UPDATE_COLORS = isObf ? "B" : "updateSegmentColors";
        final String UPDATE_COLORS_DESC = "()V";

        for (MethodNode method : classNode.methods)
        {
            if (method.name.equals(UPDATE_COLORS) && method.desc.equals(UPDATE_COLORS))
            {
                JumpInsnNode ifNode = (JumpInsnNode) ASMHelper.findPreviousInstructionWithOpcode(ASMHelper.findFirstInstructionWithOpcode(method, IF_ICMPGT), IFEQ);
                AbstractInsnNode insertNode = ASMHelper.findPreviousInstructionWithOpcode(ifNode, ALOAD);

                LabelNode jump = new LabelNode();

                InsnList toInject = new InsnList();
                toInject.add(new MethodInsnNode(INVOKESTATIC, Type.getInternalName(Hooks.class), "shouldBeaconCheckForSky", "()Z", false));
                toInject.add(new JumpInsnNode(IFNE, jump));

                method.instructions.insertBefore(insertNode, toInject);
                method.instructions.insert(ifNode, jump);
            }
        }
    }

    public static void transformWorldServer(ClassNode classNode, boolean isObf)
    {
        final String TICK = isObf ? "c" : "tick";
        final String TICK_DESC = "()V";

        for (MethodNode method : classNode.methods)
        {
            if (method.name.equals(TICK) && method.desc.equals(TICK_DESC))
            {
                AbstractInsnNode ldcNode = ASMHelper.findPreviousInstructionWithOpcode(ASMHelper.findFirstInstructionWithOpcode(method, POP), LDC);

                LabelNode jump = new LabelNode();

                InsnList toInject = new InsnList();
                toInject.add(new MethodInsnNode(INVOKESTATIC, Type.getInternalName(Hooks.class), "getSpawnTicksWait", "()J", false));
                toInject.add(new JumpInsnNode(GOTO, jump));

                method.instructions.insertBefore(ldcNode, toInject);
                method.instructions.insert(ldcNode, jump);
            }
        }
    }

    public static void transformBlockCrops(ClassNode classNode, boolean isObf)
    {
        final String UPDATE_TICK = isObf ? "b" : "updateTick";
        final String UPDATE_TICK_DESC = isObf ? "(Ladm;Lcj;Lalz;Ljava/util/Random;)V" : "(Lnet/minecraft/world/World;Lnet/minecraft/util/BlockPos;Lnet/minecraft/block/state/IBlockState;Ljava/util/Random;)V";

        for (MethodNode method : classNode.methods)
        {
            if (method.name.equals(UPDATE_TICK) && method.desc.equals(UPDATE_TICK_DESC))
            {
                AbstractInsnNode lightValNode = ASMHelper.findFirstInstructionWithOpcode(method, BIPUSH);

                LabelNode jump = new LabelNode();

                InsnList toInject = new InsnList();
                toInject.add(new MethodInsnNode(INVOKESTATIC, Type.getInternalName(Hooks.class), "getMinimumLightCrops", "()I", false));
                toInject.add(new JumpInsnNode(GOTO, jump));

                method.instructions.insertBefore(lightValNode, toInject);
                method.instructions.insert(lightValNode, jump);
            }
        }
    }

    public static void transformBlockSapling(ClassNode classNode, boolean isObf)
    {
        final String UPDATE_TICK = isObf ? "b" : "updateTick";
        final String UPDATE_TICK_DESC = isObf ? "(Ladm;Lcj;Lalz;Ljava/util/Random;)V" : "(Lnet/minecraft/world/World;Lnet/minecraft/util/BlockPos;Lnet/minecraft/block/state/IBlockState;Ljava/util/Random;)V";

        for (MethodNode method : classNode.methods)
        {
            if (method.name.equals(UPDATE_TICK) && method.desc.equals(UPDATE_TICK_DESC))
            {
                AbstractInsnNode lightValNode = ASMHelper.findFirstInstructionWithOpcode(method, BIPUSH);

                LabelNode jump = new LabelNode();

                InsnList toInject = new InsnList();
                toInject.add(new MethodInsnNode(INVOKESTATIC, Type.getInternalName(Hooks.class), "getMinimumLightSapling", "()I", false));
                toInject.add(new JumpInsnNode(GOTO, jump));

                method.instructions.insertBefore(lightValNode, toInject);
                method.instructions.insert(lightValNode, jump);
            }
        }
    }

    public static void transformSpawnerAnimals(ClassNode classNode, boolean isObf)
    {
        final String FIND_CHUNKS = isObf ? "a" : "findChunksForSpawning";
        final String FIND_CHUNKS_DESC = isObf ? "(Lle;ZZZ)I" : "(Lnet/minecraft/world/WorldServer;ZZZ)I";

        for (MethodNode method : classNode.methods)
        {
            if (method.name.equals(FIND_CHUNKS) && method.desc.equals(FIND_CHUNKS_DESC))
            {
                AbstractInsnNode maxChunkRadiusNode = ASMHelper.findFirstInstructionWithOpcode(method, BIPUSH);

                InsnList toInject = new InsnList();
                toInject.add(new MethodInsnNode(INVOKESTATIC, Type.getInternalName(Hooks.class), "getMaxChunkRadius", "()B", false));

                method.instructions.insertBefore(maxChunkRadiusNode, toInject);

                ASMHelper.skipInstructions(method.instructions, maxChunkRadiusNode, maxChunkRadiusNode.getNext());
            }
        }
    }

    public static void transformBlockEndPortal(ClassNode classNode, boolean isObf)
    {
        final String ON_ENTITY_COLLIDE = isObf ? "a" : "onEntityCollidedWithBlock";
        final String ON_ENTITY_COLLIDE_DESC = isObf ? "(Ladm;Lcj;Lalz;Lpk;)V" : "(Lnet/minecraft/world/World;Lnet/minecraft/util/BlockPos;Lnet/minecraft/block/state/IBlockState;Lnet/minecraft/entity/Entity;)V";

        for (MethodNode method : classNode.methods)
        {
            if (method.name.equals(ON_ENTITY_COLLIDE) && method.desc.equals(ON_ENTITY_COLLIDE_DESC))
            {
                JumpInsnNode ifNode = (JumpInsnNode) ASMHelper.findFirstInstructionWithOpcode(method, IFNONNULL);

                InsnList toInject = new InsnList();
                toInject.add(new MethodInsnNode(INVOKESTATIC, Type.getInternalName(Hooks.class), "shouldEndPortalTeleport", "()Z", false));
                toInject.add(new JumpInsnNode(IFEQ, ifNode.label));

                method.instructions.insert(ifNode, toInject);
            }
        }
    }

    public static void transformItemEnderEye(ClassNode classNode, boolean isObf)
    {
        final String ON_ENTITY_COLLIDE = isObf ? "a" : "onItemUse";
        final String ON_ENTITY_COLLIDE_DESC = isObf ? "(Lzx;Lwn;Ladm;Lcj;Lcq;FFF)Z" : "(Lnet/minecraft/item/ItemStack;Lnet/minecraft/entity/player/EntityPlayer;Lnet/minecraft/world/World;Lnet/minecraft/util/BlockPos;Lnet/minecraft/util/EnumFacing;FFF)Z";

        for (MethodNode method : classNode.methods)
        {
            if (method.name.equals(ON_ENTITY_COLLIDE) && method.desc.equals(ON_ENTITY_COLLIDE_DESC))
            {
                JumpInsnNode ifNode = (JumpInsnNode) ASMHelper.findLastInstructionWithOpcode(method, IFEQ);

                InsnList toInject = new InsnList();
                toInject.add(new MethodInsnNode(INVOKESTATIC, Type.getInternalName(Hooks.class), "shouldPortalBlockBeGenerated", "()Z", false));
                toInject.add(new JumpInsnNode(IFEQ, ifNode.label));

                method.instructions.insert(ifNode, toInject);
            }
        }
    }

    public static void transformEntityZombie(ClassNode classNode, boolean isObf)
    {
        final String LIVING_UPDATE = isObf ? "m" : "onLivingUpdate";
        final String LIVING_UPDATE_DESC = "()V";

        for (MethodNode method : classNode.methods)
        {
            if (method.name.equals(LIVING_UPDATE) && method.desc.equals(LIVING_UPDATE_DESC))
            {
                JumpInsnNode ifNode = (JumpInsnNode) ASMHelper.findFirstInstructionWithOpcode(method, IFEQ);

                InsnList toInject = new InsnList();
                toInject.add(new MethodInsnNode(INVOKESTATIC, Type.getInternalName(Hooks.class), "shouldZombiesBurn", "()Z", false));
                toInject.add(new JumpInsnNode(IFEQ, ifNode.label));

                method.instructions.insert(ifNode, toInject);
            }
        }
    }

    public static void transformEntitySkeleton(ClassNode classNode, boolean isObf)
    {
        final String LIVING_UPDATE = isObf ? "m" : "onLivingUpdate";
        final String LIVING_UPDATE_DESC = "()V";

        for (MethodNode method : classNode.methods)
        {
            if (method.name.equals(LIVING_UPDATE) && method.desc.equals(LIVING_UPDATE_DESC))
            {
                JumpInsnNode ifNode = (JumpInsnNode) ASMHelper.findFirstInstructionWithOpcode(method, IFEQ);

                InsnList toInject = new InsnList();
                toInject.add(new MethodInsnNode(INVOKESTATIC, Type.getInternalName(Hooks.class), "shouldSkeletonsBurn", "()Z", false));
                toInject.add(new JumpInsnNode(IFEQ, ifNode.label));

                method.instructions.insert(ifNode, toInject);
            }
        }
    }

    public static void transformEntityPlayerSP(ClassNode classNode, boolean isObf)
    {
        final String LIVING_UPDATE = isObf ? "m" : "onLivingUpdate";
        final String LIVING_UPDATE_DESC = "()V";

        for (MethodNode method : classNode.methods)
        {
            if (method.name.equals(LIVING_UPDATE) && method.desc.equals(LIVING_UPDATE_DESC))
            {
                JumpInsnNode ifNode = (JumpInsnNode) ASMHelper.findFirstInstructionWithOpcode(method, IFLE);

                InsnList toInject = new InsnList();
                toInject.add(new MethodInsnNode(INVOKESTATIC, Type.getInternalName(HooksClient.class), "shouldStopSprinting", "()Z", false));
                toInject.add(new JumpInsnNode(IFEQ, ifNode.label));

                method.instructions.insert(ifNode, toInject);

                boolean found = false;
                AbstractInsnNode node = method.instructions.getFirst();
                while (!found) {
                    node = ASMHelper.findNextInstructionWithOpcode(node, LDC);
                    if (((LdcInsnNode)node).cst instanceof Float) {
                        Float f = (Float)((LdcInsnNode)node).cst;
                        if (f == 6.0F) {
                            found = true;
                        }
                    }
                }

                MethodInsnNode toAdd = new MethodInsnNode(INVOKESTATIC, Type.getInternalName(HooksClient.class), "getMinimumHunger", "()F", false);
                method.instructions.insert(node, toAdd);

                ASMHelper.skipInstructions(method.instructions, node, node.getNext());
            }
        }
    }
}
