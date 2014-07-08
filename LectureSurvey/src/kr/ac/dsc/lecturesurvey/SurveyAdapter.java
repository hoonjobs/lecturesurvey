package kr.ac.dsc.lecturesurvey;

import java.util.ArrayList;

import kr.ac.dsc.lecturesurvey.model.Survey;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class SurveyAdapter extends BaseAdapter {

	private Context context;
	private LayoutInflater inflater;
	private ArrayList<Survey> listarray;
	
	public SurveyAdapter(Context context, ArrayList<Survey> listarray) {
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
			convertView = inflater.inflate(R.layout.survey_list_row, parent, false);
		}
		
		ImageView icStatus = (ImageView)convertView
				.findViewById(R.id.survey_list_row_ivStatus);
		
		//설문중일때 아이콘
		if(listarray.get(position).getStatus() < 2) {
			icStatus.setImageResource(R.drawable.ic_alert);
		}
		else {
			icStatus.setImageResource(R.drawable.ic_star);
		}
		
		//학과명 및 교수님 이름 출력
		TextView deptname = (TextView) convertView
				.findViewById(R.id.survey_list_row_tvDeptName);
		deptname.setText(listarray.get(position).getDeptName() + "  /  " 
				+listarray.get(position).getProfName() );

		//강의명
		TextView name = (TextView) convertView
				.findViewById(R.id.survey_list_row_tvLectureName);
		name.setText(listarray.get(position).getLectureName());
		
		//강의일
		TextView date = (TextView) convertView
				.findViewById(R.id.survey_list_row_tvLectureDate);
		date.setText(listarray.get(position).getLectureDate());
		
		return convertView;
		
	}

}
