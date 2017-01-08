package Mesaj;

import java.io.Serializable;

public enum RequestEnum implements Serializable{
	REQUEST_VALUE(100),
	REQUEST_LOGIN(101),
	REQUEST_REGISTER(102),
	REQUEST_UPLOAD_FILE(103),
	REQUEST_EDIT_FILE(104),
	REQUEST_FILE(105);
	
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
