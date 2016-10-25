package androidsandbox.org.webyneter.app.features;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidsandbox.org.webyneter.app.R;

/**
 * Created by webyn on 10/7/2016.
 */
public class ImmutableFeatureDescriptionsAdapter extends BaseAdapter {
    private static LayoutInflater inflater;

    private final Context context;
    private final FeatureDescription[] descriptions;

    public ImmutableFeatureDescriptionsAdapter(Context context, FeatureDescription[] descriptions) {
        this.context = context;
        this.descriptions = descriptions;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public Context getContext() {
        return context;
    }

    public FeatureDescription[] getDescriptions() {
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
            v = inflater.inflate(R.layout.feature_description_list_item, null);
        }

        FeatureDescription descriptionForPosition = (FeatureDescription) getItem(position);

        TextView tvName = (TextView) v.findViewById(R.id.featuredesclistitem_tvName);
        tvName.setText(descriptionForPosition.getName());

        ImageView ivIcon = (ImageView) v.findViewById(R.id.featuredesclistitem_ivIcon);
        ivIcon.setImageResource(descriptionForPosition.getImageResourceId());

        TextView tvDescription = (TextView) v.findViewById(R.id.featuredesclistitem_tvDescription);
        tvDescription.setText(descriptionForPosition.getDescription());

        return v;
    }
}
