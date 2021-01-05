package cx2002;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

public class FileRW 
{
	private static final String USERNAMEFILE = "username.dat";
	public static ArrayList<String> read(String fileName)
	{
		ArrayList<String> data = new ArrayList<String>();
		try
		{
			Scanner sc = new Scanner(new FileInputStream(fileName));
			while (sc.hasNextLine())
				data.add(sc.nextLine());
			sc.close();
		}
		catch (IOException ex)
		{
			ex.printStackTrace();
		}
		return data;
	}
	
	public static void write(String fileName, ArrayList<String> data)
	{
	    try 
	    {
    	    PrintWriter out = new PrintWriter(new FileWriter(fileName, false));
    		for (int i =0; i < data.size() ; i++) 
    			out.println((String)data.get(i));
    		out.close();
	    }
	    catch (IOException ex)
		{
			ex.printStackTrace();
		}
	}
	
	public static ArrayList<User> readUser()
	{
		ArrayList<User> data = new ArrayList<User>();
		try
		{
			FileInputStream fis = new FileInputStream(USERNAMEFILE);
			ObjectInputStream ois = new ObjectInputStream(fis);
			data = (ArrayList<User>) ois.readObject();
			ois.close();
			fis.close();
		}
		catch (IOException | ClassNotFoundException e)
		{
			e.printStackTrace();
		}
		return data;
	}
	
	public static void updateUser(ArrayList<User> data)
	{
	    try 
	    {
    		FileOutputStream fos = new FileOutputStream(USERNAMEFILE);
			ObjectOutputStream out = new ObjectOutputStream(fos);
			out.writeObject(data);
			out.close();
	    }
	    catch (IOException ex)
		{
			ex.printStackTrace();
		}
	}
}