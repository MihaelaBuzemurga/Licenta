package FileManager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import Database.ManagerDb;
import Message.Message;

public class FileManager {

	private FileManager() {
	};

	private static FileManager instance_manager = null;

	public static FileManager getInstance() {
		if (instance_manager == null) {
			instance_manager = new FileManager();
		}
		return instance_manager;
	}
	public Message getFile(Message message) {
		Message newMessage = new Message();
		String id=message.getNextParameter();
		Carte carte=ManagerDb.getSession().getCarte(Integer.parseInt(id));
		newMessage.addMessage(carte);
		return newMessage;

	}
	public Message uploadFile(Message message) {

		Message newMessage = new Message();
		String nume = message.getNextParameter();
		String autor = message.getNextParameter();
		String descriere = message.getNextParameter();
		int id_gen = Integer.parseInt(message.getNextParameter());
		int nr_buf_img=Integer.parseInt(message.getNextParameter());
		byte[]  imagine = message.getNextParameterBytes(nr_buf_img);
		 System.out.println("lenght1="+String.valueOf(imagine.length));
		int nr_buf_continut=Integer.parseInt(message.getNextParameter());
		byte[] continut = message.getNextParameterBytes(nr_buf_continut);
		 System.out.println("lenght2="+String.valueOf(continut.length));
		 
		 
		Carte carte = new Carte(nume, autor, descriere, id_gen, continut,imagine);
		ManagerDb.getSession().uploadCarte(carte);
		
		System.out.println("Upload carte"+carte.getNume()+" cu id-ul="+String.valueOf(carte.getID()));
		uploadOnServer(carte);

		return newMessage;

	}
	private void uploadOnServer(Carte carte)
	{
		createDirector(String.valueOf(carte.getID()));
		uploadFileOnFolder(String.valueOf(carte.getID()),carte.getNume()+".pdf",carte.getContinut());
		carte.setCale(String.valueOf(carte.getID())+"\\"+carte.getNume()+".pdf");
		uploadFileOnFolder(String.valueOf(carte.getID()),carte.getNume()+".jpg",carte.getImagine());
		carte.setCale_img(String.valueOf(carte.getID())+"\\"+carte.getNume()+".img");
		ManagerDb.getSession().updateCarteCale(carte);
		
	}
	private void createDirector(String nume)
	{
		File file = new File("C:\\Users\\Gabriel\\Desktop\\test_upload\\Fisiere\\"+nume);
        if (!file.exists()) {
            if (file.mkdir()) {
                System.out.println("Directory is created!");
            } else {
                System.out.println("Failed to create directory!");
            }
        }
	}
	private void uploadFileOnFolder(String director,String nume,byte[] continut) {
		File file = new File("C:\\Users\\Gabriel\\Desktop\\test_upload\\Fisiere\\"+director+"\\"+nume);
		try {
			file.createNewFile();
			FileOutputStream fisier = new FileOutputStream(file.getPath());
			fisier.write(continut);
			fisier.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}


}
