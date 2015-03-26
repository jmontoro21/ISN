package com.inftel.isn.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.inftel.isn.R;
import com.inftel.isn.adapter.CommentGroupsAdapter;
import com.inftel.isn.model.Group;
import com.inftel.isn.model.GroupComments;
import com.inftel.isn.request.CmmentsGroupRequest;
import com.inftel.isn.request.DownloadImageTask;


public class CommentGroupActivity extends Activity {
    private Group group;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment_group);
        listView = (ListView) findViewById(R.id.listview_droup);
        Bundle extras = getIntent().getExtras();

        group = extras.getParcelable("group");

        String admin=group.getAdmin();
        admin=admin.replaceAll("\\.","___");
        new CmmentsGroupRequest(this, admin, group.getName()).execute();
        TextView textView = (TextView) findViewById(R.id.textView_titulo);
        textView.setText(group.getName());
        ImageView imageView = (ImageView) findViewById(R.id.imageView);
        String image =group.getImageUrl();

        new DownloadImageTask(imageView).execute(image);
    }


    public void loadListView (GroupComments comments) {
        CommentGroupsAdapter adapter = new CommentGroupsAdapter(comments.getCommentsList(), CommentGroupActivity.this);
        listView.setAdapter(adapter);
    }

    public void createCommentGroup(View view ){
        Intent intent = new Intent (this, CreateCommentActivity.class);
        intent.putExtra("GRUPO", group);
        startActivity(intent);
    }

}
