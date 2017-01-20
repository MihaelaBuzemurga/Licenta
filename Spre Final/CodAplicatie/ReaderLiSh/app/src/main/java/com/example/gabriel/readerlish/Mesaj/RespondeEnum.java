package com.example.gabriel.readerlish.Mesaj;

import java.io.Serializable;

/**
 * Created by Gabriel on 18.01.2017.
 */

public enum RespondeEnum  implements Serializable {
    LOGIN_SUCCES(100),
    LOGIN_FAIL(101),
    REQUEST_REGISTER(102);

    private final int value;
    RespondeEnum(int value) { this.value = value; }
    public int getValue() { return value; }

    public static RespondeEnum fromId(int id) {
        for (RespondeEnum type : RespondeEnum.values()) {
            if (type.getValue() == id) {
                return type;
            }
        }
        return null;
    }
}