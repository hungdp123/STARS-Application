package cx2002;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;
import java.util.StringTokenizer;

public class AccountCreator 
{
	private static String TEXTFILE = "username.txt";
	private static final String SEPARATOR = "|";
	
	public static void main(String[] args) throws ClassNotFoundException, IOException 
	{
		Scanner sc = new Scanner(System.in);
		ArrayList<User> userList = getUser(TEXTFILE);
		
		System.out.println("1. Add new account");
		System.out.println("2. Update current accounts");
		System.out.printf("Option: ");
		int option = Integer.parseInt(sc.nextLine());
		
		switch(option)
		{
		case 1:
			System.out.printf("Enter username: ");
			String username = sc.nextLine();
			System.out.printf("Enter password: ");
			String password = sc.nextLine();
			String hashedPassword = new String(MainApplication.hash(password.getBytes()));
			System.out.printf("Enter domain(Student:1 Admin:2): ");
			int domain = Integer.parseInt(sc.nextLine());
			System.out.printf("Enter matric number: ");
			String matric = sc.nextLine();
			User user = new User(username, hashedPassword, domain, matric);
			userList.add(user);
			Collections.sort(userList);
			FileRW.updateUser(userList);
			updateUser(TEXTFILE, userList);
			break;
			
		case 2:
			for(int i = 0; i < userList.size(); i++)
			{
				String newPassword = new String(MainApplication.hash(userList.get(i).getPassword().getBytes()));
				userList.get(i).setPassword(newPassword);
			}
			FileRW.updateUser(userList);
			break;
			
		default:
			System.out.println("No such option!");
		}
		sc.close();
	}
	
	public static ArrayList<User> getUser(String fileName) throws IOException, ClassNotFoundException
	{
		ArrayList<User> userList = new ArrayList<User>();
		ArrayList<String> data = FileRW.read(fileName);
		
        for (int i = 0 ; i < data.size() ; i++) 
        {
			String fileLine = (String) data.get(i);
			StringTokenizer part = new StringTokenizer(fileLine, SEPARATOR);

			String username = part.nextToken().trim();
			String password = part.nextToken().trim();
			int domain = Integer.parseInt(part.nextToken().trim());
			String  matric = part.nextToken().trim();
			
			User users = new User(username, password, domain, matric);
			userList.add(users);
        }
		return userList;
	}
	
	public static void updateUser(String fileName, ArrayList<User> userList) throws IOException
	{
		ArrayList<String> data = new ArrayList<String>();
		
		for(int i = 0; i < userList.size(); i++)
		{
			StringBuilder userString =  new StringBuilder();
			userString.append(userList.get(i).getUsername());
			userString.append(SEPARATOR);
			userString.append(userList.get(i).getPassword());
			userString.append(SEPARATOR);
			userString.append(userList.get(i).getDomain());
			userString.append(SEPARATOR);
			userString.append(userList.get(i).getMatric());
			
			data.add(userString.toString());
		}
		FileRW.write(fileName, data);
	}
}