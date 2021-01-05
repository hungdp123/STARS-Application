package cx2002;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class StudentOptions 
{
	private static Scanner sc = new Scanner(System.in);
	
	public static boolean addModule(ArrayList<Index> indexList, Student stud)
	{
		int chosenIndexNumber;
		while(true)
		{
			try
			{
				System.out.printf("Enter index: ");
			    chosenIndexNumber = Integer.parseInt(sc.nextLine());
				break;
			}
			catch(NumberFormatException e)
			{
				System.out.println("Please enter integer!");
			}
		}
	    int i = Collections.binarySearch(indexList, new Index(chosenIndexNumber));
	    if(i < 0)
	    {
	    	System.out.println("Index does not exist!");
	    	return false;
	    }
	    Index index = indexList.get(i);
	    
	    if(stud.getRegisteredIndexes().contains(index))
	    {
	    	System.out.println("Index already registered!");
	    	return false;
	    }
	    else if(!stud.getSchool().equals(index.getCourse().getSchool()))
	    {
	    	System.out.println("Index from different school!");
	    	return false;
	    }
	    else if(stud.getWaitingIndexes().contains(index))
	    {
	    	System.out.println("Index already in waiting list!");
	    	return false;
	    }
	    for(Index j: stud.getRegisteredIndexes())
	    	if(j.getCourse().equals(index.getCourse()))
	    	{
	    		System.out.println("Course already registered with index: " + j.getIndexId() + "!");
	    		return false;
	    	}
	    
	    index.display();
  	  	boolean loop = true;
  	  	while(loop)
  	  	{
	  	  	System.out.printf("Do you wish to add this index? (Y/N): ");
	  	  	String choice = sc.nextLine().toUpperCase();
	  	  	switch(choice)
	  	  	{
	  	  	case "Y":
	  	  		if(stud.checkClash(index))
	  	  		{
	  	  			System.out.println("Timetable clash!");
	  	  			return false;
	  	  		}
	  	  		else if(stud.getTotalAU() + index.getCourse().getAU() > MainApplication.AULIMIT)
	  	  		{
	  	  			System.out.println("Total AU exceeds " + MainApplication.AULIMIT + "!");
	  	  			return false;
	  	  		}
	  	  		loop = false;
	  	  		break;
	  	  		
	  	  	case "N":
	  	  		System.out.println("You have chosen no.");
	  	  		return false;
	  	  		
	  	  	default:
	  	  		System.out.println("Invalid input!");
	  	  	}
  	  	}
  	  	
  	  	if(index.getSize() > index.getRegisteredStudents().size())
  	  	{
	  		stud.getRegisteredIndexes().add(index);
	  		index.getRegisteredStudents().add(stud);
	  		Collections.sort(stud.getRegisteredIndexes());
	  		Collections.sort(index.getRegisteredStudents());
	  		System.out.println("Index successfully registered!");
  	  	}
  	  	else
  	  	{
	  	  	stud.getWaitingIndexes().add(index);
	  		index.getWaitingStudents().add(stud);
	  		Collections.sort(stud.getWaitingIndexes());
	  		System.out.println("Index added to waiting list!");
  	  	}
  	  	return true;
	}

	public static boolean dropModule(Student stud)
	{
		int chosenIndexNumber;
		while(true)
		{
			try
			{
				System.out.printf("Enter index: ");
			    chosenIndexNumber = Integer.parseInt(sc.nextLine());
				break;
			}
			catch(NumberFormatException e)
			{
				System.out.println("Please enter integer!");
			}
		}
	    
	    int i;
	    Index index;
	    boolean waiting = false;
	    i = Collections.binarySearch(stud.getRegisteredIndexes(), new Index(chosenIndexNumber));
	    if(i < 0)
	    {
	    	waiting = true;
	    	i = Collections.binarySearch(stud.getWaitingIndexes(), new Index(chosenIndexNumber));
	    	if(i < 0)
		    {
		    	System.out.println("Index not registered!");
		    	return false;
		    }
	    	else
	    	{
	    		index = stud.getWaitingIndexes().get(i);
	    	}
	    }
	    else
	    {
	    	index = stud.getRegisteredIndexes().get(i);
	    }
	    
	    index.display();
	    boolean loop = true;
	    while(loop)
  	  	{
		    System.out.printf("Do you wish to drop this index? (Y/N):");
	  	  	String choice = sc.nextLine().toUpperCase();
	  	  	switch(choice)
	  	  	{
	  	  	case "Y":
	  	  		loop = false;
	  	  		break;
	  	  		
	  	  	case "N":
	  	  		System.out.println("You have chosen no.");
	  	  		return false;
	  	  		
	  	  	default:
	  	  		System.out.println("Invalid input!");
	  	  	}
  	  	}
	    if(!waiting)
	    {
	    	stud.getRegisteredIndexes().remove(index);
	    	index.getRegisteredStudents().remove(stud);
	    	Collections.sort(stud.getRegisteredIndexes());
	    	Collections.sort(index.getRegisteredStudents());
	    	System.out.println("Index successfully dropped!");
	    }
	    else
	    {
	    	stud.getWaitingIndexes().remove(index);
	    	index.getWaitingStudents().remove(stud);
	    	Collections.sort(stud.getWaitingIndexes());
	    	System.out.println("Index removed from waiting list!");
	    }
	    return true;
	}
	
	public static void printCourses(ArrayList<Index> indexList, Student stud)
	{
		if(!stud.getRegisteredIndexes().isEmpty() || !stud.getWaitingIndexes().isEmpty())
		{
			if(!stud.getRegisteredIndexes().isEmpty())
			{
				System.out.println("Registered courses: ");
				for(int i = 0; i < stud.getRegisteredIndexes().size(); i++)
					stud.getRegisteredIndexes().get(i).display();
			}
			
			if(!stud.getWaitingIndexes().isEmpty())
			{
				System.out.println("WaitList courses: ");
				for(int i = 0; i < stud.getWaitingIndexes().size(); i++)
					stud.getWaitingIndexes().get(i).display();
			}
		}
		else
			System.out.println("No courses registered!");
	}
	
	public static void checkVacancy(ArrayList<Index> indexList)
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
				catch(NumberFormatException e)
				{
					System.out.println("Please enter integer!");
				}
			}
			
			if(indexId == -1)
				break;
			
			int i = Collections.binarySearch(indexList, new Index(indexId));
			if(i >= 0)
				indexList.get(i).display();
			else
				System.out.println("Index not found!\n");
		}
	}
	
	public static boolean swapIndex(ArrayList<Index> indexList, Student stud)
	{
		int indexId;
		while(true)
		{
			try 
			{
				System.out.printf("Enter current index number: ");
				indexId = Integer.parseInt(sc.nextLine());
				break;
			} 
			catch (NumberFormatException e) 
			{
				System.out.println("Please enter an integer!");
			}
		}
		

		int i = Collections.binarySearch(indexList, new Index(indexId));
		if(i < 0)
		{
			System.out.println("Index does not exist!");
			return false;
		}
		Index indexCurrent = indexList.get(i);
		
		if(!stud.getRegisteredIndexes().contains(indexCurrent))
		{
			System.out.println("Index not registered!");
			return false;
		}
		
		while(true)
		{
			try
			{
				System.out.printf("Enter new index number: ");
				indexId = Integer.parseInt(sc.nextLine());
				break;
			}
			catch(NumberFormatException e)
			{
				System.out.println("Please enter integer!");
			}
		}
		i = Collections.binarySearch(indexList, new Index(indexId));
		if(i < 0)
		{
			System.out.println("Index does not exist!");
			return false;
		}
		Index indexNew = indexList.get(i);
		
		if(!indexNew.getCourse().getCourseId().equals(indexCurrent.getCourse().getCourseId()))
		{
			System.out.println("Index from different module!");
			return false;
		}
		else if(!(indexNew.getSize() > indexNew.getRegisteredStudents().size()))
		{
			System.out.println("New index has no vacancy!");
			return false;
		}
		else if(stud.checkClash(indexNew))
		{
			System.out.println("Timetable clash!");
			return false;
		}
		
		indexCurrent.getRegisteredStudents().remove(stud);
		indexNew.getRegisteredStudents().add(stud);
		stud.getRegisteredIndexes().remove(indexCurrent);
		stud.getRegisteredIndexes().add(indexNew);
		Collections.sort(indexCurrent.getRegisteredStudents());
		Collections.sort(indexNew.getRegisteredStudents());
		Collections.sort(stud.getRegisteredIndexes());
		
		System.out.printf("Index number %d has been changed to %d\n", indexCurrent.getIndexId(), indexNew.getIndexId());
		return true;
	}
	
	public static boolean swapStudent(ArrayList<Index> indexList, Student stud1, Student stud2)
	{
		int indexId;
		while(true)
		{
			try
			{
				System.out.printf("Enter student 1 index number: ");
				indexId = Integer.parseInt(sc.nextLine());
				break;
			}
			catch(NumberFormatException e)
			{
				System.out.println("Please enter integer!");
			}
		}
		
		int i = Collections.binarySearch(indexList, new Index(indexId));
		if(i < 0)
		{
			System.out.println("Index does not exist!");
			return false;
		}
		Index indexCurrent = indexList.get(i);
		
		if(!stud1.getRegisteredIndexes().contains(indexCurrent))
		{
			System.out.println("Index not registered!");
			return false;
		}
		
		while(true)
		{
			try
			{
				System.out.printf("Enter student 2 index number: ");
				indexId = Integer.parseInt(sc.nextLine());
				break;
			}
			catch(NumberFormatException e)
			{
				System.out.println("Please enter integer!");
			}
		}
		
		i = Collections.binarySearch(indexList, new Index(indexId));
		if(i < 0)
		{
			System.out.println("Index does not exist!");
			return false;
		}
		Index indexNew = indexList.get(i);
		
		if(!stud2.getRegisteredIndexes().contains(indexNew))
		{
			System.out.println("Index not registered!");
			return false;
		}
		else if(!indexNew.getCourse().getCourseId().equals(indexCurrent.getCourse().getCourseId()))
		{
			System.out.println("Index from different module!");
			return false;
		}
		
		if(stud1.checkClash(indexNew))
		{
			System.out.println("Timetable clash for student 1!");
			return false;
		}
		else if(stud2.checkClash(indexCurrent))
		{
			System.out.println("Timetable clash for student 2!");
			return false;
		}
		
		indexCurrent.getRegisteredStudents().remove(stud1);
		indexCurrent.getRegisteredStudents().add(stud2);
		indexNew.getRegisteredStudents().add(stud2);
		indexNew.getRegisteredStudents().add(stud1);
		stud1.getRegisteredIndexes().remove(indexCurrent);
		stud1.getRegisteredIndexes().add(indexNew);
		stud2.getRegisteredIndexes().remove(indexNew);
		stud2.getRegisteredIndexes().add(indexCurrent);
		Collections.sort(indexCurrent.getRegisteredStudents());
		Collections.sort(indexNew.getRegisteredStudents());
		Collections.sort(stud1.getRegisteredIndexes());
		Collections.sort(stud2.getRegisteredIndexes());
			
		System.out.printf("%s index number %d has been successfully swapped with %s index number %d\n", stud1.getMatric(), indexCurrent.getIndexId(), stud2.getMatric(), indexNew.getIndexId());
		return true;
	}
}
