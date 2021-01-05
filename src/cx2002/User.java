package cx2002;

import java.io.Serializable;

public class User implements Comparable<User>, Serializable
{
	private String username;
	private String password;
	private int domain;
	private String matric;
	
	public User(String username, String password, int domain, String matric)
	{
		this.username = username;
		this.password = password;
		this.domain = domain;
		this.matric = matric;
	}

	public User(String username) 
	{
		this.username = username;
	}

	public String getUsername()
	{
		return username;
	}
	
	public String getPassword()
	{
		return password;
	}
	
	public void setPassword(String password)
	{
		this.password = password;
	}
	
	public int getDomain()
	{
		return domain;
	}
	
	public String getMatric()
	{
		return matric;
	}
	
	public int compareTo(User u)
	{
		return this.getUsername().compareTo(u.getUsername());
	}
}
