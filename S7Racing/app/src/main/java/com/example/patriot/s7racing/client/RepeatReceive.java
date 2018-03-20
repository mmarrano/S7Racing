package com.example.patriot.s7racing.client;

public class RepeatReceive extends Thread {
    private Client c;
    public RepeatReceive(Client c){
        super();
        this.c = c;
    }

    @Override
    public void run(){
        while(true){
            c.receiveMsg();
        }
    }
}
