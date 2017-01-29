package Reusable;

public enum ServerInfo {
	SERVER_PORT(9090),
	SERVER_MAX_USERS(5),
	SERVER_SLEEP_TIME(20*1000);
	
	
	private final int value;
	ServerInfo(int value) { this.value = value; }
    public int getValue() { return value; }
   
}
