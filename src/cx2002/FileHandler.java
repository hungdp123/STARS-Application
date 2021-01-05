package cx2002;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.StringTokenizer;

public class FileHandler 
{
	private static final String SEPARATOR = "|";
	private static final String SCHOOLFILE = "school.txt";
	private static final String COURSEFILE = "course.txt";
	private static final String INDEXFILE = "index.txt";
	private static final String LESSONFILE = "lesson.txt";
	private static final String STUDENTFILE = "student.txt";
	private static final String REGISTEREDFILE = "registered.txt";
	
	public static ArrayList<School> getSchool()
	{
		ArrayList<String> data = FileRW.read(SCHOOLFILE);
		ArrayList<School> schoolList = new ArrayList<School>();
		
		for (int i = 0 ; i < data.size() ; i++) 
        {
			String fileLine = (String) data.get(i);
			StringTokenizer part = new StringTokenizer(fileLine, SEPARATOR);
			
			String name = part.nextToken().trim();
			try 
			{
				Date startTime = MainApplication.df.parse(part.nextToken().trim());
				Date endTime = MainApplication.df.parse(part.nextToken().trim());
				School school = new School(name, startTime, endTime);
				schoolList.add(school);
			} 
			catch (ParseException e) 
			{
				e.printStackTrace();
			}
        }
		Collections.sort(schoolList);
		return schoolList;
	}
	
	public static boolean updateSchool(ArrayList<School> schoolList)
	{
		ArrayList<String> data = new ArrayList<String>();
		for(int i = 0; i < schoolList.size(); i++)
		{
			StringBuilder sb =  new StringBuilder();
			sb.append(schoolList.get(i).getName());
			sb.append(SEPARATOR);
			sb.append(MainApplication.df.format(schoolList.get(i).getTimeStart()));
			sb.append(SEPARATOR);
			sb.append(MainApplication.df.format(schoolList.get(i).getTimeEnd()));
			
			data.add(sb.toString());
		}
		FileRW.write(SCHOOLFILE, data);
		return true;
	}
	
	public static ArrayList<Course> getCourse(ArrayList<School> schoolList)
	{
		ArrayList<String> data = FileRW.read(COURSEFILE);
		ArrayList<Course> courseList = new ArrayList<Course>();
		
		for (int i = 0 ; i < data.size() ; i++) 
        {
			String fileLine = (String) data.get(i);
			StringTokenizer part = new StringTokenizer(fileLine, SEPARATOR);
			
			String school = part.nextToken().trim();
			String courseId = part.nextToken().trim();
			int au = Integer.parseInt(part.nextToken().trim());
			
			int j = Collections.binarySearch(schoolList, new School(school));
			Course course = new Course(schoolList.get(j), courseId, au);
			courseList.add(course);
        }
		Collections.sort(courseList);
		return courseList;
	}
	
	public static boolean updateCourse(ArrayList<Course> courseList)
	{
		ArrayList<String> data = new ArrayList<String>();
		for(int i = 0; i < courseList.size(); i++)
		{
			StringBuilder sb =  new StringBuilder();
			sb.append(courseList.get(i).getSchool().getName());
			sb.append(SEPARATOR);
			sb.append(courseList.get(i).getCourseId());
			sb.append(SEPARATOR);
			sb.append(courseList.get(i).getAU());
			
			data.add(sb.toString());
		}
		FileRW.write(COURSEFILE, data);
		return true;
	}
	
	public static ArrayList<Index> getIndex(ArrayList<Course> courseList)
	{
		ArrayList<String> data = FileRW.read(INDEXFILE);
		ArrayList<Index> indexList = new ArrayList<Index>();
		
		for (int i = 0 ; i < data.size() ; i++) 
        {
			String fileLine = (String) data.get(i);
			StringTokenizer part = new StringTokenizer(fileLine, SEPARATOR);
			
			String course = part.nextToken().trim();
			int indexId = Integer.parseInt(part.nextToken().trim());
			int size = Integer.parseInt(part.nextToken().trim());
			
			ArrayList<Student> waitList = new ArrayList<Student>();
			while(part.hasMoreTokens())
				waitList.add(new Student(part.nextToken().trim()));
			
			int j = Collections.binarySearch(courseList, new Course(course));
			Index index = new Index(courseList.get(j), indexId, size, waitList);
			indexList.add(index);
        }
		Collections.sort(indexList);
		return indexList;			
	}
	
