package com.omarahmed42.editor;

import com.omarahmed42.main.GameObject;
import com.omarahmed42.main.MouseListener;
import com.omarahmed42.renderer.PickingTexture;
import com.omarahmed42.scenes.Scene;

import imgui.ImGui;

import static org.lwjgl.glfw.GLFW.*;

public class PropertiesWindow {
    private GameObject activeGameObject = null;
    private PickingTexture pickingTexture;

    public PropertiesWindow(PickingTexture pickingTexture) {
        this.pickingTexture = pickingTexture;
    }

    public void update(float dt, Scene currentScene) {
        if (MouseListener.mouseButtonDown(GLFW_MOUSE_BUTTON_LEFT)) {
            int x = (int) MouseListener.getScreenX();
            int y = (int) MouseListener.getScreenY();
            int gameObjectId = pickingTexture.readPixel(x, y);
            activeGameObject = currentScene.getGameObject(gameObjectId);
        }
    }

    public void imgui() {
        if (activeGameObject != null) {
            ImGui.begin("Properties");
            activeGameObject.imgui();
            ImGui.end();
        }
    }

}
