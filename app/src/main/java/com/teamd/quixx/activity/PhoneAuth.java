package com.teamd.quixx.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.teamd.quixx.MainActivity;
import com.teamd.quixx.R;
import com.teamd.quixx.domain.SimpleApiResponse;
import com.teamd.quixx.retrofit.Api;
import com.teamd.quixx.retrofit.RetrofitInstance;
import com.teamd.quixx.utils.Commons;
import com.teamd.quixx.utils.Constants;
import com.teamd.quixx.utils.Message;

import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PhoneAuth extends AppCompatActivity {

    private Api api = null;
    private String deliveryManPhoneNumber = null;

    private String preFix = "+88";
    private FirebaseAuth firebaseAuth;
    private EditText vCode;
    private Button okButton;
    private String code = "";
    private String idSentByFirebase = "";
    private ProgressBar progressBar;
    private TextView waitText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_auth);
        vCode = findViewById(R.id.vCode);
        okButton = findViewById(R.id.vCodeOkButton);
        progressBar = findViewById(R.id.vCodeProgressBar);
        waitText=findViewById(R.id.waittext);
        api= RetrofitInstance.getRetrofitInstance().create(Api.class);//?
        deliveryManPhoneNumber = getIntent().getStringExtra(Constants.DELIVERY_MAN_PHONE_NUMBER);//?
        okButton.setBackground(getResources().getDrawable(R.drawable.button_round));

        //quixxServerAuth();

        //for zaoad
        //getVerificationCone();

        /*firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuthVerification(preFix+deliveryManPhoneNumber);*/

        okButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                waitText.setVisibility(View.VISIBLE);
                code=vCode.getText().toString();
                if(code == null || code.isEmpty()){
                    Commons.showToast(getApplicationContext(), "code is not valid");
                }

                /*else if(idSentByFirebase == null || "".equals(idSentByFirebase)){
                    Commons.showToast(getApplicationContext(), "id sent by firebase is null or empty");
                }*/
                else{
                    verifyAuthCode();
                }
                //progressBar.setVisibility(View.INVISIBLE);
                //waitText.setVisibility(View.INVISIBLE);
            }
        });

    }

    private void verifyAuthCode() {
        Call<SimpleApiResponse> call=api.verifyAuthCode(code, deliveryManPhoneNumber);
        call.enqueue(new Callback<SimpleApiResponse>() {

            @Override
            public void onResponse(Call<SimpleApiResponse> call, Response<SimpleApiResponse> response) {
                if(response.isSuccessful()){
                    SimpleApiResponse simpleApiResponse = response.body();
                    if(simpleApiResponse.getData().equals("true")){
                        Intent intent = new Intent(getApplicationContext(), UserProfile.class);
                        intent.putExtra(Constants.DELIVERY_MAN_PHONE_NUMBER, deliveryManPhoneNumber);
                        startActivity(intent);
                        finish();
                        progressBar.setVisibility(View.INVISIBLE);
                        waitText.setVisibility(View.INVISIBLE);
                        //Commons.showToast(getApplicationContext(), "Done");
                    }else{
                        Commons.showToast(getApplicationContext(), "code is not valid");
                        progressBar.setVisibility(View.INVISIBLE);
                        waitText.setVisibility(View.INVISIBLE);

                    }
                }
                else
                {

                    Commons.showToast(getApplicationContext(), "code is not valid");
                    progressBar.setVisibility(View.INVISIBLE);
                    waitText.setVisibility(View.INVISIBLE);
                }

            }

            @Override
            public void onFailure(Call<SimpleApiResponse> call, Throwable t) {
                Commons.showToast(getApplicationContext(), t.getMessage());

            }
        });
    }

    /*private void phoneAuthMenual(){
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(idSentByFirebase, code);
        signInWithPhoneAuthCredential(credential);
    }

    private void firebaseAuthVerification(String pNumber){
        PhoneAuthProvider.getInstance().verifyPhoneNumber(pNumber, 60, TimeUnit.SECONDS,this, callBack);
    }

    PhoneAuthProvider.OnVerificationStateChangedCallbacks callBack =
            new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

                @Override
                public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                    progressBar.setVisibility(View.GONE);
                    String code = phoneAuthCredential.getSmsCode();
                    vCode.setText(code);
                    if(code != null && !code.isEmpty()){
                        signInWithPhoneAuthCredential(phoneAuthCredential);
                    }
                }

                @Override
                public void onVerificationFailed(FirebaseException e) {
                    Commons.showToast(getApplicationContext(), e.getMessage());
                }

                @Override
                public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                    super.onCodeSent(s, forceResendingToken);
                    idSentByFirebase = s;
                }
            };


    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            quixxServerAuth();
                        } else {
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                Commons.showToast(getApplicationContext(), task.getException().getMessage());
                            }
                        }
                    }
                });
    }

    public void quixxServerAuth(){
            Call<SimpleApiResponse> call=api.deliveryManIsApproved(deliveryManPhoneNumber);
            call.enqueue(new Callback<SimpleApiResponse>() {

                @Override
                public void onResponse(Call<SimpleApiResponse> call, Response<SimpleApiResponse> response) {
                    if(response.isSuccessful()){
                        SimpleApiResponse simpleApiResponse = response.body();
                        if(simpleApiResponse.getData().equals("true")){
                            //get office time api
                            Intent intent = new Intent(getApplicationContext(), UserProfile.class);
                            intent.putExtra(Constants.DELIVERY_MAN_PHONE_NUMBER, deliveryManPhoneNumber);
                            startActivity(intent);
                            finish();
                        }else{
                            Commons.showToast(getApplicationContext(), Message.PHONE_NUMBER_IS_NOT_VALID);
                        }
                    }
                }

                @Override
                public void onFailure(Call<SimpleApiResponse> call, Throwable t) {
                    Commons.showToast(getApplicationContext(), t.getMessage());
                }
            });
    }*/


}
