package kr.ac.dsc.lecturesurvey.model;

import java.io.Serializable;

public class User implements Serializable {

	private int uid;
	private String email;
	private String name;
	private String deptname;
	private int usertype;	// 0: 학생, 1:교수
	
	public User() {
		super();
		this.uid = 0;
		this.email = "";
		this.name = "";
		this.deptname = "";
		this.usertype = -1;
	}
	
	public User(int uid, String email, String name, String deptname, int usertype) {
		super();
		this.uid = uid;
		this.name = name;
		this.deptname = deptname;
		this.email = email;
		this.usertype = usertype;
	}
	
	public int getUid() {
		return uid;
	}
	public void setUid(int uid) {
		this.uid = uid;
	}
	public int getUsertype() {
		return usertype;
	}
	public void setUsertype(int usertype) {
		this.usertype = usertype;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDeptname() {
		return deptname;
	}
	public void setDeptname(String deptname) {
		this.deptname = deptname;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	
}
