package cx2002;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Scanner;

public class AdminOptions 
{
	private static Scanner sc = new Scanner(System.in);
	
	public static boolean editStudentAccessPeriod(ArrayList<School> schoolList)
	{
		System.out.printf("Enter school: ");
        String school = sc.nextLine().toUpperCase();
        int i = Collections.binarySearch(schoolList, new School(school));
        if(i < 0)
        {
        	System.out.println("School does not exist!");
        	return false;
        }
        Date timeStart;
        while(true)
        {
			try 
			{
		        System.out.printf("Enter start access time(HHMM DD/MM/YY): ");
				timeStart = MainApplication.df.parse(sc.nextLine());
				break;
			}
			catch (ParseException e) 
			{
				System.out.println("Wrong format!");
			}
        }
        Date timeEnd;
        while(true)
        {
			try 
			{
		        System.out.printf("Enter end access time(HHMM DD/MM/YY): ");
				timeEnd = MainApplication.df.parse(sc.nextLine());
				break;
			} 
			catch (ParseException e) 
			{
				System.out.println("Wrong format!");
			}
        }
        schoolList.get(i).setTimeStart(timeStart);
        schoolList.get(i).setTimeEnd(timeEnd);
        System.out.printf("Successfully changed %s access period!\n", school);
        return true;
    }
	
	public static boolean addStudent(ArrayList<School> schoolList, ArrayList<Student> studentList, ArrayList<User> userList)
	{
		System.out.printf("Username: ");
		String username = sc.nextLine();
		if(Collections.binarySearch(userList, new User(username)) >= 0)
		{
			System.out.println("Username already exists!");
			return false;
		}
		
		System.out.printf("Password: ");
		String password = sc.nextLine();
        String hashedPassword = new String(MainApplication.hash(password.getBytes()));
		
		System.out.printf("Matric number: ");
		String matric = sc.nextLine().toUpperCase();
		if(Collections.binarySearch(studentList, new Student(matric)) >= 0)
		{
			System.out.println("Student already exists!");
			return false;
		}
		
		System.out.printf("Name: ");
		String name = sc.nextLine().toUpperCase();
		
		System.out.printf("Email: ");
		String email = sc.nextLine();
		
		System.out.printf("Gender: ");
		String gender = sc.nextLine().toUpperCase();
		
		System.out.printf("Nationality: ");
		String nationality = sc.nextLine().toUpperCase();
		
		System.out.printf("School: ");
		String school = sc.nextLine().toUpperCase();
		int i = Collections.binarySearch(schoolList, new School(school));
		if(i < 0)
		{
			System.out.println("School does not exist!");
			return false;
		}
        
		User user = new User(username, hashedPassword, 1, matric);
		Student stud = new Student(matric, name, email, gender, nationality, schoolList.get(i), 1);
		userList.add(user);
		studentList.add(stud);
		Collections.sort(userList);
		Collections.sort(studentList);
		System.out.println("Student successfully added!");
		
		System.out.printf("Displaying all students from %s:\n\n", school);
		System.out.printf("%-20s %-7s %-10s\n", "Name", "Gender", "Nationality");
		for(Student studs: studentList)
			if(studs.getSchool().getName().equals(school))
				studs.displayLimited();
		return true;
	}
	
