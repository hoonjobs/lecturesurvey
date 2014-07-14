package kr.ac.dsc.lecturesurvey.model;

import java.io.Serializable;

public class User implements Serializable {

	private int uid;
	private String name;
	private String deptname;
	private String studentID;
	private String email;
	private int usertype;	// 0: 학생, 1:교수
	
	public User() {
		super();
		this.uid = 0;
		this.email = "";
		this.name = "";
		this.deptname = "";
		this.usertype = -1;
	}

	public User(int uid, String name, String deptname, String studentID,
			String email, int usertype) {
		super();
		this.uid = uid;
		this.name = name;
		this.deptname = deptname;
		this.studentID = studentID;
		this.email = email;
		this.usertype = usertype;
	}

	public int getUid() {
		return uid;
	}

	public void setUid(int uid) {
		this.uid = uid;
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

	public String getStudentID() {
		return studentID;
	}

	public void setStudentID(String studentID) {
		this.studentID = studentID;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public int getUsertype() {
		return usertype;
	}

	public void setUsertype(int usertype) {
		this.usertype = usertype;
	}
	
	
}
