package com.omarahmed42.main;

import org.joml.Vector2f;

import com.omarahmed42.components.Sprite;
import com.omarahmed42.components.SpriteRenderer;

public class Prefabs {

    public static GameObject generateSpriteObject(Sprite sprite, float sizeX, float sizeY) {
        GameObject block = Window.getScene().createGameObject("Sprite_Object_Gen");
        block.transform.scale.x = sizeX;
        block.transform.scale.y = sizeY;
                // new Transform(new Vector2f(), new Vector2f(sizeX, sizeY)), 0);
        SpriteRenderer renderer = new SpriteRenderer();
        renderer.setSprite(sprite);
        block.addComponent(renderer);
        return block;
    }
}
