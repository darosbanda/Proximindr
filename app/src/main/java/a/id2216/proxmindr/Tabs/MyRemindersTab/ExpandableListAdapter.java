package a.id2216.proxmindr.Tabs.MyRemindersTab;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import a.id2216.proxmindr.R;
import a.id2216.proxmindr.ReminderPackage.ReminderStorage;

public class ExpandableListAdapter extends BaseExpandableListAdapter {
    private ReminderStorage reminderStorage = ReminderStorage.getInstance();
    private final LayoutInflater inf;
    private ArrayList<String> groups;
    private ArrayList<List<String>> children;

    public ExpandableListAdapter(ArrayList<String> groups, ArrayList<List<String>> children, LayoutInflater inf) {
        this.groups = groups;
        this.children = children;
        this.inf = inf;
    }

    @Override
    public int getGroupCount() {
        return groups.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return children.get(groupPosition).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return groups.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return children.get(groupPosition).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                convertView = inf.inflate(R.layout.list_item, parent, false);
                holder = new ViewHolder();
                holder.text = convertView.findViewById(R.id.lblListItem);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            if (childPosition == 0) {
                holder.text.setVisibility(View.GONE);
                return convertView;
            }
            holder.text.setText(getChild(groupPosition, childPosition).toString());
            holder.text.setVisibility(View.VISIBLE);
            return convertView;

    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = inf.inflate(R.layout.list_group, parent, false);

            holder = new ViewHolder();
            holder.text = convertView.findViewById(R.id.lblListHeader);
            holder.button = convertView.findViewById(R.id.delete_reminder_button);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.text.setText(getGroup(groupPosition).toString());
        holder.button.setOnClickListener(v -> {
            List<String> temp = children.get(groupPosition);
            reminderStorage.removeReminder(temp.get(0));
        });
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    private class ViewHolder {
        TextView text;
        Button button;
    }
}