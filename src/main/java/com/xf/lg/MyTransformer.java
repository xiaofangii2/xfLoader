package com.xf.lg;

import org.objectweb.asm.*;
import java.lang.instrument.ClassFileTransformer;
import java.security.ProtectionDomain;

public class MyTransformer implements ClassFileTransformer {

    private static final String TARGET = "net/minecraft/server/MinecraftServer";

    @Override
    public byte[] transform(ClassLoader loader, String name,
                            Class<?> c, ProtectionDomain pd, byte[] buf) {
        if (!TARGET.equals(name)) return null;
        Log.i("改: " + name + " (" + buf.length + " bytes)");

        try {
            ClassReader cr = new ClassReader(buf);
            ClassWriter cw = new ClassWriter(cr, ClassWriter.COMPUTE_MAXS);
            ClassVisitor cv = new ClassVisitor(Opcodes.ASM9, cw) {
                @Override
                public MethodVisitor visitMethod(int acc, String mName, String desc,
                                                 String sig, String[] ex) {
                    MethodVisitor mv = super.visitMethod(acc, mName, desc, sig, ex);
                    if (!mName.equals("<init>")) return mv;
                    Log.ok("hook 到 <init>");
                    return new MethodVisitor(Opcodes.ASM9, mv) {
                        @Override
                        public void visitInsn(int opcode) {
                            if (opcode == Opcodes.RETURN) {
                                visitVarInsn(Opcodes.ALOAD, 0);
                                visitMethodInsn(Opcodes.INVOKESTATIC,
                                    "com/xf/lg/hook/WorldHook", "onWorldLoad",
                                    "(Ljava/lang/Object;)V", false);
                            }
                            super.visitInsn(opcode);
                        }
                    };
                }
            };
            cr.accept(cv, 0);
            Log.ok("改完");
            return cw.toByteArray();
        } catch (Throwable t) {
            Log.fail("改类炸了: " + t);
            return null;
        }
    }
}
