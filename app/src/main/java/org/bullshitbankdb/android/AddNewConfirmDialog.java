package org.bullshitbankdb.android;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by youngbin on 15. 2. 22.
 */
public class AddNewConfirmDialog extends DialogFragment {

    static AddNewConfirmDialog newInstance(String NewNumber, String GUID) {
        AddNewConfirmDialog f = new AddNewConfirmDialog();

        // Supply num input as an argument.
        Bundle args = new Bundle();
        args.putString("phone", NewNumber);
        args.putString("guid", GUID);
        f.setArguments(args);

        return f;
    }
    
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final String NewNumber = getArguments().getString("phone");
        final String GUID = getArguments().getString("guid");
        
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final LinearLayout Layout = (LinearLayout)inflater.inflate(R.layout.confirm_dialog, null);
        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        

        TextView Phone = (TextView)Layout.findViewById(R.id.phone);
        TextView Guid = (TextView)Layout.findViewById(R.id.guid);
        final EditText AddComment = (EditText)Layout.findViewById(R.id.addcomment);
        Phone.setText(NewNumber);
        Guid.setText(GUID);
        builder.setView(Layout)
                // Add action buttons
                .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        if (AddComment.getText().length() < 1) {
                            Toast.makeText(getActivity(),
                                    getResources().getString(R.string.empty_desc),
                                    Toast.LENGTH_LONG).show();

                        } else {
                            ParseObject bullshITBankDB = new ParseObject("BullshITBankDB");
                            bullshITBankDB.put("phone", NewNumber);
                            bullshITBankDB.put("submitterguid", GUID);
                            bullshITBankDB.saveInBackground(new SaveCallback() {
                                @Override
                                public void done(ParseException e) {
                                    Toast.makeText(getActivity(),
                                            getResources().getString(R.string.saved),
                                            Toast.LENGTH_LONG).show();

                                    addCommentTask(NewNumber,AddComment.getText().toString(),GUID);
                                }
                            });

                        }


                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        AddNewConfirmDialog.this.getDialog().cancel();
                    }
                });

        return builder.create();
    }

    public void addCommentTask(String Number, final String Comment, final String GUID){
        ParseQuery<ParseObject> query = ParseQuery.getQuery("BullshITBankDB");
        query.whereEqualTo("phone", Number);
        query.getFirstInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject parseObject, ParseException e) {
                Log.d("TAG", "Done Loading Detail!");
                List<String> CommentsList = parseObject.getList("comments");
                if(CommentsList==null){
                    CommentsList = new ArrayList<String>();
                }
                CommentsList.add(Comment+"["+GUID+"]");
                parseObject.put("comments",CommentsList);
                parseObject.saveInBackground();
            }
        });
    }
}
