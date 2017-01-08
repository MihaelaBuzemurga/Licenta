package FileManager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

import Database.ManagerDb;
import Grup.Grup;
import Mesaj.Mesaj;
import TransformerBytes.TransformerBytes;

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
	public Mesaj getFile(Carte carte) {
		System.out.println(carte.getID());
		Mesaj mesaj=new Mesaj();
		carte=ManagerDb.getSession().getCarte(carte);
		carte.setImagine(getFileFromServer(carte.getCale_img()));
		System.out.println(carte.getNume());
		mesaj.setObiect(carte);
		
		return mesaj;

	}
	public Mesaj getContent(Carte carte) {
		Mesaj mesaj=new Mesaj();
		carte=ManagerDb.getSession().getCarte(carte);
		carte.setContinut(getFileFromServer(carte.getCale()));
		System.out.println(carte.getNume());
		mesaj.setObiect(carte);
		
		return mesaj;

	}
	public Mesaj getBooks(int id) {
		Carte []carte=ManagerDb.getSession().getBooks(id);
		Mesaj mesaj=new Mesaj();
		mesaj.setObiect(carte);
		return mesaj;

	}
	
	public Mesaj getGrupForSubscribe()
	{
		int id=1;
		Mesaj mesaj=new Mesaj();
		ArrayList<Grup> grupuri=ManagerDb.getSession().getGrupForSubscribe(id);
		ArrayList<Grup> grupuri2=ManagerDb.getSession().getAllGrup();
		for(int i=0;i<grupuri.size();i++)
		{
			for(int j=0;j<grupuri2.size();j++)
			{
				if(grupuri2.get(j).getId()==grupuri.get(i).getId())
				{
					grupuri2.remove(j);
				}
			}
		}
		mesaj.setObiect(grupuri2);
		return mesaj;
	}
	public Mesaj addToGrup(int id)
	{
		Mesaj mesaj=new Mesaj();
		ArrayList<Grup> grupuri=ManagerDb.getSession().addToGrup(id);
		mesaj.setObiect(grupuri);
		return mesaj;
	}
	
	private byte[] getFileFromServer(String path_file)
	{
		 byte[] data = null;
		 Path path = Paths.get("C:\\Users\\Gabriel\\Desktop\\test_upload\\Fisiere\\"+path_file);
		 try {
			 data = Files.readAllBytes(path);
       } catch (IOException ex) {
          
       }
		 return data;
	}

	public void uploadFile(Object obiect) {
		Carte carte=(Carte)obiect;
		ManagerDb.getSession().uploadCarte(carte);
		System.out.println("Upload carte"+carte.getNume()+" cu id-ul="+String.valueOf(carte.getID()));
		uploadOnServer(carte);
	}
	private void uploadOnServer(Carte carte)
	{
		createDirector(String.valueOf(carte.getID()));
		uploadFileOnFolder(String.valueOf(carte.getID()),carte.getNume()+".pdf",carte.getContinut());
		carte.setCale(String.valueOf(carte.getID())+"\\"+carte.getNume()+".pdf");
		uploadFileOnFolder(String.valueOf(carte.getID()),carte.getNume()+".jpg",carte.getImagine());
		carte.setCale_img(String.valueOf(carte.getID())+"\\"+carte.getNume()+".jpg");
		ManagerDb.getSession().updateCarteCale(carte);
		
	}
	
	public Mesaj updateCarteFromUser(Carte carte)
	{
		Mesaj mesaj=new Mesaj();
		if(carte.getContinut()!=null)
		{
			uploadFileOnFolder(String.valueOf(carte.getID()),carte.getNume()+".pdf",carte.getContinut());
		}
		if(carte.getImagine()!=null)
		{
			uploadFileOnFolder(String.valueOf(carte.getID()),carte.getNume()+".pdf",carte.getImagine());
		}
		ManagerDb.getSession().updateCarte(carte);
		carte.setContinut(null);
		carte.setImagine(null);
		mesaj.setObiect(carte);
		return mesaj;
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
			if(!file.exists())
			{
				file.createNewFile();
			}
			FileOutputStream fisier = new FileOutputStream(file.getPath());
			fisier.write(continut);
			fisier.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}


}
