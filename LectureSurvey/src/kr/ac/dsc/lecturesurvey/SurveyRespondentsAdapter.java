package kr.ac.dsc.lecturesurvey;


import java.util.ArrayList;

import kr.ac.dsc.lecturesurvey.model.Respondent;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


public class SurveyRespondentsAdapter extends BaseAdapter {

	private Context context;
	private LayoutInflater inflater;
	private ArrayList<Respondent> listarray;
	
	public SurveyRespondentsAdapter(Context context, ArrayList<Respondent> listarray) {
		super();
		this.context = context;
		this.listarray = listarray;
		this.inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return listarray.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return listarray.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return listarray.get(arg0).getUid();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.survey_respondent_list_row, parent, false);
		}

		TextView dept = (TextView) convertView
		.findViewById(R.id.survey_respondent_list_row_dept);
		dept.setText(listarray.get(position).getDeptname());
		
		TextView name = (TextView) convertView
		.findViewById(R.id.survey_respondent_list_row_name);
		name.setText(listarray.get(position).getName());

		TextView email = (TextView) convertView
		.findViewById(R.id.survey_respondent_list_row_email);
		email.setText(listarray.get(position).getEmail());

		TextView student_id = (TextView) convertView
		.findViewById(R.id.survey_respondent_list_row_student_id);
		student_id.setText("학번 : "+ listarray.get(position).getStudentID());

		TextView cnt = (TextView) convertView
		.findViewById(R.id.survey_respondent_list_row_respond_cnt);
		cnt.setText(listarray.get(position).getRespondCnt() + "");
		
		return convertView;
		
	}

}
