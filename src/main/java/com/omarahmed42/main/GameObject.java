package com.omarahmed42.main;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.omarahmed42.components.Component;
import com.omarahmed42.components.ComponentDeserializer;
import com.omarahmed42.components.SpriteRenderer;
import com.omarahmed42.util.AssetPool;

import imgui.ImGui;

public class GameObject {
    private static int ID_COUNTER = 0;
    private int uid = -1;

    public String name;
    private List<Component> components;
    public transient Transform transform;
    private boolean doSerialization = true;
    private boolean isDead = false;
        
    public GameObject(String name) {
        this.name = name;
        this.components = new ArrayList<>();

        this.uid = ID_COUNTER++;
    }
    
    public <T extends Component> T getComponent(Class<T> componentClass) {
        for (Component component : components) {
            if (componentClass.isAssignableFrom(component.getClass())) {
                try {
                    return componentClass.cast(component);
                } catch (ClassCastException e) {
                    e.printStackTrace();
                    assert false : "Error: Casting component.";
                }
            }
        }
        return null;
    }
    
    public <T extends Component> void removeComponent(Class<T> componentClass) {
        for (int i = 0; i < components.size(); i++) {
            Component c = components.get(i);
            if (componentClass.isAssignableFrom(c.getClass())) {
                components.remove(i);
                return;
            }
        }
    }
    
    public void addComponent(Component c) {
        c.generateId();
        this.components.add(c);
        c.gameObject = this;
    }
    
    public void editorUpdate(float dt) {
        for (int i = 0; i < components.size(); i++) {
            components.get(i).editorUpdate(dt);
        }
    }
    
    public void update(float dt) {
        for (int i = 0; i < components.size(); i++) {
            components.get(i).update(dt);
        }
    }
    
    public void start() {
        for (int i = 0; i < components.size(); i++) {
            components.get(i).start();
        }
    }

    public void imgui() {
        for (Component c : components) {
            if (ImGui.collapsingHeader(c.getClass().getSimpleName()))
                c.imgui();
        }
    }

    public static void init(int maxId) {
        ID_COUNTER = maxId;
    }
    
    public void destroy() {
        this.isDead = true;
        for (int i = 0; i < components.size(); i++) {
            components.get(i).destroy();
        }
    }

    public int getUid() {
        return this.uid;
    }

    public List<Component> getAllComponents() {
        return this.components;
    }

    public void setNoSerialize() {
        this.doSerialization = false;
    }

    public boolean doSerialization() {
        return this.doSerialization;
    }

    public boolean isDead() {
        return this.isDead;
    }

    public void generateUid() {
        this.uid = ID_COUNTER++;
    }

    public GameObject copy() {
        Gson gson = new GsonBuilder().setPrettyPrinting()
                .registerTypeAdapter(Component.class, new ComponentDeserializer())
                .registerTypeAdapter(GameObject.class, new GameObjectDeserializer())
                .enableComplexMapKeySerialization()
                .create();
        String objAsJsn = gson.toJson(this);
        GameObject obj = gson.fromJson(objAsJsn, GameObject.class);
        obj.generateUid();
        for (Component c : obj.getAllComponents()) {
            c.generateId();
        }

        SpriteRenderer sprite = obj.getComponent(SpriteRenderer.class);
        if (sprite != null && sprite.getTexture() != null) {
            sprite.setTexture(AssetPool.getTexture(sprite.getTexture().getFilePath()));
        }

        return obj;
    }
}
