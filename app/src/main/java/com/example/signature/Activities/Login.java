package com.example.signature.Activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.hardware.biometrics.BiometricManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;

import com.example.signature.Modle.SharePref;
import com.example.signature.R;

import java.util.concurrent.Executor;

public class Login extends AppCompatActivity {
    EditText et_pass_login;
    TextView tv_notify, tv_l_count;
    Button btn_login;
    ImageView iv_finger, iv_login_eye, iv_l_notify;
    int checkShowPassLogin = 1, count = 5;

    @SuppressLint({"SwitchIntDef", "SetTextI18n"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        checkSharePreLogin();
        checkCountLogin();
        init();
        // finger print
        androidx.biometric.BiometricManager biometricManager = androidx.biometric.BiometricManager.from(this);
        switch (biometricManager.canAuthenticate()) {
            case BiometricManager.BIOMETRIC_SUCCESS:
                tv_notify.setText("You can use the fingerPrint sensor to login");
                tv_notify.setTextColor(Color.parseColor("#Fafafa"));
                break;
            case BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE:
                tv_notify.setText("The biometric sensor is current unavailable");
                iv_finger.setVisibility(View.GONE);
                break;
            case BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED:
                tv_notify.setText("Your phone doesn't have any fingerprint saved, please check your security settings");
                iv_finger.setVisibility(View.GONE);
                break;
            case BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE:
                tv_notify.setText("Your phone does not have a fingerprint sensor");
                break;
            case BiometricManager.BIOMETRIC_ERROR_SECURITY_UPDATE_REQUIRED:
                break;
        }
        Executor executor = ContextCompat.getMainExecutor(this);
        final androidx.biometric.BiometricPrompt biometricPrompt = new androidx.biometric.BiometricPrompt
                (Login.this, executor
                        , new BiometricPrompt.AuthenticationCallback() {
                    @Override
                    public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                        super.onAuthenticationError(errorCode, errString);
                    }

                    @Override
                    public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                        super.onAuthenticationSucceeded(result);
                        login();
                    }

                    @Override
                    public void onAuthenticationFailed() {
                        super.onAuthenticationFailed();
                    }
                });
        final androidx.biometric.BiometricPrompt.PromptInfo promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle("Login")
                .setDescription("User your fingerprint to login")
                .setNegativeButtonText("Cancel")
                .build();

        iv_finger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (SharePref.AskUseFinger()) {
                    biometricPrompt.authenticate(promptInfo);
                } else {
                    Toast.makeText(Login.this, "You have set use Fingerprint yet!", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    // set view login
    public void setViewLogin(View view) {
        if (checkShowPassLogin == 0) {
            iv_login_eye.setImageResource(R.drawable.ic_hind_password);
            et_pass_login.setTransformationMethod(new PasswordTransformationMethod());
            checkShowPassLogin = 1;
        } else {
            iv_login_eye.setImageResource(R.drawable.ic_show_password);
            et_pass_login.setTransformationMethod(null);
            checkShowPassLogin = 0;
        }
    }

    // check the first time to enter app
    public void checkSharePreLogin() {
        if (!SharePref.CheckPassExsit()) {
            startActivity(new Intent(Login.this, MainActivity.class));
            Login.this.finish();
        }
    }

    public void init() {
        iv_l_notify = findViewById(R.id.iv_l_notify);
        et_pass_login = findViewById(R.id.et_pass_login);
        iv_login_eye = findViewById(R.id.iv_login_eye);
        tv_notify = findViewById(R.id.tv_notify);
        btn_login = findViewById(R.id.btn_login);
        iv_finger = findViewById(R.id.iv_finger);
        tv_l_count = findViewById(R.id.tv_l_count);
    }

    // check password
    public void check(View view) {
        if (et_pass_login.getText().toString().isEmpty()) {
            Toast.makeText(this, "Fulfil!", Toast.LENGTH_SHORT).show();
        } else {
            if (SharePref.CheckLogin(et_pass_login.getText().toString())) {
                login();
            } else {
                iv_l_notify.setImageResource(R.drawable.ic_not_verify);
                count -= 1;
                if (count == 0) {
                    countLogin(60000);
                }

            }
        }
    }

    // check time login
    public void checkCountLogin() {
        if (SharePref.SellectCountLogin() != 0) {
            countLogin(SharePref.SellectCountLogin());
        }

    }

    // count time login
    public void countLogin(long countTime) {
        new CountDownTimer(countTime, 1000) {

            @SuppressLint("SetTextI18n")
            public void onTick(long millisUntilFinished) {
                tv_l_count.setText("seconds remaining: " + millisUntilFinished / 1000 + " s");
                SharePref.countLogin(millisUntilFinished);
                btn_login.setEnabled(false);
                iv_finger.setEnabled(false);
            }

            public void onFinish() {
                tv_l_count.setText(null);
                btn_login.setEnabled(true);
                iv_finger.setEnabled(true);
                SharePref.countLogin(0);
                count = 5;
            }

        }.start();
    }

    // login
    public void login() {
        startActivity(new Intent(Login.this, MainActivity.class));
        Login.this.finish();
    }
}