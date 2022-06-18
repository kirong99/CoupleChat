package com.daelim;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;

public class LoginActivity extends AppCompatActivity {
    WebSocketClient ws;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Button login = findViewById(R.id.login);
        EditText id = findViewById(R.id.id);
        EditText pwd = findViewById(R.id.pwd);
        CheckBox auto = findViewById(R.id.auto);


        Boolean nauto = getIntent().getBooleanExtra("auto",false);

        SharedPreferences sp = getSharedPreferences("file",MODE_PRIVATE);
        SharedPreferences.Editor ed = sp.edit();

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String eid;
                String epwd;
                Boolean eauto;
                ed.putString("id",id.getText().toString());
                ed.putString("pwd",pwd.getText().toString());
                ed.putBoolean("auto",auto.isChecked());
                ed.commit();
                eid = sp.getString("id","");
                epwd = sp.getString("pwd","");
                eauto = sp.getBoolean("auto",true);
                Intent i = new Intent(LoginActivity.this,MainActivity.class);
                i.putExtra("id",eid);
                i.putExtra("pwd",epwd);
                i.putExtra("auto",eauto);
                startActivity(i);
            }
        });
        if(sp.getBoolean("auto",true)){
            ed.commit();
            id.setText(sp.getString("id",""));
            pwd.setText(sp.getString("pwd",""));
            auto.setChecked(true);
        } else{
            id.setText("");
            pwd.setText("");
        }

//        if(nauto == false){
//            auto.setChecked(false);
//            id.setText("");
//            pwd.setText("");
//        } else{
//            ed.commit();
//            id.setText(sp.getString("id",""));
//            pwd.setText(sp.getString("pwd",""));
//            auto.setChecked(true);
//        }
    }
}