package com.omarahmed42.observers;

import com.omarahmed42.main.GameObject;
import com.omarahmed42.observers.events.Event;

public interface Observer {
    void onNotify(GameObject object, Event event);
}