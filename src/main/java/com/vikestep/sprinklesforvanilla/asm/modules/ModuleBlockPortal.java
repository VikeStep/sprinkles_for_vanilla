package com.vikestep.sprinklesforvanilla.asm.modules;

import com.vikestep.sprinklesforvanilla.common.util.LogHelper;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.MethodNode;

import static org.objectweb.asm.Opcodes.*;

public class ModuleBlockPortal
{
    private static final String BLOCK_PORTAL_UPDATE_TICK_DEOBF      = "updateTick";
    private static final String BLOCK_PORTAL_UPDATE_TICK_OBF        = "func_149674_a";
    private static final String BLOCK_PORTAL_GET_SIZE_DEOBF         = "func_150000_e";
    private static final String BLOCK_PORTAL_GET_SIZE_OBF           = "func_150000_e";
    private static final String BLOCK_PORTAL_ENTITY_COLLIDE_DEOBF   = "onEntityCollidedWithBlock";
    private static final String BLOCK_PORTAL_ENTITY_COLLIDE_OBF   = "func_149670_a";

    public static byte[] transform(byte[] portalClass, boolean isObfuscated)
    {
        LogHelper.log("Transforming Portal Class");

        ClassNode classNode = ASMHelper.readClassFromBytes(portalClass);

        for(MethodNode method : classNode.methods)
        {
            if (method.name.equals(BLOCK_PORTAL_UPDATE_TICK_DEOBF) || method.name.equals(BLOCK_PORTAL_UPDATE_TICK_OBF))
            {
                transformUpdateTick(classNode, method, isObfuscated);
            }
            else if (method.name.equals(BLOCK_PORTAL_GET_SIZE_DEOBF) || method.name.equals(BLOCK_PORTAL_GET_SIZE_OBF))
            {
                transformGetSize(classNode, method, isObfuscated);
            }
            else if (method.name.equals(BLOCK_PORTAL_ENTITY_COLLIDE_DEOBF) || method.name.equals(BLOCK_PORTAL_ENTITY_COLLIDE_OBF))
            {
                transformEntityCollide(classNode, method, isObfuscated);
            }
        }

        return ASMHelper.writeClassToBytes(classNode);
    }

    private static void transformUpdateTick(ClassNode classNode, MethodNode method, boolean isObfuscated)
    {
        LogHelper.log("Method " + method.name + " found!");
        method.instructions.clear();
        method.instructions.add(new InsnNode(RETURN));
    }

    private static void transformGetSize(ClassNode classNode, MethodNode method, boolean isObfuscated)
    {
        LogHelper.log("Method " + method.name + " found!");
        method.instructions.clear();
        method.instructions.add(new InsnNode(ICONST_0));
        method.instructions.add(new InsnNode(IRETURN));
    }

    private static void transformEntityCollide(ClassNode classNode, MethodNode method, boolean isObfuscated)
    {
        LogHelper.log("Method " + method.name + " found!");
        /*method.instructions.clear();
        method.instructions.add(new InsnNode(RETURN));*/
        /*
        Changing:
        FROM if(p_149670_5_.ridingEntity == null && p_149670_5_.riddenByEntity == null)
        TO if(p_149670_5_.ridingEntity == null && p_149670_5_.riddenByEntity == null && HookBlockPortal.isNetherAllowed())
         */

        AbstractInsnNode isRiding = ASMHelper.findFirstInstructionWithOpcode(method, IFEQ);
    }
}
