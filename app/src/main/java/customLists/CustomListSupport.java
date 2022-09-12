package customLists;

import java.util.ArrayList;

import dataInLists.DataInSupport;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.usmart.com.rummanah.R;

@SuppressLint("ResourceAsColor")
public class CustomListSupport extends ArrayAdapter<DataInSupport> {
	private Activity Activity;
	private ArrayList<DataInSupport> Data;

	public CustomListSupport(Activity context, ArrayList<DataInSupport> data) {
		super(context, R.layout.singel_support_list, data);
		// TODO Auto-generated constructor stub
		this.Activity = context;
		this.Data = data;

	}

	@SuppressLint({ "ViewHolder", "InflateParams" })
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = Activity.getLayoutInflater();
		View rowView = inflater.inflate(R.layout.singel_support_list, null, true);
		TextView txtTitle = rowView.findViewById(R.id.tv_icon1);

		Typeface font = Typeface.createFromAsset(Activity.getAssets(), "fonts/DroidKufi-Bold.ttf");
		txtTitle.setTypeface(font);

		txtTitle.setText(Data.get(position).mobile);

		return rowView;
	}

}