	public static void addCourse(ArrayList<School> schoolList, ArrayList<Course> courseList, ArrayList<Index> indexList) throws ParseException
	{
		int indexId = 0;
		while(true)
		{
			try
			{
				System.out.printf("Index number(existing index to update): ");
				indexId = Integer.parseInt(sc.nextLine());
				break;
			}
			catch(NumberFormatException e)
			{
				System.out.println("Please enter integer!");
			}
		}
		
		System.out.printf("School: ");
		String schoolName = sc.nextLine().toUpperCase();
		int schoolI = Collections.binarySearch(schoolList, new School(schoolName));
		School school;
		if(schoolI < 0)
		{
			Date date = MainApplication.df.parse("0000 01/01/01");
			school = new School(schoolName, date, date);
			schoolList.add(school);
			Collections.sort(schoolList);
		}
		else
		{
			school = schoolList.get(schoolI);
		}
		
		int indexI = Collections.binarySearch(indexList, new Index(indexId));
		Course course;
		if(indexI < 0)
		{
			System.out.printf("Module code: ");
			String courseId = sc.nextLine().toUpperCase();
			int au;
			while(true)
			{
				try 
				{
					System.out.printf("Number of AU: ");
					au = Integer.parseInt(sc.nextLine());
					break;
				} 
				catch (NumberFormatException e) 
				{
					System.out.println("Please enter an integer!");
				}
			}
			course = new Course(school, courseId, au);
			courseList.add(course);
			Collections.sort(courseList);
		}
		else
		{
			course = indexList.get(indexI).getCourse();
		}
		
		int size;
		while(true)
		{
			try 
			{
				System.out.printf("Size: ");
				size = Integer.parseInt(sc.nextLine());
				break;
			} 
			catch (NumberFormatException e) 
			{
				System.out.println("Please enter an integer!");
			}
		}
		
		int totalLesson;
		while(true)
		{
			try 
			{
				System.out.printf("Number of lessons: ");
				totalLesson = Integer.parseInt(sc.nextLine());
				break;
			} 
			catch (NumberFormatException e) 
			{
				System.out.println("Please enter an integer!");
			}
		}
		System.out.println("");
		ArrayList<Lesson> lessonList = new ArrayList<Lesson>();
		
		for(int i = 0; i < totalLesson; i++)
		{
			System.out.printf("Lesson %d\n", i+1);
			System.out.printf("Type: ");
			String type = sc.nextLine().toUpperCase();
			System.out.printf("Group: ");
			String group = sc.nextLine().toUpperCase();
			System.out.printf("Day: ");
			String day = sc.nextLine().toUpperCase();
			System.out.printf("Time(HHMM-HHMM): ");
			String time = sc.nextLine();
			System.out.printf("Venue: ");
			String venue = sc.nextLine().toUpperCase();
			System.out.printf("Remarks(Enter to skip): ");
			String remarks = sc.nextLine();
			System.out.println("");
			if(remarks.isEmpty())
				remarks = " ";
			Lesson lesson;
			lesson = new Lesson(type, group, day, time, venue, remarks);
			lessonList.add(lesson);
		}
		
		ArrayList<Student> waitingStudents = new ArrayList<Student>();
		if(indexI >= 0)
		{
			waitingStudents = indexList.get(indexI).getWaitingStudents();
			indexList.remove(indexI);
		}
		Index index = new Index(course, indexId, size, waitingStudents);
		index.setLessonList(lessonList);
		indexList.add(index);
		Collections.sort(indexList);
		System.out.println("Lesson added/updated successfully!");
		
		System.out.printf("Displaying all %s index:\n\n", course.getCourseId());
		for(Index indexes: indexList)
			if(indexes.getCourse().getCourseId().equals(course.getCourseId()))
				indexes.display();
	}
	
	public static void checkSlot(ArrayList<Index> indexList)
	{
		while(true)
		{
			int indexId;
			while(true)
			{
				try 
				{
					System.out.printf("Search index number(-1 to exit): ");
					indexId = Integer.parseInt(sc.nextLine());
					break;
				} 
				catch (NumberFormatException e) 
				{
					System.out.println("Please enter an integer!");
				}
			}
			System.out.println("");
			if(indexId == -1)
				break;
			
			int i = Collections.binarySearch(indexList, new Index(indexId));
			if(i >= 0)
			{
				Index index = indexList.get(i);
				index.display();
				if(index.getWaitingStudents().size() > 0)
				{
					System.out.printf("Students in waiting list: \n");
					System.out.printf("%-20s %-7s %-10s\n", "Name", "Gender", "Nationality");
					for(Student stud: index.getWaitingStudents())
						stud.displayLimited();
				}
			}
			else
				System.out.println("Index not found!\n");
		}
	}
	
	public static void printStudentByIndex(ArrayList<Index> indexList) 
	{
		int indexId;
		while(true)
		{
			try 
			{
				System.out.printf("Enter index number: ");
				indexId = Integer.parseInt(sc.nextLine());
				break;
			} 
			catch (NumberFormatException e) 
			{
				System.out.println("Please enter an integer!");
			}
		}
		int i = Collections.binarySearch(indexList, new Index(indexId));
		if(i >= 0)
		{
			System.out.printf("%-20s %-7s %-10s\n", "Name", "Gender", "Nationality");
			for(Student stud: indexList.get(i).getRegisteredStudents())
				stud.displayLimited();
		}
		else
			System.out.println("Index does not exist!");
	}
	
	public static void printStudentByCourse(ArrayList<Course> courseList, ArrayList<Index> indexList) 
	{
		System.out.printf("Enter module number: ");
		String courseId = sc.nextLine().toUpperCase();
		
		int i = Collections.binarySearch(courseList, new Course(courseId));
		if(i >= 0)
		{
			System.out.printf("%-20s %-7s %-10s\n", "Name", "Gender", "Nationality");
			for(Index index: indexList)
				if(index.getCourse().getCourseId().equals(courseId))
					for(Student stud: index.getRegisteredStudents())
						stud.displayLimited();
		}
		else
			System.out.println("Module does not exist!");
	}
}