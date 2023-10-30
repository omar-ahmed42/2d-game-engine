package com.omarahmed42.components;

import org.lwjgl.glfw.GLFW;

import com.omarahmed42.main.KeyListener;
import com.omarahmed42.main.Window;

public class GizmoSystem extends Component {
    private Spritesheet gizmos;
    private int usingGizmo = 0;

    public GizmoSystem(Spritesheet gizmoSprites) {
        gizmos = gizmoSprites;
    }

    @Override
    public void start() {
        gameObject.addComponent(
                new TranslateGizmo(gizmos.getSprite(1), Window.get().getImGuiLayer().getPropertiesWindow()));

        gameObject
                .addComponent(new ScaleGizmo(gizmos.getSprite(2), Window.get().getImGuiLayer().getPropertiesWindow()));
    }

    @Override
    public void editorUpdate(float dt) {
        if (usingGizmo == 0) {
            gameObject.getComponent(TranslateGizmo.class).setUsing();
            gameObject.getComponent(ScaleGizmo.class).setNotUsing();
        } else if (usingGizmo == 1) {
            gameObject.getComponent(ScaleGizmo.class).setUsing();
            gameObject.getComponent(TranslateGizmo.class).setNotUsing();
        }

        if (KeyListener.isKeyPressed(GLFW.GLFW_KEY_E)) {
            usingGizmo = 0;
        } else if (KeyListener.isKeyPressed(GLFW.GLFW_KEY_R)) {
            usingGizmo = 1;
        }
    }
}
