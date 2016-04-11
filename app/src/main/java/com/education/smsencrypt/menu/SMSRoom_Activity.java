package com.education.smsencrypt.menu;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import com.education.smsencrypt.R;
import com.education.smsencrypt.adapter.RoomSMSAdapter;
import com.education.smsencrypt.database.database_adapter.TableSendRecieveAdapter;
import com.education.smsencrypt.utils.listItemObject.ListItemRecieveSMS;

import java.util.List;

/**
 * Created by Eno on 3/28/2016.
 */
public class SMSRoom_Activity extends AppCompatActivity {
    private RecyclerView mRecyclerSurvey;
    private EditText inputSMS;
    private ImageButton send;
    private List<ListItemRecieveSMS> listItems;
    private RoomSMSAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_room);
        mRecyclerSurvey = (RecyclerView) findViewById(R.id.recycleSMS);
        inputSMS = (EditText) findViewById(R.id.inputSMS);
        send = (ImageButton) findViewById(R.id.sendSMS);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TableSendRecieveAdapter tbl = new TableSendRecieveAdapter(SMSRoom_Activity.this).open();
                tbl.insert("", inputSMS.getText().toString(), "", "", "1");
                final LinearLayoutManager layoutParams = new LinearLayoutManager(SMSRoom_Activity.this);
                layoutParams.setStackFromEnd(true);
                mRecyclerSurvey.setLayoutManager(layoutParams);
                listItems = tbl.fetchall(SMSRoom_Activity.this);
                adapter = new RoomSMSAdapter(SMSRoom_Activity.this, listItems);
                mRecyclerSurvey.setAdapter(adapter);
                tbl.close();
            }
        });
        ShowHistorySMS();
    }


    private void ShowHistorySMS() {
        TableSendRecieveAdapter tbl = new TableSendRecieveAdapter(SMSRoom_Activity.this);
        final LinearLayoutManager layoutParams = new LinearLayoutManager(SMSRoom_Activity.this);
        layoutParams.setStackFromEnd(true);
        mRecyclerSurvey.setLayoutManager(layoutParams);
        listItems = tbl.fetchall(SMSRoom_Activity.this);
        adapter = new RoomSMSAdapter(SMSRoom_Activity.this, listItems);
        mRecyclerSurvey.setAdapter(adapter);

    }
}
