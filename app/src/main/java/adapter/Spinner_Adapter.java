package adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.deeps.ips.R;
import com.example.deeps.ips.SpinnerGet;

import java.util.ArrayList;

public class Spinner_Adapter extends BaseAdapter {

    Context context;
    ArrayList<SpinnerGet> list;
    LayoutInflater inflater;
    public Spinner_Adapter(Context context, ArrayList<SpinnerGet> list) {
        this.context = context;
        this.list = list;
        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }



    public class ViewHolder
    {
        TextView spin_nm;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if(convertView==null)
        {
            viewHolder=new ViewHolder();
            convertView=inflater.inflate(R.layout.spin_raw, parent, false);

            viewHolder.spin_nm= (TextView) convertView.findViewById(R.id.spin_txt);
            convertView.setTag(viewHolder);
        }
        else
        viewHolder= (ViewHolder) convertView.getTag();

        viewHolder.spin_nm.setText(""+list.get(position).get_class());

        return convertView;
    }
}
