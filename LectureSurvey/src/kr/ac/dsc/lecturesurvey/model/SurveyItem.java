package kr.ac.dsc.lecturesurvey.model;

import java.io.Serializable;

public class SurveyItem implements Serializable {

	private int idx;		//설문 번호
	private int surveyIdx;		//설문지 번호
	private String question;	//설문 질문
	private int answer;		//설문응답 번호 / 1:1번(그렇다), 2:2반(보통이다), 3:3번(아니다)

	private int answerCnt1;	//1번 응답  수
	private int answerCnt2; //2번 응답  수
	private int answerCnt3; //3번 응답  수
	
	//학생
	public SurveyItem(int idx, int surveyIdx, String question) {
		super();
		this.idx = idx;
		this.surveyIdx = surveyIdx;
		this.question = question;
	}

	//학생
	public SurveyItem(int idx, int surveyIdx, String question, int answer) {
		super();
		this.idx = idx;
		this.surveyIdx = surveyIdx;
		this.question = question;
		this.answer = answer;
	}

	//교수
	public SurveyItem(int idx, int surveyIdx, String question, int answer,
			int answerCnt1, int answerCnt2, int answerCnt3) {
		super();
		this.idx = idx;
		this.surveyIdx = surveyIdx;
		this.question = question;
		this.answer = answer;
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

	public int getSurveyIdx() {
		return surveyIdx;
	}

	public void setSurveyIdx(int surveyIdx) {
		this.surveyIdx = surveyIdx;
	}

	public String getQuestion() {
		return question;
	}

	public void setQuestion(String question) {
		this.question = question;
	}

	public int getAnswer() {
		return answer;
	}

	public void setAnswer(int answer) {
		this.answer = answer;
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