	public static boolean updateIndex(ArrayList<Index> indexList)
	{
		ArrayList<String> data = new ArrayList<String>();
		for(int i = 0; i < indexList.size(); i++)
		{
			StringBuilder sb =  new StringBuilder();
			sb.append(indexList.get(i).getCourse().getCourseId());
			sb.append(SEPARATOR);
			sb.append(indexList.get(i).getIndexId());
			sb.append(SEPARATOR);
			sb.append(indexList.get(i).getSize());
			sb.append(SEPARATOR);
			for(int j = 0; j < indexList.get(i).getWaitingStudents().size(); j++)
			{
				sb.append(indexList.get(i).getWaitingStudents().get(j).getMatric());
				sb.append(SEPARATOR);
			}
			data.add(sb.toString());
		}
		FileRW.write(INDEXFILE, data);
		return true;
	}
	
	public static ArrayList<Index> getIndexFull(ArrayList<Course> courseList)
	{
		ArrayList<String> data = FileRW.read(LESSONFILE);
		ArrayList<Index> indexList = getIndex(courseList);
		
		for (int i = 0 ; i < data.size() ; i++) 
        {
			ArrayList<Lesson> lessonList = new ArrayList<Lesson>();
			String fileLine = (String) data.get(i);
			StringTokenizer part = new StringTokenizer(fileLine, SEPARATOR);
			int indexId = Integer.parseInt(part.nextToken().trim());
			while(part.hasMoreTokens())
			{
				String type = part.nextToken().trim();
				String group = part.nextToken().trim();
				String day = part.nextToken().trim();
				String time = part.nextToken().trim();
				String venue = part.nextToken().trim();
				String remarks = part.nextToken();
				
				Lesson lesson;
				try 
				{
					lesson = new Lesson(type, group, day, time, venue, remarks);
					lessonList.add(lesson);
				} 
				catch (ParseException e) 
				{
					e.printStackTrace();
				}
			}
			int j = Collections.binarySearch(indexList, new Index(indexId));
			indexList.get(j).setLessonList(lessonList);
        }
		Collections.sort(indexList);
		return indexList;
	}
	
	public static boolean updateLesson(ArrayList<Index> indexList)
	{
		ArrayList<String> data = new ArrayList<String>();
		for(int i = 0; i < indexList.size(); i++)
		{
			StringBuilder sb =  new StringBuilder();
			sb.append(indexList.get(i).getIndexId());
			for(int j = 0; j < indexList.get(i).getLessonList().size(); j++)
			{
				sb.append(SEPARATOR);
				sb.append(indexList.get(i).getLessonList().get(j).getType());
				sb.append(SEPARATOR);
				sb.append(indexList.get(i).getLessonList().get(j).getGroup());
				sb.append(SEPARATOR);
				sb.append(indexList.get(i).getLessonList().get(j).getDay());
				sb.append(SEPARATOR);
				sb.append(indexList.get(i).getLessonList().get(j).getTime());
				sb.append(SEPARATOR);
				sb.append(indexList.get(i).getLessonList().get(j).getVenue());
				sb.append(SEPARATOR);
				sb.append(indexList.get(i).getLessonList().get(j).getRemarks());
			}
			data.add(sb.toString());
		}
		FileRW.write(LESSONFILE, data);
		return true;
	}
	
