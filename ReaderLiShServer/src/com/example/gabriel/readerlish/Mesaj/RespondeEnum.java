package com.example.gabriel.readerlish.Mesaj;

import java.io.Serializable;

public enum RespondeEnum implements Serializable{
	LOGIN_SUCCES(100),
	LOGIN_FAIL(101),
	REGISTER_SUCCES(102),
	REGISTER_FAIL(103);
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
