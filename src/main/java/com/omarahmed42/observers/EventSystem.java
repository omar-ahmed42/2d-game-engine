package com.omarahmed42.observers;

import java.util.ArrayList;
import java.util.List;

import com.omarahmed42.main.GameObject;
import com.omarahmed42.observers.events.Event;

public class EventSystem {

    private static List<Observer> observers = new ArrayList<>();

    public static void addObserver(Observer observer) {
        observers.add(observer);
    }

    public static void notify(GameObject obj, Event event) {
        for (Observer observer : observers) {
            observer.onNotify(obj, event);
        }
    }
}
