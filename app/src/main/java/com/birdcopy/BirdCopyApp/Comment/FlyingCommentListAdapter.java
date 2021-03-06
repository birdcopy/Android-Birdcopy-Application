package com.birdcopy.BirdCopyApp.Comment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.birdcopy.BirdCopyApp.Scan.decoding.Intents;
import com.birdcopy.BirdCopyApp.ShareDefine;
import com.birdcopy.BirdCopyApp.R;
import com.squareup.picasso.Picasso;

/**
 * Created by vincentsung on 12/18/15.
 */
public class FlyingCommentListAdapter extends ArrayAdapter<FlyingCommentData> {

    private static final String TAG = "FlyingCommentListAdapter";

    static class ViewHolder {
        ImageView commentPortrait;
        TextView commentNickName;
        TextView commentTime;
        TextView commentContent;
    }

    private final LayoutInflater mLayoutInflater;

    public FlyingCommentListAdapter(final Context context, final int textViewResourceId) {
        super(context, textViewResourceId);
        mLayoutInflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {

        final ViewHolder vh;
        if (convertView == null) {

            convertView = mLayoutInflater.inflate(R.layout.comment_item, parent, false);
            vh = new ViewHolder();

            vh.commentPortrait = (ImageView) convertView.findViewById(R.id.comment_portrait);

            vh.commentNickName = (TextView) convertView.findViewById(R.id.comment_nickname);
            vh.commentTime = (TextView) convertView.findViewById(R.id.comment_time);
            vh.commentContent = (TextView) convertView.findViewById(R.id.comment_content);

            convertView.setTag(vh);
        }
        else
        {
            vh = (ViewHolder) convertView.getTag();
        }

        FlyingCommentData itemData = (FlyingCommentData) getItem(position);

        vh.commentNickName.setText(itemData.nickName);
        vh.commentContent.setText(itemData.commentContent);

        vh.commentTime.setText(ShareDefine.getdayhoursecond(itemData.commentTime));

        if(itemData.portraitURL!=null && ShareDefine.checkURL(itemData.portraitURL))
        {
            Picasso.with(getContext())
                    .load(itemData.portraitURL)
                    .into(vh.commentPortrait);
        }
        else
        {
            vh.commentPortrait.setImageResource(R.drawable.default_head);
        }

        //convertView.setBackgroundResource(R.drawable.abc_menu_dropdown_panel_holo_light);

        return convertView;
    }
}