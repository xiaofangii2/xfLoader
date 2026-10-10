package com.xf.lg;

import org.objectweb.asm.*;
import java.lang.instrument.ClassFileTransformer;
import java.security.ProtectionDomain;

public class PlayerJoinTransformer implements ClassFileTransformer {

    private static final String TARGET = "net/minecraft/server/players/PlayerList";

    @Override
    public byte[] transform(ClassLoader loader, String name,
                            Class<?> c, ProtectionDomain pd, byte[] buf) {
        if (!TARGET.equals(name)) return null;
        Log.i("改 PlayerList (" + buf.length + " bytes)");

        try {
            ClassReader cr = new ClassReader(buf);
            ClassWriter cw = new ClassWriter(cr, ClassWriter.COMPUTE_MAXS);
            ClassVisitor cv = new ClassVisitor(Opcodes.ASM9, cw) {
                @Override
                public MethodVisitor visitMethod(int acc, String mName, String desc,
                                                 String sig, String[] ex) {
                    MethodVisitor mv = super.visitMethod(acc, mName, desc, sig, ex);
                    if (!mName.equals("placeNewPlayer")) return mv;
                    Log.ok("hook 到 placeNewPlayer " + desc);
                    return new MethodVisitor(Opcodes.ASM9, mv) {
                        @Override
                        public void visitCode() {
                            super.visitCode();
                            visitVarInsn(Opcodes.ALOAD, 0);
                            visitVarInsn(Opcodes.ALOAD, 2);
                            visitMethodInsn(Opcodes.INVOKESTATIC,
                                "com/xf/lg/hook/PostJoinHook", "onPlayerJoin",
                                "(Ljava/lang/Object;Ljava/lang/Object;)V", false);
                        }
                    };
                }
            };
            cr.accept(cv, 0);
            Log.ok("PlayerList 改完");
            return cw.toByteArray();
        } catch (Throwable t) {
            Log.fail("改 PlayerList 炸了: " + t);
            return null;
        }
    }
}
