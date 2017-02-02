package com.example.gabriel.readerlish.Mesaj;

import java.io.Serializable;

public enum RequestEnum implements Serializable{
	REQUEST_VALUE(100),
	REQUEST_LOGIN(101),
	REQUEST_REGISTER(102),
	REQUEST_UPLOAD_FILE(103),
	REQUEST_EDIT_FILE(104),
	REQUEST_FILE(105),
	REQUEST_FILE_CONTENT(106),
	REQUEST_BOOKS(107),
	REQUEST_GRUP_FOR_SUBSCRIBE(108),
	REQUEST_ADD_TO_GRUP(109),
	REQUEST_ADD_NOTA(109),
	REQUEST_EDIT_PROFIL(110),
	REQUEST_EDIT_PHOTO_PROFILE(111),
    REQUEST_GEN(112),
    REQUEST_MY_BOOKS(113),
    REQUEST_EDIT_BOOK(114);
	
	private final int value;
	RequestEnum(int value) { this.value = value; }
    public int getValue() { return value; }
    
    public static RequestEnum fromId(int id) {
        for (RequestEnum type : RequestEnum.values()) {
            if (type.getValue() == id) {
                return type;
            }
        }
        return null;
    }

}
