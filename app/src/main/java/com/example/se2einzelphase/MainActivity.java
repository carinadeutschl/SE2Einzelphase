package com.example.se2einzelphase;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.BundleCompat;
import androidx.lifecycle.Observer;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.Socket;

import java.util.Arrays;


import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;


public class MainActivity extends AppCompatActivity implements Observer<String> { {

    EditText txtStudentid;
    TextView txtAnswer;
    Button btnSend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        txtStudentid = findViewById(R.id.txtStudentid);
        txtAnswer = findViewById(R.id.txtAnswer);
        btnSend = findViewById(R.id.btnSend);
    }

    public void sendToServer(View view){
        String studendID = txtStudentid.getText().toString();

        getServerAnswerNetworkCall(studendID)
                .subscribeOn(Schedulers.io()) // wait in the background on io thread
                .observeOn(AndroidSchedulers.mainThread()) //receive updates on ui thread
                .subscribe(this); //recieve updates in this class

    }

    public void sortStudentId(View view){
        String id = txtStudentid.getText().toString();
        StringBuilder even = new StringBuilder();
        StringBuilder odd = new StringBuilder();

        char [] studentId = id.toCharArray();

        Arrays.sort(studentId);

        for (char c : studentId){
            if (Character.getNumericValue(c) % 2 == 0) {
                even.append(c);
            }else{
                odd.append(c);
            }
        }

        txtAnswer.setText(String.format("%s%s", even.toString(), odd.toString()));
    }
    public Observable <String> getServerAnswerNetworkCall(String message) {
        return Observable.create(emitter -> {
            Socket socket = new Socket(InetAddress.getByName("se2-isys.aau.at"), 53212);

            DataOutputStream writer = new DataOutputStream(socket.getOutputStream());
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            System.out.print("Sending message ");
            System.out.println(message);

            //send message
            writer.writeBytes(message + '\n');
            writer.flush();

            //wait for answer
            System.out.println("Reading ... ");
            String answer = reader.readLine();
            System.out.println("Answer");
            System.out.println(answer);
            emitter.onNext(answer);

            //clean up
            writer.close();
            reader.close();
            socket.close();

        });
    }

    @Override
public void onSubscribe(@NonNull Disposable d){
    }
    @Override
    public void OnNext(@NonNull String s){
        System.out.print("Receiving ... ");
        System.out.println(s);
        txtAnswer.setText(s);
    }
    @Override
    public void onError(@NonNull Throwable e){
        System.out.print("Error ... ");
        System.out.println(e.getMessage());
        txtAnswer.setText(e.getMessage());
    }

    @Override
    public void onComplete(){

    }
}}