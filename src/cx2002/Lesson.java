package cx2002;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Lesson 
{
	private String type;
	private String group;
	private String day;
	private String time;
	private String venue;
	private String remarks;
	private Date timeStart;
	private Date timeEnd;
	private String week;
	
	public Lesson(String type, String group, String day, String time, String venue, String remarks) throws ParseException
	{
		this.type = type;
		this.group = group;
		this.day = day;
		this.time = time;
		this.venue = venue;
		this.remarks = remarks;
		
		final SimpleDateFormat df = new SimpleDateFormat("EEE HHmm");
		String timeSplit[] = time.split("-");
		String start = day + " " + timeSplit[0];
		String end =  day + " " + timeSplit[1];
		this.timeStart = df.parse(start);
		this.timeEnd = df.parse(end);
		if(remarks.contains("Teaching Wk1,3,5,7,9,11,13"))
		{
			this.week = "odd";
		}
		else if(remarks.contains("Teaching Wk2,4,6,8,10,12"))
		{
			this.week = "even";
		}
		else
		{
			this.week = "both";
		}
	}
	
	public String getType()
	{
		return type;
	}
	
	public String getGroup()
	{
		return group;
	}
	
	public String getDay()
	{
		return day;
	}
	
	public String getTime()
	{
		return time;
	}
	
	public String getVenue()
	{
		return venue;
	}
	
	public String getRemarks()
	{
		return remarks;
	}
	
	public boolean getClash(Lesson l)
	{
		if(this.getType().equals("LEC/STUDIO") && l.getType().equals("LEC/STUDIO"))
			return false;
		else if(this.week.equals("odd") && l.week.equals("odd"))
			return this.timeStart.before(l.timeEnd) && l.timeStart.before(this.timeEnd);
		else if(this.week.equals("even") && l.week.equals("even"))
			return this.timeStart.before(l.timeEnd) && l.timeStart.before(this.timeEnd);
		else if(this.week.equals("odd") && l.week.equals("even") || this.week.equals("even") && l.week.equals("odd"))
			return false;
		else
			return this.timeStart.before(l.timeEnd) && l.timeStart.before(this.timeEnd);
	}

	public void display() 
	{
		System.out.printf("%-13s %-8s %-8s %-13s %-10s %-10s\n", type, group, day, time, venue, remarks);
	}
}
