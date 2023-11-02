package com.omarahmed42.physics2d.components;

import org.joml.Vector2f;

import com.omarahmed42.components.Component;
import com.omarahmed42.renderer.DebugDraw;

public class CircleCollider extends Component {

    protected Vector2f offset = new Vector2f();

    private float radius = 1f;

    public float getRadius() {
        return radius;
    }

    public void setRadius(float radius) {
        this.radius = radius;
    }

    public Vector2f getOffset() {
        return this.offset;
    }

    public void setOffset(Vector2f offset) {
        this.offset = offset;
    }

    @Override
    public void editorUpdate(float dt) {
        Vector2f center = new Vector2f(this.gameObject.transform.position).add(this.offset);
        DebugDraw.addCircle(center, this.radius);
    }

}
