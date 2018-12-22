package dandandateam.dandanda.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import dandandateam.dandanda.Items.GenderOption;
import dandandateam.dandanda.R;

public class GenderAdapter extends ArrayAdapter<GenderOption> {

    public GenderAdapter(Context context, List<GenderOption> donationOptions) {
        super(context, 0, donationOptions);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder vh;
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_gender, parent, false);
            vh = new ViewHolder(convertView);
            convertView.setTag(vh);
        } else vh = (ViewHolder) convertView.getTag();

        GenderOption option = getItem(position);

        vh.text.setText(option.gender);

        return convertView;
    }

    private static final class ViewHolder {
        TextView text;

        public ViewHolder(View v) {
            text = (TextView) v.findViewById(R.id.item_gender_text);
        }
    }
}
