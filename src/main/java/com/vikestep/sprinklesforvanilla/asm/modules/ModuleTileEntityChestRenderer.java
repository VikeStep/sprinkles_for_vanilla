package com.vikestep.sprinklesforvanilla.asm.modules;

import com.vikestep.sprinklesforvanilla.asm.hooks.HookTileEntityChestRenderer;
import com.vikestep.sprinklesforvanilla.common.util.LogHelper;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.*;
import squeek.asmhelper.ASMHelper;

import static org.objectweb.asm.Opcodes.*;

public class ModuleTileEntityChestRenderer
{
    private static final String INIT_METHOD     = "<init>";
    private static final String INIT_METHOD_SIG = "()V";

    public static byte[] transform(byte[] chestRendererClass, boolean isObfuscated)
    {
        LogHelper.log("Transforming Chest Renderer Class");

        ClassNode classNode = ASMHelper.readClassFromBytes(chestRendererClass);

        MethodNode methodNode = ASMHelper.findMethodNodeOfClass(classNode, INIT_METHOD, INIT_METHOD_SIG);
        if (methodNode != null)
        {
            transformInit(methodNode, isObfuscated);
        }

        return ASMHelper.writeClassToBytes(classNode);
    }

    //Replaces the christmas chest functionality with HookTileEntityChestRenderer.isChristmasChest();
    private static void transformInit(MethodNode method, boolean isObfuscated)
    {
        AbstractInsnNode calendarStartNode = ASMHelper.findFirstInstructionWithOpcode(method, INVOKESTATIC).getPrevious();
        JumpInsnNode ifStatementNode = (JumpInsnNode) ASMHelper.findFirstInstructionWithOpcode(method, IF_ICMPGT);
        LabelNode ifStatementEndLabel = ifStatementNode.label;

        ASMHelper.removeFromInsnListUntil(method.instructions, calendarStartNode.getNext(), ifStatementNode.getNext());

        InsnList toInject = new InsnList();
        toInject.add(new MethodInsnNode(INVOKESTATIC, Type.getInternalName(HookTileEntityChestRenderer.class), "isChristmasChest", "()Z", false));
        toInject.add(new JumpInsnNode(IFEQ, ifStatementEndLabel));

        method.instructions.insert(calendarStartNode, toInject);
    }
}