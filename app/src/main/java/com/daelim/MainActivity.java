package com.daelim;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.daelim.data.ListData;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.w3c.dom.Text;

import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    WebSocketClient ws;
    String eid;
    String epwd;
    Boolean eauto;
    private ArrayList<ListData> array;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView nick = findViewById(R.id.nick);
        Button logout = findViewById(R.id.logout);
        Button send = findViewById(R.id.send);
        EditText chat = findViewById(R.id.chat);
        ListView list = findViewById(R.id.list);

        eid = getIntent().getStringExtra("id");
        epwd = getIntent().getStringExtra("pwd");
        eauto = getIntent().getBooleanExtra("auto",true);

        array = new ArrayList<ListData>();


        try{
            ws = new WebSocketClient(new URI("ws://61.83.168.88:4877")) {
                @Override
                public void onOpen(ServerHandshake serverHandshake) {
                    Log.e("!!!","onOpen");
                    ws.send("CHAT|" + eid + "|" + epwd);

//
//                    ws.send("LOGOUT|" + eid);
                    if(eid != null){
                        nick.setText(eid + "님 환영합니다.");
                    }


                }

                @Override
                public void onMessage(String s) {

                    Log.e("!!!","onMessage s : "+s);
                    String[] strs = s.split("\\|");


                    send.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            ws.send("CHAT|" + eid + "|" + chat.getText());
                            array.add(new ListData(chat.getText().toString()));
                            list.setAdapter(new BaseAdapter() {
                                @Override
                                public int getCount() {
                                    return array.size();
                                }

                                @Override
                                public Object getItem(int i) {
                                    return null;
                                }

                                @Override
                                public long getItemId(int i) {
                                    return 0;
                                }

                                @Override
                                public View getView(int i, View view, ViewGroup viewGroup) {
                                    view = getLayoutInflater().inflate(R.layout.list_custom_item,viewGroup,false);
                                    TextView my = view.findViewById(R.id.chatt);
                                    my.setText(array.get(i).getChat());
                                    notifyDataSetChanged();
                                    chat.setText("");
                                    return view;
                                }
                            });
                            list.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);

                        }
                    });

                }

                @Override
                public void onClose(int i, String s, boolean b) {
                    Log.e("!!!","onClose" + s);
                }

                @Override
                public void onError(Exception e) {
                    Log.e("!!!","onError");
                    e.printStackTrace();
                }
            };
            ws.connect();

        } catch(Exception e){
            e.printStackTrace();
        }

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Boolean nauto = false;
                ws.close();
                Intent i = new Intent(MainActivity.this,LoginActivity.class);
                i.putExtra("eauto",nauto);
                startActivity(i);
            }
        });


    }
}