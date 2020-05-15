package org.meowcat.wearable.chain.heytap.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.meowcat.wearable.chain.R;
import org.meowcat.wearable.chain.heytap.model.AchievementModel;

/**
 * 被 luern0313 创建于 2020/5/14.
 */
public class AchievementAdapter extends BaseAdapter
{
    private LayoutInflater mInflater;

    private AchievementModel achievementModel;

    public AchievementAdapter(LayoutInflater inflater)
    {
        mInflater = inflater;
        this.achievementModel = AchievementModel.getInstance();
    }

    @Override
    public int getCount()
    {
        return achievementModel.getAchievementMap().size();
    }

    @Override
    public Object getItem(int position)
    {
        return position;
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup)
    {
        String key = achievementModel.getAchievementMap().keySet().toArray(new String[]{})[position];
        ViewHolder viewHolder;
        if(convertView == null)
        {
            convertView = mInflater.inflate(R.layout.item_achievement, null);
            viewHolder = new ViewHolder();
            convertView.setTag(viewHolder);
            viewHolder.name = convertView.findViewById(R.id.item_achievement_name);
            viewHolder.num = convertView.findViewById(R.id.item_achievement_num);
        }
        else
        {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.name.setText(key);
        viewHolder.num.setText(String.valueOf(achievementModel.getAchievementMap().get(key)));

        return convertView;
    }

    class ViewHolder
    {
        TextView name;
        TextView num;
    }

}
