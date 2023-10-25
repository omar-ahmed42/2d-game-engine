package com.omarahmed42.components;

import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_LEFT;

import com.omarahmed42.main.GameObject;
import com.omarahmed42.main.MouseListener;
import com.omarahmed42.main.Window;
import com.omarahmed42.util.Settings;

public class MouseControls extends Component {

    private GameObject holdingObject = null;

    public void pickupObject(GameObject go) {
        this.holdingObject = go;
        Window.getScene().addGameObjectToScene(go);
    }

    public void place() {
        this.holdingObject = null;
    }

    @Override
    public void update(float dt) {
        if (holdingObject != null) {
            holdingObject.transform.position.x = MouseListener.getOrthoX();
            holdingObject.transform.position.y = MouseListener.getOrthoY();
            holdingObject.transform.position.x = ((int) (holdingObject.transform.position.x / Settings.GRID_WIDTH)) * Settings.GRID_WIDTH;
            holdingObject.transform.position.y = ((int) (holdingObject.transform.position.y / Settings.GRID_HEIGHT)) * Settings.GRID_HEIGHT;
            if (MouseListener.mouseButtonDown(GLFW_MOUSE_BUTTON_LEFT)) {
                place();
            }
        }
    }
}
