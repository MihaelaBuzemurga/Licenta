package com.example.gabriel.readerlish.Reusable;

import java.io.Serializable;

/**
 * Created by Gabriel on 01.02.2017.
 */

public enum Views implements Serializable {
    EDIT_BOOKS(100);

    private final int value;
    Views(int value) { this.value = value; }
    public int getValue() { return value; }

    public static Views fromId(int id) {
        for (Views type : Views.values()) {
            if (type.getValue() == id) {
                return type;
            }
        }
        return null;
    }

}
