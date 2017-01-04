package Reusable;

public enum ReqeustEnum {
	REQUEST_VALUE(100),
	REQUEST_LOGIN(101),
	REQUEST_UPLOAD_FILE(101),
	REQUEST_REGISTER(102);
	
	private final int value;
	ReqeustEnum(int value) { this.value = value; }
    public int getValue() { return value; }
    
    public static ReqeustEnum fromId(int id) {
        for (ReqeustEnum type : ReqeustEnum.values()) {
            if (type.getValue() == id) {
                return type;
            }
        }
        return null;
    }

}
