package com.omarahmed42.scenes;

import org.joml.Vector2f;

import com.omarahmed42.components.EditorCamera;
import com.omarahmed42.components.GizmoSystem;
import com.omarahmed42.components.GridLines;
import com.omarahmed42.components.MouseControls;
import com.omarahmed42.components.Sprite;
import com.omarahmed42.components.SpriteRenderer;
import com.omarahmed42.components.Spritesheet;
import com.omarahmed42.main.GameObject;
import com.omarahmed42.main.Prefabs;
import com.omarahmed42.util.AssetPool;

import imgui.ImGui;
import imgui.ImVec2;

public class LevelEditorSceneInitializer extends SceneInitializer {

    private Spritesheet sprites;

    private GameObject levelEditorStuff;

    public LevelEditorSceneInitializer() {
    }

    @Override
    public void init(Scene scene) {
        this.sprites = AssetPool.getSpritesheet("assets/images/spritesheets/decorationsAndBlocks.png");
        Spritesheet gizmos = AssetPool.getSpritesheet("assets/images/gizmos.png");

        levelEditorStuff = scene.createGameObject("LevelEditor");
        levelEditorStuff.setNoSerialize();
        levelEditorStuff.addComponent(new MouseControls());
        levelEditorStuff.addComponent(new GridLines());
        levelEditorStuff.addComponent(new EditorCamera(scene.camera()));
        levelEditorStuff.addComponent(new GizmoSystem(gizmos));
        scene.addGameObjectToScene(levelEditorStuff);
    }

    @Override
    public void loadResources(Scene scene) {
        AssetPool.getShader("assets/shaders/default.glsl");
        AssetPool.addSpritesheet("assets/images/spritesheets/decorationsAndBlocks.png",
                new Spritesheet(AssetPool.getTexture("assets/images/spritesheets/decorationsAndBlocks.png"), 16, 16, 81,
                        0));
        AssetPool.addSpritesheet("assets/images/gizmos.png",
                new Spritesheet(
                        AssetPool.getTexture("assets/images/gizmos.png"), 24, 48, 3, 0));
        AssetPool.getTexture("assets/images/blendImage2.png");

        for (GameObject g : scene.getGameObjects()) {
            if (g.getComponent(SpriteRenderer.class) != null) {
                SpriteRenderer spr = g.getComponent(SpriteRenderer.class);
                if (spr.getTexture() != null) {
                    spr.setTexture(AssetPool.getTexture(spr.getTexture().getFilePath()));
                }
            }
        }
    }

    @Override
    public void imgui() {
        ImGui.begin("Level Editor Stuff");
        levelEditorStuff.imgui();
        ImGui.end();

        ImGui.begin("Test window");

        ImVec2 windowPos = new ImVec2();
        ImGui.getWindowPos(windowPos);

        ImVec2 windowSize = new ImVec2();
        ImGui.getWindowSize(windowSize);

        ImVec2 itemSpacing = new ImVec2();
        ImGui.getStyle().getItemSpacing(itemSpacing);

        float windowX2 = windowPos.x + windowSize.x;
        for (int i = 0; i < sprites.size(); i++) {
            Sprite sprite = sprites.getSprite(i);
            float spriteWidth = sprite.getWidth() * 2;
            float spriteHeight = sprite.getHeight() * 2;

            int id = sprite.getTexId();
            Vector2f[] texCoords = sprite.getTexCoords();

            ImGui.pushID(i);
            if (ImGui.imageButton(id, spriteWidth, spriteHeight, texCoords[2].x, texCoords[0].y,
                    texCoords[0].x,
                    texCoords[2].y)) {
                GameObject object = Prefabs.generateSpriteObject(sprite, 0.25f, 0.25f);
                // Attach this to the mouse cursor
                levelEditorStuff.getComponent(MouseControls.class).pickupObject(object);
            }
            ImGui.popID();

            ImVec2 lastButtonPos = new ImVec2();
            ImGui.getItemRectMax(lastButtonPos);

            float lastButtonX2 = lastButtonPos.x;
            float nextButtonX2 = lastButtonX2 + itemSpacing.x + spriteWidth;
            if (i + 1 < sprites.size() && nextButtonX2 < windowX2) {
                ImGui.sameLine();
            }
        }
        ImGui.end();
    }

}
