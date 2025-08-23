/*
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software Foundation,
 * either version 3 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program.
 * If not, see <https://www.gnu.org/licenses/>.
 * */
package hfnl.launch.fabric;

import net.fabricmc.loader.impl.game.patch.GamePatch;
import net.fabricmc.loader.impl.launch.FabricLauncher;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;

import java.util.ListIterator;
import java.util.function.Consumer;
import java.util.function.Function;

public class EntrypointPatch extends GamePatch {

    @SuppressWarnings("ALL")
    @Override
    public void process(FabricLauncher launcher, Function<String, ClassNode> classSource, Consumer<ClassNode> classEmitter) {
        try {
            ClassNode mainClass = classSource.apply(launcher.getEntrypoint());

            MethodNode initMethod = findMainMethod(mainClass, method -> "main".equals(method.name));

            ListIterator<AbstractInsnNode> it = initMethod.instructions.iterator();

            it.add(new MethodInsnNode(Opcodes.INVOKESTATIC, Hooks.class.getName().replace('.', '/'), "init", "()V", false));

            classEmitter.accept(mainClass);
        } catch (Throwable t) {
            throw new RuntimeException(t);
        }
    }

    private MethodNode findMainMethod(ClassNode classNode, Function<MethodNode, Boolean> predicate) {
        if (classNode.methods == null) {
            return null;
        }
        for (MethodNode method : classNode.methods) {
            if (predicate.apply(method)) {
                return method;
            }
        }
        return null;
    }
}