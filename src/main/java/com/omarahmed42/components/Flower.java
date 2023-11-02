package com.omarahmed42.components;

import org.jbox2d.dynamics.contacts.Contact;
import org.joml.Vector2f;

import com.omarahmed42.main.GameObject;
import com.omarahmed42.physics2d.components.RigidBody2D;
import com.omarahmed42.util.AssetPool;

public class Flower extends Component {
    private transient RigidBody2D rb;

    @Override
    public void start() {
        this.rb = gameObject.getComponent(RigidBody2D.class);
        AssetPool.getSound("assets/sounds/powerup_appears.ogg").play();
        this.rb.setIsSensor();
    }

    @Override
    public void beginCollision(GameObject collidingObject, Contact contact, Vector2f contactNormal) {
        PlayerController playerController = collidingObject.getComponent(PlayerController.class);
        if (playerController != null) {
            playerController.powerUp();
            this.gameObject.destroy();
        }
    }

    
    
}
