package com.omarahmed42.editor;

import com.omarahmed42.observers.EventSystem;
import com.omarahmed42.observers.events.Event;
import com.omarahmed42.observers.events.EventType;

import imgui.ImGui;

public class MenuBar {
    public void imgui() {
        ImGui.beginMenuBar();

        if (ImGui.beginMenu("File")) {
            if (ImGui.menuItem("Save", "CTRL+S")) {
                EventSystem.notify(null, new Event(EventType.SaveLevel));
            }

            if (ImGui.menuItem("Load", "CTRL+O")) {
                EventSystem.notify(null, new Event(EventType.LoadLevel));
            }

            ImGui.endMenu();
        }
        ImGui.endMenuBar();
    }
}