	public static ArrayList<Student> getStudent(ArrayList<School> schoolList)
	{
		ArrayList<String> data = FileRW.read(STUDENTFILE);
		ArrayList<Student> studentList = new ArrayList<Student>();
		
		for (int i = 0 ; i < data.size() ; i++) 
        {
			String fileLine = (String) data.get(i);
			StringTokenizer part = new StringTokenizer(fileLine, SEPARATOR);
			
			String matric = part.nextToken().trim();
			String name = part.nextToken().trim();
			String email = part.nextToken().trim();
			String gender = part.nextToken().trim();
			String nationality = part.nextToken().trim();
			String school = part.nextToken().trim();
			int year = Integer.parseInt(part.nextToken().trim());
			
			int j = Collections.binarySearch(schoolList, new School(school));
			Student student = new Student(matric, name, email, gender, nationality, schoolList.get(j), year);
			studentList.add(student);
        }
		Collections.sort(studentList);
		return studentList;
	}
	
	public static boolean updateStudent(ArrayList<Student> studentList)
	{
		ArrayList<String> data = new ArrayList<String>();
		for(int i = 0; i < studentList.size(); i++)
		{
			StringBuilder sb =  new StringBuilder();
			sb.append(studentList.get(i).getMatric());
			sb.append(SEPARATOR);
			sb.append(studentList.get(i).getName());
			sb.append(SEPARATOR);
			sb.append(studentList.get(i).getEmail());
			sb.append(SEPARATOR);
			sb.append(studentList.get(i).getGender());
			sb.append(SEPARATOR);
			sb.append(studentList.get(i).getNationality());
			sb.append(SEPARATOR);
			sb.append(studentList.get(i).getSchool().getName());
			sb.append(SEPARATOR);
			sb.append(studentList.get(i).getYear());
			
			data.add(sb.toString());
		}
		FileRW.write(STUDENTFILE, data);
		return true;
	}
	
	public static ArrayList<Student> getStudentFull(ArrayList<School> schoolList, ArrayList<Index> indexList)
	{
		ArrayList<String> data = FileRW.read(REGISTEREDFILE);
		ArrayList<Student> studentList = getStudent(schoolList);
		
		for (int i = 0 ; i < data.size() ; i++) 
        {
			ArrayList<Index> registeredList = new ArrayList<Index>();
			ArrayList<Index> waitingList = new ArrayList<Index>();
			String fileLine = (String) data.get(i);
			StringTokenizer part = new StringTokenizer(fileLine, SEPARATOR);
			
			String matric = part.nextToken().trim();
			int j = Collections.binarySearch(studentList, new Student(matric));
			Student stud = studentList.get(j);
			while(part.hasMoreTokens())
			{
				String indexId = part.nextToken().trim();
				if(!indexId.equals(","))
				{
					int k = Collections.binarySearch(indexList, new Index(Integer.parseInt(indexId)));
					registeredList.add(indexList.get(k));
					indexList.get(k).getRegisteredStudents().add(stud);
				}
				else
					break;
			}
			while(part.hasMoreTokens())
			{
				int indexId = Integer.parseInt(part.nextToken().trim());
				int k = Collections.binarySearch(indexList, new Index(indexId));
				waitingList.add(indexList.get(k));
				int l = Collections.binarySearch(indexList.get(k).getWaitingStudents(), stud);
				indexList.get(k).getWaitingStudents().set(l, stud);
			}
			Collections.sort(registeredList);
			Collections.sort(waitingList);
			stud.setRegisteredList(registeredList);
			stud.setWaitingList(waitingList);
        }
		return studentList;
	}
	
	public static boolean updateRegistered(ArrayList<Student> studentList)
	{
		ArrayList<String> data = new ArrayList<String>();
		for(int i = 0; i < studentList.size(); i++)
		{
			StringBuilder sb =  new StringBuilder();
			sb.append(studentList.get(i).getMatric());
			sb.append(SEPARATOR);
			for(int j = 0; j < studentList.get(i).getRegisteredIndexes().size(); j++)
			{
				sb.append(studentList.get(i).getRegisteredIndexes().get(j).getIndexId());
				sb.append(SEPARATOR);
			}
			sb.append(",");
			sb.append(SEPARATOR);
			for(int j = 0; j < studentList.get(i).getWaitingIndexes().size(); j++)
			{
				sb.append(studentList.get(i).getWaitingIndexes().get(j).getIndexId());
				sb.append(SEPARATOR);
			}
			data.add(sb.toString());
		}
		FileRW.write(REGISTEREDFILE, data);
		return true;
	}
}
