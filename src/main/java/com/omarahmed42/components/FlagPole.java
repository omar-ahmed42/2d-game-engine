package com.omarahmed42.components;

import org.jbox2d.dynamics.contacts.Contact;
import org.joml.Vector2f;

import com.omarahmed42.main.GameObject;

public class FlagPole extends Component {
    private boolean isTop = false;

    public FlagPole(boolean isTop) {
        this.isTop = isTop;
    }

    @Override
    public void beginCollision(GameObject obj, Contact contact, Vector2f contactNormal) {
        PlayerController playerController = obj.getComponent(PlayerController.class);
        if (playerController != null) {
            playerController.playWinAnimation(this.gameObject);
        }
    }

    
    
}
