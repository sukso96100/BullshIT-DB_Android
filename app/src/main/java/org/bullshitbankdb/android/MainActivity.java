package org.bullshitbankdb.android;

import android.content.Context;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends ActionBarActivity {

    String TAG = "MainActivity";
    ListView listview;
    ArrayAdapter<String> ResultsAdapter;
    Context mContext = MainActivity.this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final EditText SearchInput = (EditText)findViewById(R.id.search);
        EditText AddNewInput = (EditText)findViewById(R.id.addnew);
        listview = (ListView)findViewById(R.id.listView);

        SearchInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId == EditorInfo.IME_ACTION_DONE){
                    Log.d(TAG,"Searching BullshIT...");
                    searchBullshITBank(SearchInput.getText().toString());
                }
                return false;
            }
        });
    }

    public void searchBullshITBank(String Number){
        ParseQuery<ParseObject> query = ParseQuery.getQuery("BullshITBankDB");
        query.whereStartsWith("phone", Number);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> parseObjects, ParseException e) {
                Log.d(TAG,"Done Searching!");
                Log.d(TAG,parseObjects.toString());
                ArrayList<String> Array = new ArrayList<String>();
                for(int i=0; i<parseObjects.size(); i++){
                    String Item = parseObjects.get(i).getString("phone");
                    Log.d(TAG, Item);
                    Array.add(Item);
                    ResultsAdapter = new ArrayAdapter<String>(mContext, android.R.layout.simple_list_item_1, Array);
                    listview.setAdapter(ResultsAdapter);
                }
            }
        });
    }


//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_main, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }
}
