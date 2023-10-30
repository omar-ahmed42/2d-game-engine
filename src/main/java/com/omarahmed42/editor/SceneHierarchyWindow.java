package com.omarahmed42.editor;

import java.util.List;

import com.omarahmed42.main.GameObject;
import com.omarahmed42.main.Window;

import imgui.ImGui;
import imgui.flag.ImGuiTreeNodeFlags;

public class SceneHierarchyWindow {

    public void imgui() {
        ImGui.begin("Scene hierarchy");

        List<GameObject> gameObjects = Window.getScene().getGameObjects();
        int index = 0;
        for (GameObject obj : gameObjects) {
            if (!obj.doSerialization()) {
                continue;
            }

            ImGui.pushID(index);
            boolean treeNodeOpen = ImGui.treeNodeEx(obj.name,
                    ImGuiTreeNodeFlags.DefaultOpen | ImGuiTreeNodeFlags.FramePadding | ImGuiTreeNodeFlags.OpenOnArrow
                            | ImGuiTreeNodeFlags.SpanAvailWidth,
                    obj.name);
            ImGui.popID();
            if (treeNodeOpen) {
                ImGui.treePop();
            }
            index++;
        }

        ImGui.end();
    }
}
