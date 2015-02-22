package org.bullshitbankdb.android;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;


public class DetilActivity extends ActionBarActivity {
    String Phone;
    CardView BullshITBtn;
    LinearLayout BullshITBtnBg;
    TextView BullshITCount;
    ListView Comments;
    EditText AddComment;
    String GUID;
    String TAG = "DetailActivity";
    ArrayAdapter<String> CommentsAdapter;
    Context mContext = DetilActivity.this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Phone = getIntent().getStringExtra("phone");
        setContentView(R.layout.activity_detil);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(Phone);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        BullshITBtn = (CardView)findViewById(R.id.bullshitbtn);
        BullshITCount = (TextView)findViewById(R.id.bullshitcount);
        Comments = (ListView)findViewById(R.id.comments);
        AddComment = (EditText)findViewById(R.id.addcomment);
        BullshITBtnBg = (LinearLayout)findViewById(R.id.bullshitbg);

        GuidTool GT = new GuidTool(mContext);
        GUID = GT.getGUID();

        loadDetail();

        BullshITBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isBullshITBankBtnTask();
            }
        });

        AddComment.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId == EditorInfo.IME_ACTION_DONE){
                    addCommentTask();
                }
                return false;
            }
        });

    }


    public void loadDetail(){
        Log.d("TAG","Loading Deail");
        ParseQuery<ParseObject> query = ParseQuery.getQuery("BullshITBankDB");
        query.whereEqualTo("phone", Phone);
        query.getFirstInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject parseObject, ParseException e) {
                Log.d("TAG","Done Loading Detail!");
                List<String> BullshITCountList = parseObject.getList("bullshitcount");
                if(BullshITCountList==null){
                    BullshITCountList = new ArrayList<String>();
                    Log.d("TAG","BullshITCountList is null");
                }
                if(BullshITCountList.contains(GUID)){
                    BullshITBtnBg.setBackgroundColor(Color.RED);
                }else{
                    BullshITBtnBg.setBackgroundColor(Color.parseColor("#ffffb600"));
                }
                try {
                    BullshITCount.setText(String.valueOf(BullshITCountList.size()));
                }catch (Exception ee){
                    BullshITCount.setText(String.valueOf(0));
                }

                Log.d(TAG,"Loading Comments");
                try{
                    List<String> CommentsList = parseObject.getList("comments");
                    if(CommentsList==null){
                        CommentsList = new ArrayList<String>();
                    }
                    CommentsAdapter = new ArrayAdapter<String>(mContext, android.R.layout.simple_list_item_1,CommentsList);
                    Comments.setAdapter(CommentsAdapter);
                }catch (Exception er){
                    Log.d(TAG,"Loading Comments Failed");
                    er.printStackTrace();
                }
                getSupportActionBar().setSubtitle(
                        getResources().getString(R.string.submitter)
                                +parseObject.getString("submitterguid"));
            }
        });
    }

    public void isBullshITBankBtnTask(){
        ParseQuery<ParseObject> query = ParseQuery.getQuery("BullshITBankDB");
        query.whereEqualTo("phone", Phone);
        query.getFirstInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject parseObject, ParseException e) {
                Log.d("TAG","Done Loading Detail!");
                List<String> BullshITCountList = parseObject.getList("bullshitcount");
                if(BullshITCountList==null){
                    BullshITCountList = new ArrayList<String>();
                    Log.d("TAG","BullshITCountList is null");
                }
                if(BullshITCountList.contains(GUID)){
                    BullshITBtnBg.setBackgroundColor(Color.parseColor("#ffffb600"));
                    BullshITCountList.remove(GUID);
                    Log.d("TAG","BullshITCount Minus");
                }else{
                    BullshITBtnBg.setBackgroundColor(Color.RED);
                    BullshITCountList.add(GUID);
                    Log.d("TAG","BullshITCount Plus");
                }
                parseObject.put("bullshitcount",BullshITCountList);
                parseObject.saveInBackground();
                BullshITCount.setText(String.valueOf(BullshITCountList.size()));
            }
        });
    }

    public void addCommentTask(){
        ParseQuery<ParseObject> query = ParseQuery.getQuery("BullshITBankDB");
        query.whereEqualTo("phone", Phone);
        query.getFirstInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject parseObject, ParseException e) {
                Log.d("TAG","Done Loading Detail!");
                String NewComment = AddComment.getText().toString();
                List<String> CommentsList = parseObject.getList("comments");
                if(CommentsList==null){
                    CommentsList = new ArrayList<String>();
                }
                CommentsList.add(NewComment+"["+GUID+"]");
                parseObject.put("comments",CommentsList);
                parseObject.saveInBackground();
                loadDetail();
            }
        });
    }

}
