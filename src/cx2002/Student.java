package cx2002;

import java.util.ArrayList;

public class Student implements Comparable<Student>
{
	private String matric;
	private String name;
	private String email;
	private String gender;
	private String nationality;
	private School school;
	private int year;
	private ArrayList<Index> registeredIndexes = new ArrayList<Index>();
	private ArrayList<Index> waitingIndexes = new ArrayList<Index>();

	public Student(String matric, String name, String email, String gender, String nationality, School school, int year) 
	{
		this.matric = matric;
		this.name = name;
		this.email = email;
		this.gender = gender;
		this.nationality = nationality;
		this.school = school;
		this.year = year;
	}
	
	public Student(String matric)
	{
		this.matric = matric;
	}

	public String getMatric()
	{
		return matric;
	}

	public String getName()
	{
		return name;
	}

	public String getEmail()
	{
		return email;
	}

	public String getGender()
	{
		return gender;
	}

	public String getNationality()
	{
		return nationality;
	}

	public School getSchool()
	{
		return school;
	}

	public int getYear()
	{
		return year;
	}

	public ArrayList<Index> getRegisteredIndexes()
	{
		return registeredIndexes;
	}
	
	public void setRegisteredList(ArrayList<Index> registeredIndexes)
	{
		this.registeredIndexes = registeredIndexes;
	}
	
	public ArrayList<Index> getWaitingIndexes()
	{
		return waitingIndexes;
	}
	
	public void setWaitingList(ArrayList<Index> waitingIndexes)
	{
		this.waitingIndexes = waitingIndexes;
	}
	
	public int getTotalAU()
	{
		int total = 0;
		for(Index index: this.getRegisteredIndexes())
			total += index.getCourse().getAU();
		return total;
	}
	
	public boolean checkClash(Index i)
	{
		for(Index index: this.getRegisteredIndexes())
			if(index.checkClash(i))
				return true;
		return false;
	}
	
	public void displayLimited()
	{
		System.out.printf("%-20s %-7s %-10s\n", this.getName(), this.getGender(), this.getNationality());
	}
	
	public void displayDetailed()
	{
		System.out.printf("Name: %-15s Matric: %-10s School: %-5s Year: %d\n", this.getName(), this.getMatric(), this.getSchool().getName(), this.getYear());
	}
	
	public int compareTo(Student s)
	{
		return this.getMatric().compareTo(s.getMatric());
	}
}
