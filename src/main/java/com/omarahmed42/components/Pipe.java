package com.omarahmed42.components;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_A;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_D;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_DOWN;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_LEFT;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_RIGHT;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_S;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_UP;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_W;

import org.jbox2d.dynamics.contacts.Contact;
import org.joml.Vector2f;

import com.omarahmed42.main.Direction;
import com.omarahmed42.main.GameObject;
import com.omarahmed42.main.KeyListener;
import com.omarahmed42.main.Window;
import com.omarahmed42.util.AssetPool;

public class Pipe extends Component {

    private Direction direction;
    private String connectingPipeName = "";
    private boolean isEntrance = false;
    private transient GameObject connectingPipe = null;
    private transient float entranceVectorTolerance = 0.6f;
    private transient PlayerController collidingPlayer = null;

    public Pipe(Direction direction) {
        this.direction = direction;
    }

    @Override
    public void start() {
        connectingPipe = Window.getScene().getGameObject(connectingPipeName);
    }

    @Override
    public void update(float dt) {
        if (connectingPipe == null) {
            return;
        }

        if (collidingPlayer != null) {
            boolean playerEntering = false;
            switch (direction) {
                case Up:
                    if ((KeyListener.isKeyPressed(GLFW_KEY_DOWN) || KeyListener.isKeyPressed(GLFW_KEY_S))
                            && isEntrance) {
                        playerEntering = true;
                    }
                    break;
                case Left:
                    if ((KeyListener.isKeyPressed(GLFW_KEY_RIGHT) || KeyListener.isKeyPressed(GLFW_KEY_D))
                            && isEntrance) {
                        playerEntering = true;

                    }

                    break;
                case Right:
                    if ((KeyListener.isKeyPressed(GLFW_KEY_LEFT) || KeyListener.isKeyPressed(GLFW_KEY_A))
                            && isEntrance) {
                        playerEntering = true;

                    }
                    break;
                case Down:
                    if ((KeyListener.isKeyPressed(GLFW_KEY_UP) || KeyListener.isKeyPressed(GLFW_KEY_W))
                            && isEntrance) {
                        playerEntering = true;

                    }
                    break;
                default:
                    break;
            }

            if (playerEntering) {
                collidingPlayer.setPosition(
                        getPlayerPosition(connectingPipe));
                AssetPool.getSound("assets/sounds/pipe.ogg").play();
            }
        }
    }

    private Vector2f getPlayerPosition(GameObject pipe) {
        Pipe pipeComponent = pipe.getComponent(Pipe.class);
        return switch (pipeComponent.direction) {
            case Up -> new Vector2f(pipe.transform.position).add(0.0f, 0.5f);
            case Left -> new Vector2f(pipe.transform.position).add(-0.5f, 0.0f);
            case Right -> new Vector2f(pipe.transform.position).add(0.5f, 0.0f);
            case Down -> new Vector2f(pipe.transform.position).add(0.0f, -0.5f);
            default -> new Vector2f();
        };
    }

    @Override
    public void beginCollision(GameObject collidingObject, Contact contact, Vector2f contactNormal) {
        PlayerController playerController = collidingObject.getComponent(PlayerController.class);
        if (playerController != null) {
            switch (direction) {
                case Up:
                    if (contactNormal.y < entranceVectorTolerance) {
                        return;
                    }
                    break;
                case Right:
                    if (contactNormal.x < entranceVectorTolerance) {
                        return;
                    }
                    break;
                case Down:
                    if (contactNormal.y > -entranceVectorTolerance) {
                        return;
                    }
                    break;
                case Left:
                    if (contactNormal.x > -entranceVectorTolerance) {
                        return;
                    }
                    break;
            }

            collidingPlayer = playerController;
        }
    }

    @Override
    public void endCollision(GameObject collidingObject, Contact contact, Vector2f contactNormal) {
        PlayerController playerController = collidingObject.getComponent(PlayerController.class);
        if (playerController != null) {
            collidingPlayer = null;
        }
    }
}