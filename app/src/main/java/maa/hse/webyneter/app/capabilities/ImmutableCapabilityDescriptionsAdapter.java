package maa.hse.webyneter.app.capabilities;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import maa.hse.webyneter.app.R;

/**
 * Created by webyn on 10/7/2016.
 */
public class ImmutableCapabilityDescriptionsAdapter extends BaseAdapter {
    private static LayoutInflater inflater;

    private final Context context;
    private final CapabilityDescription[] descriptions;

    public ImmutableCapabilityDescriptionsAdapter(Context context, CapabilityDescription[] descriptions) {
        this.context = context;
        this.descriptions = descriptions;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public Context getContext() {
        return context;
    }

    public CapabilityDescription[] getDescriptions() {
        return descriptions;
    }

    @Override
    public int getCount() {
        return descriptions.length;
    }

    @Override
    public Object getItem(int position) {
        return descriptions[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if (v == null) {
            v = inflater.inflate(R.layout.task_description_list_item, null);
        }

        CapabilityDescription descriptionForPosition = (CapabilityDescription) getItem(position);

        TextView tvName = (TextView) v.findViewById(R.id.taskdesclistitem_tvName);
        tvName.setText(descriptionForPosition.getName());

        ImageView ivIcon = (ImageView) v.findViewById(R.id.taskdesclistitem_ivIcon);
        ivIcon.setImageResource(descriptionForPosition.getImageResourceId());

        TextView tvDescription = (TextView) v.findViewById(R.id.taskdesclistitem_tvDescription);
        tvDescription.setText(descriptionForPosition.getDescription());

        return v;
    }
}
