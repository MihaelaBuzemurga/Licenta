package Reusable;

public enum ServerInfo {
	SERVER_PORT(9091),
	SERVER_MAX_USERS(100),
	SERVER_SLEEP_TIME(20*1000);
	
	
	private final int value;
	ServerInfo(int value) { this.value = value; }
    public int getValue() { return value; }
   
}
