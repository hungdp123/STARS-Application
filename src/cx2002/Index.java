package cx2002;

import java.util.ArrayList;

public class Index implements Comparable<Index>
{
	private Course course;
	private int indexId;
	private int size;
	private ArrayList<Student> registeredStudents = new ArrayList<Student>();
	private ArrayList<Student> waitingStudents;
	private ArrayList<Lesson> lessonList;
	
	public Index(Course course, int indexId, int size, ArrayList<Student> waitingStudents)
	{
		this.course = course;
		this.indexId = indexId;
		this.size = size;
		this.waitingStudents = waitingStudents;
	}
	
	public Index(int indexId) 
	{
		this.indexId = indexId;
	}

	public Course getCourse() 
	{
		return course;
	}
	
	public Integer getIndexId() 
	{
		return indexId;
	}
	
	public int getSize() 
	{
		return size;
	}
	
	public ArrayList<Student> getRegisteredStudents()
	{
		return registeredStudents;
	}
	
	public ArrayList<Student> getWaitingStudents()
	{
		return waitingStudents;
	}

	public ArrayList<Lesson> getLessonList()
	{
		return lessonList;
	}
	
	public void setLessonList(ArrayList<Lesson> lessonList)
	{
		this.lessonList = lessonList;
	}
	
	public boolean checkClash(Index i)
	{
		for(Lesson l1: this.lessonList)
			for(Lesson l2: i.getLessonList())
				if(l1.getClash(l2))
				{
					System.out.println("Time clash on " + l1.getDay() + " at " + l1.getTime());
					return true;
				}
		return false;
	}
	
	public void display()
	{
		System.out.printf("Index Number: %d\t\t\tModule: %s\n", this.indexId, this.getCourse().getCourseId());
		System.out.printf("%-13s %-8s %-8s %-13s %-10s %-10s\n", "Type", "Group", "Day", "Time", "Venue", "Remarks");
		for(int i = 0; i < this.getLessonList().size(); i++)
			this.getLessonList().get(i).display();
		System.out.printf("Vacancy: %d/%d\t\t\t\tWait List: %d\n", this.getSize() - this.getRegisteredStudents().size(), this.getSize(), this.getWaitingStudents().size());
		System.out.println("");
	}

	public int compareTo(Index i)
	{
		return this.getIndexId().compareTo(i.getIndexId());
	}
}
