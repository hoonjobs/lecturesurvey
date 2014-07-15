package kr.ac.dsc.lecturesurvey.model;

import java.io.Serializable;

public class Survey implements Serializable {

	private int idx;		//설문지 번호
	private String lectureName;	//강의명
	private String lectureDate;	//강의일
	private String deptName;	//강의 학과명
	private String profName;	//강의 교수님 이름
	private int status; 		//설문 상태// 0:설문대기, 1:설문중, 2:설문종료
	private String regDate;	//설문 등록일
	private String msg;		//설문요청 메세지
	
	private int answerCnt1;	//1번 응답  수
	private int answerCnt2; //2번 응답  수
	private int answerCnt3; //3번 응답  수

	private boolean	answerSurvey;	//설문응답 여부
	

	//학생
	public Survey(int idx, String lectureName, String lectureDate,
			String deptName, String profName, int status, String msg,
			boolean answerSurvey) {
		super();
		this.idx = idx;
		this.lectureName = lectureName;
		this.lectureDate = lectureDate;
		this.deptName = deptName;
		this.profName = profName;
		this.status = status;
		this.msg = msg;
		this.answerSurvey = answerSurvey;
	}

	//교수
	public Survey(int idx, String lectureName, String lectureDate,
			String deptName, String profName, int status, String regDate,
			String msg,
			int answerCnt1, int answerCnt2, int answerCnt3) {
		super();
		this.idx = idx;
		this.lectureName = lectureName;
		this.lectureDate = lectureDate;
		this.deptName = deptName;
		this.profName = profName;
		this.status = status;
		this.regDate = regDate;
		this.msg = msg;
		this.answerCnt1 = answerCnt1;
		this.answerCnt2 = answerCnt2;
		this.answerCnt3 = answerCnt3;
	}

	public int getIdx() {
		return idx;
	}

	public void setIdx(int idx) {
		this.idx = idx;
	}

	public String getLectureName() {
		return lectureName;
	}

	public void setLectureName(String lectureName) {
		this.lectureName = lectureName;
	}
	
	public String getLectureDate() {
		return lectureDate;
	}

	public void setLectureDate(String lectureDate) {
		this.lectureDate = lectureDate;
	}

	public String getDeptName() {
		return deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}

	public String getProfName() {
		return profName;
	}

	public void setProfName(String profName) {
		this.profName = profName;
	}

	public boolean isSurvey() {
		return answerSurvey;
	}

	public void setSurvey(boolean survey) {
		this.answerSurvey = survey;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getRegDate() {
		return regDate;
	}

	public void setRegDate(String regDate) {
		this.regDate = regDate;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}
	
	public int getAnswerCnt1() {
		return answerCnt1;
	}

	public void setAnswerCnt1(int answerCnt1) {
		this.answerCnt1 = answerCnt1;
	}

	public int getAnswerCnt2() {
		return answerCnt2;
	}

	public void setAnswerCnt2(int answerCnt2) {
		this.answerCnt2 = answerCnt2;
	}

	public int getAnswerCnt3() {
		return answerCnt3;
	}

	public void setAnswerCnt3(int answerCnt3) {
		this.answerCnt3 = answerCnt3;
	}
}
