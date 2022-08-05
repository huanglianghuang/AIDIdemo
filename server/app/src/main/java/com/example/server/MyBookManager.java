package com.example.server;

import android.os.RemoteException;

public class MyBookManager extends IMyBookManager.Stub{
    @Override
    public void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat, double aDouble, String aString) throws RemoteException {

    }

    @Override
    public String login(String userName, String pwd) throws RemoteException {
        if(userName.equalsIgnoreCase("abcd") && pwd.equalsIgnoreCase("123")){
            return "success";
        }
        return "error";
    }

    @Override
    public Book queryByName(String bookName) throws RemoteException {
        Book book = new Book();
        book.setName(bookName);
        book.setPrice(100);
        return book;
    }
}
