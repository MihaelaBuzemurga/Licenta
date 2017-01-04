package FileManager;

import Message.Message;

public class FileManager {
	
	private FileManager(){};
	
	private static FileManager instance_manager=null;
	
	public FileManager getInstance()
	{
		if(instance_manager==null)
		{
			instance_manager=new FileManager();
		}
		return instance_manager;
	}
	
	public Message uploadFile(Message message)
	{
		Message newMessage=new Message();
		String nume=message.getNextParameter();
		String autor=message.getNextParameter();
		String cale=message.getNextParameter();
		
		
		
		return newMessage;
		
	}

}
