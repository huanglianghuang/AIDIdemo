package com.example.client;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.server.Book;
import com.example.server.IMyBookManager;

public class MainActivity extends AppCompatActivity {
    private Button btnConnection;
    private TextView txtConnectionState;
    private EditText etName;
    private EditText etPwd;
    private Button btnLogin;
    private TextView etLoginState;
    private EditText etBookInfo;
    private Button btnQuery;
    private EditText etBookName;

    private IMyBookManager bookManager = null;
    private ServiceConnection conn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            bookManager = IMyBookManager.Stub.asInterface(service);
            txtConnectionState.setText("连接成功！");
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            bookManager = null;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
    }

    private void initViews() {
        btnConnection = findViewById(R.id.btnConnection);
        btnConnection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction("com.example.bservice.bookService");
                intent.setPackage("com.example.server");
                bindService(intent, conn, Context.BIND_AUTO_CREATE);
            }
        });
        txtConnectionState = findViewById(R.id.txtConnectionState);
        etName = findViewById(R.id.etName);
        etPwd = findViewById(R.id.etPwd);
        btnLogin = findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(bookManager == null)
                {
                    Toast.makeText(MainActivity.this, "还没有绑定服务。", Toast.LENGTH_LONG).show();
                    return;
                }
                if(etName.getText().toString().isEmpty() || etPwd.getText().toString().isEmpty()){
                    Toast.makeText(MainActivity.this, "用户名或密码不能为空。", Toast.LENGTH_LONG).show();
                    return;
                }

                try {
                    String resStr = bookManager.login(etName.getText().toString(), etPwd.getText().toString());

                    if(resStr.compareToIgnoreCase("success") == 0){
                        etLoginState.setText("登录成功！");
                    } else{
                        throw new RemoteException("登录失败");
                    }
                } catch (RemoteException e) {
                    e.printStackTrace();
                    etLoginState.setText("登录成功！");
                }
            }
        });
        etLoginState = findViewById(R.id.etLoginState);
        etBookInfo = findViewById(R.id.etBookInfo);
        btnQuery = findViewById(R.id.btnQuery);
        btnQuery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(bookManager == null)
                {
                    Toast.makeText(MainActivity.this, "还没有绑定服务。", Toast.LENGTH_LONG).show();
                    return;
                }
                if(etBookName.getText().toString().isEmpty()){
                    Toast.makeText(MainActivity.this, "请输入您要查找的图书。", Toast.LENGTH_LONG).show();
                    return;
                }
                try {
                    Book book = bookManager.queryByName(etBookName.getText().toString());

                    if(book != null){
                        etBookInfo.setText(book.toString());
                    } else{
                        throw new RemoteException("查询失败");
                    }
                } catch (RemoteException e) {
                    e.printStackTrace();
                    etBookInfo.setText("查询失败");
                }
            }
        });
        etBookName = findViewById(R.id.etBookName);
    }


}