package Reusable;

public enum ReqeustEnum {
	REQUEST_VALUE(0),
	REQUEST_LOGIN(1),
	REQUEST_REGISTER(2);
	
	private final int value;
	ReqeustEnum(int value) { this.value = value; }
    public int getValue() { return value; }

}
