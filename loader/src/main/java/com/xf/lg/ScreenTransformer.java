package com.xf.lg;

import org.objectweb.asm.*;
import java.lang.instrument.ClassFileTransformer;
import java.security.ProtectionDomain;

public class ScreenTransformer implements ClassFileTransformer {

    private static final String[] TARGETS = {
        "net/minecraft/client/gui/screens/TitleScreen",
        "net/minecraft/client/gui/screens/PauseScreen"
    };

    @Override
    public byte[] transform(ClassLoader loader, String name,
                            Class<?> c, ProtectionDomain pd, byte[] buf) {
        boolean isTarget = false;
        for (String t : TARGETS) {
            if (t.equals(name)) { isTarget = true; break; }
        }
        if (!isTarget) return null;

        Log.i("改 Screen: " + name);

        try {
            ClassReader cr = new ClassReader(buf);
            ClassWriter cw = new ClassWriter(cr, ClassWriter.COMPUTE_MAXS);
            ClassVisitor cv = new ClassVisitor(Opcodes.ASM9, cw) {
                @Override
                public MethodVisitor visitMethod(int acc, String mName, String desc,
                                                 String sig, String[] ex) {
                    MethodVisitor mv = super.visitMethod(acc, mName, desc, sig, ex);
                    if (!mName.equals("init") || !desc.equals("()V")) return mv;

                    Log.ok("hook 到 " + name + ".init");
                    return new MethodVisitor(Opcodes.ASM9, mv) {
                        @Override
                        public void visitInsn(int opcode) {
                            if (opcode == Opcodes.RETURN) {
                                // this.addRenderableWidget(
                                //   ScreenHook.createButton(this));
                                visitVarInsn(Opcodes.ALOAD, 0);
                                visitVarInsn(Opcodes.ALOAD, 0);
                                visitMethodInsn(Opcodes.INVOKESTATIC,
                                    "com/xf/lg/gui/ScreenHook", "createButton",
                                    "(Lnet/minecraft/client/gui/screens/Screen;)"
                                    + "Lnet/minecraft/client/gui/components/Button;", false);
                                visitMethodInsn(Opcodes.INVOKEVIRTUAL,
                                    name, "addRenderableWidget",
                                    "(Lnet/minecraft/client/gui/components/events/GuiEventListener;)"
                                    + "Lnet/minecraft/client/gui/components/events/GuiEventListener;", false);
                                visitInsn(Opcodes.POP);
                            }
                            super.visitInsn(opcode);
                        }
                    };
                }
            };
            cr.accept(cv, 0);
            Log.ok("Screen 改完: " + name);
            return cw.toByteArray();
        } catch (Throwable t) {
            Log.fail("改 Screen 炸了: " + t);
            t.printStackTrace();
            return null;
        }
    }
}
