package cx2002;

import java.io.Console;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Properties;
import java.util.Scanner;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class MainApplication 
{
	public static final String USERNAMEFILE = "username.dat";
	public static final String EMAIL = "testingjava1999@gmail.com";
	public static final String PASSWORD = "testingjava1999Email";
	public static final SimpleDateFormat df = new SimpleDateFormat("HHmm dd/MM/yy");
	public static final int AULIMIT = 21;
	
	public static void main(String[] args) throws MessagingException, ParseException
	{
		Scanner sc = new Scanner(System.in);
		Console console = System.console();
		boolean logged = false;
		ArrayList<User> userList = FileRW.readUser();
		ArrayList<School> schoolList = FileHandler.getSchool();
		ArrayList<Course> courseList = FileHandler.getCourse(schoolList);
		ArrayList<Index> indexList = FileHandler.getIndexFull(courseList);
		ArrayList<Student> studentList = FileHandler.getStudentFull(schoolList, indexList);
		int userI = -1;
		
		while(!logged)
		{
	        System.out.print("\nUsername: ");
	        String username = sc.nextLine();
	        System.out.print("Password: ");
	        char[] passwordChar = console.readPassword();
	        String password = new String(passwordChar);
	        String hashedPassword = new String(hash(password.getBytes()));
	        
	        userI = Collections.binarySearch(userList, new User(username));
			if(userI < 0)
				System.out.println("No such user!");
			else if(hashedPassword.equals(userList.get(userI).getPassword()))
				logged = true;
			else
				System.out.println("Wrong password!");
		}
		
		switch(userList.get(userI).getDomain())
		{
		case 1:
			int studentI = Collections.binarySearch(studentList, new Student(userList.get(userI).getMatric()));
			Student stud = studentList.get(studentI);
			logged = timeCompare(stud);
			while(logged)
			{
		        System.out.println("");
		        stud.displayDetailed();
				menuStudent();
				
				int option;
				while(true)
				{
					try
					{
						System.out.printf("Option: ");
						option = Integer.parseInt(sc.nextLine());
						break;
					}
					catch(NumberFormatException e)
					{
						System.out.println("Please enter integer!");
					}
				}
				
				System.out.println("");
				switch(option)
				{
				case 1:
					if(StudentOptions.addModule(indexList, stud))
					{
						FileHandler.updateRegistered(studentList);
						FileHandler.updateIndex(indexList);
					}
					break;
					
				case 2:
					if(StudentOptions.dropModule(stud))
					{
						FileHandler.updateRegistered(studentList);
						FileHandler.updateIndex(indexList);
					}
					break;
					
				case 3:
					StudentOptions.printCourses(indexList, stud);
					break;
					
				case 4:
					StudentOptions.checkVacancy(indexList);
					break;
					
				case 5:
					if(StudentOptions.swapIndex(indexList, stud))
						FileHandler.updateRegistered(studentList);
					break;
					
				case 6:
					System.out.print("Student 2 Username: ");
			        String username = sc.nextLine();
			        System.out.print("Student 2 Password: ");
			        char[] passwordChar = console.readPassword();
			        String password = new String(passwordChar);
			        String hashedPassword = new String(hash(password.getBytes()));
			        System.out.println("");
			        
			        int user2I = Collections.binarySearch(userList, new User(username));
					if(user2I < 0)
						System.out.println("No such user!");
					else if(hashedPassword.equals(userList.get(user2I).getPassword()))
					{
						int student2I = Collections.binarySearch(studentList, new Student(userList.get(user2I).getMatric()));
						Student stud2 = studentList.get(student2I);
						if(StudentOptions.swapStudent(indexList, stud, stud2))
							FileHandler.updateRegistered(studentList);
					}
					else
						System.out.println("Wrong password!");
					break;
					
				case 7:
					System.out.println("Logging out...");
					logged = false;
					break;
					
				default:
					System.out.println("No such option!");
				}
				update(indexList, studentList);
			}
			break;
		
		case 2:
			while(logged)
			{
				System.out.println("");
				System.out.println("Admin Mode");
				menuAdmin();
				
				int option;
				while(true)
				{
					try
					{
						System.out.printf("Option: ");
						option = Integer.parseInt(sc.nextLine());
						break;
					}
					catch(NumberFormatException e)
					{
						System.out.println("Please enter integer!");
					}
				}
				
				System.out.println("");
				switch(option)
				{
				case 1:
					if(AdminOptions.editStudentAccessPeriod(schoolList))
						FileHandler.updateSchool(schoolList);
					break;
					
				case 2:
					if(AdminOptions.addStudent(schoolList, studentList, userList));
					{
						FileRW.updateUser(userList);
						FileHandler.updateStudent(studentList);
						FileHandler.updateRegistered(studentList);
					}
					break;
					
				case 3:
					AdminOptions.addCourse(schoolList, courseList, indexList);
					FileHandler.updateSchool(schoolList);
					FileHandler.updateCourse(courseList);
					FileHandler.updateIndex(indexList);
					FileHandler.updateLesson(indexList);
					break;
					
				case 4:
					AdminOptions.checkSlot(indexList);
					break;
					
				case 5:
					AdminOptions.printStudentByIndex(indexList);
					break;
					
				case 6:
					AdminOptions.printStudentByCourse(courseList, indexList);
					break;
					
				case 7:
					System.out.println("Logging out...");
					logged = false;
					break;
				
				default:
					System.out.println("No such option!");
				}
				update(indexList, studentList);
			}
			break;
		}
		sc.close();
	}
	
	public static void menuStudent()
	{
		System.out.println("");
		System.out.println("1.Add course");
		System.out.println("2.Drop course");
		System.out.println("3.Check/Print courses registered");
		System.out.println("4.Check vacancies available");
		System.out.println("5.Change index number of course");
		System.out.println("6.Swop index number with another student");
		System.out.println("7.Logout");
		System.out.println("");
	}
	
	public static void menuAdmin()
	{
		System.out.println("");
		System.out.println("1.Edit student access period");
		System.out.println("2.Add a student");
		System.out.println("3.Add/Update a Course");
		System.out.println("4.Check available slot for an index number");
		System.out.println("5.Print student list by index number");
		System.out.println("6.Print student list by course");
		System.out.println("7.Logout");
		System.out.println("");
	}

	public static boolean timeCompare(Student stud)
	{
		Calendar cal = Calendar.getInstance();
	    Date current_time = cal.getTime();
	    if(current_time.after(stud.getSchool().getTimeStart()) && current_time.before(stud.getSchool().getTimeEnd()))
	    	return true;

		System.out.printf("Currently inaccessable! Your access time is %s to %s.\n", df.format(stud.getSchool().getTimeStart()), df.format(stud.getSchool().getTimeEnd()));
	    return false;
	}
	
	public static void update(ArrayList<Index> indexList, ArrayList<Student> studentList) throws MessagingException
	{
		for(Index i: indexList)
			if(i.getSize() - i.getRegisteredStudents().size() > 0 && !i.getWaitingStudents().isEmpty())
			{
				int size;
				if(i.getSize() - i.getRegisteredStudents().size() < i.getWaitingStudents().size())
					size = i.getSize() - i.getRegisteredStudents().size();
				else
					size = i.getWaitingStudents().size();
				for(int j = 0; j < size; j++)
				{
					Student stud = i.getWaitingStudents().get(0);
					i.getRegisteredStudents().add(stud);
					i.getWaitingStudents().remove(stud);
					stud.getRegisteredIndexes().add(i);
					stud.getWaitingIndexes().remove(i);
					Collections.sort(i.getRegisteredStudents());
					Collections.sort(stud.getRegisteredIndexes());
					Collections.sort(stud.getWaitingIndexes());
					FileHandler.updateIndex(indexList);
					FileHandler.updateRegistered(studentList);
					sendNotification(stud, i.getIndexId());
				}
			}
	}
	
	public static void sendNotification(Student stud, int i) throws MessagingException
	{
		sendMail(stud, i);
	}

	public static void sendMail(Student stud, int index) throws MessagingException
	{
		Properties props = new Properties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.ssl.trust", "smtp.gmail.com");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.port", "587");

		Session session = Session.getInstance(props, new javax.mail.Authenticator() 
		{
			protected PasswordAuthentication getPasswordAuthentication() 
			{
				return new PasswordAuthentication(EMAIL, PASSWORD);
			}
		});
		Message message = prepareMessage(session, EMAIL, stud, index);
		Transport.send(message);
	}
	
	private static Message prepareMessage(Session session, String myAccount, Student stud, int index) 
	{
		try 
		{
			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(myAccount));
			message.setRecipient(Message.RecipientType.TO, 
					new InternetAddress(stud.getEmail())); 
			message.setSubject(index + " registered");
			message.setText("Dear " + stud.getName() + "\nYou have been successfully registered for " + index + "!");
			return message;
		}
		catch (MessagingException e) 
		{
			throw new RuntimeException(e);
		}	
	}
	
	public static byte[] hash(byte[] data) 
	{
		byte[] enc = new byte[data.length];
		for (int i=0; i<data.length;i++)
			enc[i] = (byte)((i%2 ==0) ? data[i] + 15 : data[i] - 10);
		return enc;
	}
}
