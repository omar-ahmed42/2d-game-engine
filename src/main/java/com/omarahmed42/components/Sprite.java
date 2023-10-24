package com.omarahmed42.components;

import org.joml.Vector2f;

import com.omarahmed42.renderer.Texture;

public class Sprite {

    private float width;
    private float height;

    private Texture texture = null;
    private Vector2f[] texCoords = {
            new Vector2f(1, 1),
            new Vector2f(1, 0),
            new Vector2f(0, 0),
            new Vector2f(0, 1)
    };

    public Texture getTexture() {
        return this.texture;
    }

    public Vector2f[] getTexCoords() {
        return this.texCoords;
    }

    public void setTexture(Texture texture) {
        this.texture = texture;
    }

    public void setTexCoords(Vector2f[] texCoords) {
        this.texCoords = texCoords;
    }

    public float getHeight() {
        return this.height;
    }

    public float getWidth() {
        return this.width;
    }

    public void setWidth(float spriteWidth) {
        this.width = spriteWidth;
    }

    public void setHeight(float spriteHeight) {
        this.height = spriteHeight;
    }

    public int getTexId() {
        return texture == null ? -1 : texture.getId();
    }
}
