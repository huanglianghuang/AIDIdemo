package com.example.server;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import com.example.server.IMyBookManager;
public class bService extends Service {
    MyBookManager bookManager = new MyBookManager();
    public bService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return bookManager;
    }
}