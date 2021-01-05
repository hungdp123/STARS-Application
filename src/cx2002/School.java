package cx2002;

import java.util.Date;

public class School implements Comparable<School>
{
	private String name;
	private Date timeStart;
	private Date timeEnd;
	
	public School(String name, Date timeStart, Date timeEnd)
	{
		this.name = name;
		this.timeStart = timeStart;
		this.timeEnd = timeEnd;
	}

	public School(String school) 
	{
		this.name = school;
	}

	public String getName()
	{
		return name;
	}

	public Date getTimeStart()
	{
		return timeStart;
	}
	
	public void setTimeStart(Date timeStart)
	{
		this.timeStart = timeStart;
	}

	public Date getTimeEnd()
	{
		return timeEnd;
	}
	
	public void setTimeEnd(Date timeEnd)
	{
		this.timeEnd = timeEnd;
	}
	
	public int compareTo(School s)
	{
		return this.getName().compareTo(s.getName());
	}
}
