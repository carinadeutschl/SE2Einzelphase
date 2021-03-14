package com.example.se2einzelphase;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Observable;


public class MainActivity extends AppCompatActivity {

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
    public android.database.Observable <String> getServerAnswerNetworkCall(String message) {
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
}