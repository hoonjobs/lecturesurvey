package kr.ac.dsc.lecturesurvey;


import java.util.ArrayList;

import kr.ac.dsc.lecturesurvey.model.SurveyItem;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


public class SurveyItemResultAdapter extends BaseAdapter {

	private Context context;
	private LayoutInflater inflater;
	private ArrayList<SurveyItem> listarray;
	
	public SurveyItemResultAdapter(Context context, ArrayList<SurveyItem> listarray) {
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
		return listarray.get(arg0).getIdx();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.survey_item_result_row, parent, false);
		}

		TextView idx = (TextView) convertView
		.findViewById(R.id.survey_item_list_row_idx);
		idx.setText(Integer.toString(position+1));
		
		TextView question = (TextView) convertView
		.findViewById(R.id.survey_item_list_row_question);
		question.setText(listarray.get(position).getQuestion());
		
		TextView ans1 = (TextView) convertView
		.findViewById(R.id.survey_item_list_row_result_answer1);
		ans1.setText(listarray.get(position).getAnswerCnt1() + " 명");
		
		TextView ans2 = (TextView) convertView
		.findViewById(R.id.survey_item_list_row_result_answer2);
		ans2.setText(listarray.get(position).getAnswerCnt2() + " 명");

		TextView ans3 = (TextView) convertView
		.findViewById(R.id.survey_item_list_row_result_answer3);
		ans3.setText(listarray.get(position).getAnswerCnt3() + " 명");
		
		return convertView;
		
	}

}
