package edu.uci.ics.icssc.projects.qsadiq;

import android.app.ListActivity;
import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import android.view.*;

public class ClassList extends ListActivity{
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		Bundle b = getIntent().getExtras();
		final String[] profs = b.getStringArray("edu.uci.ics.icssc.projects.qsadiq.classList.profs");
		String[] courses = b.getStringArray("edu.uci.ics.icssc.projects.qsadiq.classList.courseNumbers");
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, courses);
		setListAdapter(adapter);

		ListView lv = getListView();
		lv.setTextFilterEnabled(true);

		lv.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

				Toast.makeText(getApplicationContext(), profs[position],Toast.LENGTH_SHORT).show();
			}
		});
	}
}
