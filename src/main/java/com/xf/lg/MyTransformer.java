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

                    // === 在构造函数结尾注入：注册数据包 + 命令 ===
                    if (mName.equals("<init>")) {
                        Log.ok("hook 到构造函数 " + desc);
                        return new MethodVisitor(Opcodes.ASM9, mv) {
                            @Override
                            public void visitInsn(int opcode) {
                                // 在构造函数 return 之前插入
                                if (opcode == Opcodes.RETURN) {
                                    // WorldHook.onWorldLoad(this);
                                    visitVarInsn(Opcodes.ALOAD, 0);
                                    visitMethodInsn(Opcodes.INVOKESTATIC,
                                        "com/xf/lg/hook/WorldHook", "onWorldLoad",
                                        "(Ljava/lang/Object;)V", false);

                                    // CommandHook.register(this);
                                    visitVarInsn(Opcodes.ALOAD, 0);
                                    visitMethodInsn(Opcodes.INVOKESTATIC,
                                        "com/xf/lg/hook/CommandHook", "register",
                                        "(Ljava/lang/Object;)V", false);
                                }
                                super.visitInsn(opcode);
                            }
                        };
                    }

                    // === loadLevel 里不再需要注入（构造函数已经做了） ===
                    return mv;
                }
            };
            cr.accept(cv, 0);
            Log.ok("改完");
            return cw.toByteArray();
        } catch (Throwable t) {
            Log.fail("改类炸了: " + t);
            t.printStackTrace();
            return null;
        }
    }
}
