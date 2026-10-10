package com.xf.lg;

import org.objectweb.asm.*;
import java.lang.instrument.ClassFileTransformer;
import java.security.ProtectionDomain;

public class ScreenTransformer implements ClassFileTransformer {

    private static final String TARGET =
        "net/minecraft/client/gui/screen/multiplayer/MultiplayerScreen";

    @Override
    public byte[] transform(ClassLoader loader, String name,
                            Class<?> c, ProtectionDomain pd, byte[] buf) {
        if (!TARGET.equals(name)) return null;
        System.out.println("[xiaofang] 改多人界面: " + name);

        ClassReader cr = new ClassReader(buf);
        ClassWriter cw = new ClassWriter(cr, ClassWriter.COMPUTE_FRAMES);
        ClassVisitor cv = new ClassVisitor(Opcodes.ASM9, cw) {
            @Override
            public MethodVisitor visitMethod(int acc, String mName, String desc,
                                             String sig, String[] ex) {
                System.out.println("[xiaofang] MPS 方法: " + mName + " " + desc);
                return super.visitMethod(acc, mName, desc, sig, ex);
            }
        };
        cr.accept(cv, 0);
        return cw.toByteArray();
    }
}
