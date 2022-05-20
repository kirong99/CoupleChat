package com.daelim;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;

public class MainActivity extends AppCompatActivity {
    WebSocketClient ws;
    String eid;
    String epwd;
    Boolean eauto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView nick = findViewById(R.id.nick);
        Button logout = findViewById(R.id.logout);

        eid = getIntent().getStringExtra("id");
        epwd = getIntent().getStringExtra("pwd");
        eauto = getIntent().getBooleanExtra("auto",true);

        try{
            ws = new WebSocketClient(new URI("ws://61.83.168.88:4877")) {
                @Override
                public void onOpen(ServerHandshake serverHandshake) {
                    Log.e("!!!","onOpen");
                    ws.send("LOGIN | " + eid + "| " + epwd); //로그인 할때, 로그인 이후에 다른사람과 소통을 할 때
                    if(eid != null){
                        nick.setText(eid + "님 환영합니다.");
                    }
                }

                @Override
                public void onMessage(String s) {

                    Log.e("!!!","onMessage s : "+s);
                    String[] strs = s.split("\\|");

                }

                @Override
                public void onClose(int i, String s, boolean b) {
                    Log.e("!!!","onClose");
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
                Intent i = new Intent(MainActivity.this,LoginActivity.class);
                i.putExtra("eauto",nauto);
                startActivity(i);
            }
        });

    }
}