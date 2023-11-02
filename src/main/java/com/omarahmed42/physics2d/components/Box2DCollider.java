package com.omarahmed42.physics2d.components;

import org.joml.Vector2f;

import com.omarahmed42.components.Component;
import com.omarahmed42.renderer.DebugDraw;

public class Box2DCollider extends Component {

    private Vector2f halfSize = new Vector2f(1);
    private Vector2f origin = new Vector2f();

    protected Vector2f offset = new Vector2f();

    public Vector2f getOffset() {
        return this.offset;
    }

    public Vector2f getHalfSize() {
        return halfSize;
    }

    public void setHalfSize(Vector2f halfSize) {
        this.halfSize = halfSize;
    }

    public Vector2f getOrigin() {
        return origin;
    }

    public void setOrigin(Vector2f origin) {
        this.origin = origin;
    }

    @Override
    public void editorUpdate(float dt) {
        Vector2f center = new Vector2f(this.gameObject.transform.position).add(this.offset);
        DebugDraw.addBox2D(center, this.halfSize, this.gameObject.transform.rotation);
    }

    public void setOffset(Vector2f offset) {
        this.offset = offset;
    }
}
