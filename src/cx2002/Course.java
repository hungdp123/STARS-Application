package cx2002;

public class Course implements Comparable<Course>
{
	private School school;
	private String courseId;
	private int au;
	public Course(School school, String courseId, int au)
	{
		this.school = school;
		this.courseId = courseId;
		this.au = au;
	}
	
	public Course(String courseId) 
	{
		this.courseId = courseId;
	}

	public School getSchool()
	{
		return school;
	}
	
	public String getCourseId()
	{
		return courseId;
	}
	
	public int getAU()
	{
		return au;
	}
	
	public int compareTo(Course c)
	{
		return this.getCourseId().compareTo(c.getCourseId());
	}
}
