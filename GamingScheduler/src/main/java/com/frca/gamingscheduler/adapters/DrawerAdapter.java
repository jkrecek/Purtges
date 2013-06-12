package com.frca.gamingscheduler.adapters;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.frca.gamingscheduler.R;

public class DrawerAdapter extends ArrayAdapter<String> {

    final DrawerItem[] items;
    final private int resourceLayout;

    public DrawerAdapter(Context context, int resourceLayout, DrawerItem[] items) {
        super(context, resourceLayout);
        this.items = items;
        this.resourceLayout = resourceLayout;
    }

    @Override
    public int getCount() {
        return items.length;
    }

    public View getView(final int position, final View convertView, final ViewGroup parent) {

        DrawerItem item = items[position];

        View itemView;
        if (convertView != null && convertView.findViewById(R.id.text_header) != null)
            itemView = convertView;
        else {
            final LayoutInflater inflater = (LayoutInflater) getContext().getSystemService
                    (Context.LAYOUT_INFLATER_SERVICE);

            if (!TextUtils.isEmpty(item.header))
                itemView = inflater.inflate(resourceLayout, null);
            else
                return inflater.inflate(R.layout.item_drawer_divider, null);
        }

        TextView header = (TextView) itemView.findViewById(R.id.text_header);
        TextView content = (TextView) itemView.findViewById(R.id.text_content);
        ImageView icon = (ImageView) itemView.findViewById(R.id.icon);

        header.setText(item.header);
        if (!TextUtils.isEmpty(item.content))
            content.setText(item.content);
        else
            content.setVisibility(View.GONE);

        if (item.iconId != 0)
            icon.setImageResource(item.iconId);
        else
            icon.setVisibility(View.GONE);

        return itemView;
    }

    public static class DrawerItem {
        public int iconId;
        public String header;
        public String content;

        public DrawerItem(String header, int iconId, String content) {
            this.header = header;
            this.iconId = iconId;
            this.content = content;
        }

        public DrawerItem(String header, int iconId) {
            this(header, iconId, null);
        }
    }
}
