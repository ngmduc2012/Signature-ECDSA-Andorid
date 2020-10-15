package com.example.signature.Fragments.Dialogs;


import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.hardware.biometrics.BiometricManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Vibrator;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;

import com.example.signature.Activities.Login;
import com.example.signature.Modle.SharePref;
import com.example.signature.Modle.UtilsApplication;
import com.example.signature.R;

import java.util.Objects;
import java.util.concurrent.Executor;

import static com.example.signature.Fragments.Generation_Keys.setView;


public class DialogCheckPassword extends AppCompatDialogFragment {
    Button btn_Check;
    ImageView iv_d_checkPass, iv_d_checkpass_notify, iv_d_checkpass_eye;
    TextView tv_d_checkPass, tv_d_checkPass_notify;
    int ShowPassDialogCheck = 1, countDialog = 5;
    Vibrator vibrator;
    private EditText et_d_checkPass;

    @SuppressLint({"SwitchIntDef", "SetTextI18n"})
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = Objects.requireNonNull(getActivity()).getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_dialog_check_password, null);
        //init
        builder.setView(view).create();
        vibrator = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);
        iv_d_checkpass_eye = view.findViewById(R.id.iv_d_checkpass_eye);
        iv_d_checkpass_notify = view.findViewById(R.id.iv_d_checkpass_notify);
        iv_d_checkPass = view.findViewById(R.id.iv_d_checkPass);
        btn_Check = view.findViewById(R.id.btn_Check);
        et_d_checkPass = view.findViewById(R.id.et_d_checkPass);
        tv_d_checkPass = view.findViewById(R.id.tv_d_checkPass);
        tv_d_checkPass_notify = view.findViewById(R.id.tv_d_checkPass_notify);
        // check count Input Wrong Password
        if (SharePref.SellectCountLoginInputPassword() != 4
                && SharePref.SellectCountLoginInputPassword() != 3
                && SharePref.SellectCountLoginInputPassword() != 2
                && SharePref.SellectCountLoginInputPassword() != 1
                && SharePref.SellectCountLoginInputPassword() != 0) {
            SharePref.countLoginInputPassword(5);
        } else {
            countDialog = SharePref.SellectCountLoginInputPassword();
            tv_d_checkPass_notify.setText(countDialog + " times left");
            if(countDialog == 1)
            {
                tv_d_checkPass_notify.setText(countDialog + " time left");
            }
        }
        // on click check pass
        btn_Check.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                CheckPass();
            }
        });
        // finger print
        androidx.biometric.BiometricManager biometricManager = androidx.biometric.BiometricManager.from(getActivity());
        switch (biometricManager.canAuthenticate()) {
            case BiometricManager.BIOMETRIC_SUCCESS:
                tv_d_checkPass.setText("You can use the fingerPrint sensor");
                break;
            case BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE:
                tv_d_checkPass.setText("The biometric sensor is current unavailable");
                iv_d_checkPass.setVisibility(View.GONE);
                break;
            case BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED:
                tv_d_checkPass.setText("Your phone doesn't have any fingerprint saved, please check your security settings");
                iv_d_checkPass.setVisibility(View.GONE);
                break;
            case BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE:
                tv_d_checkPass.setText("Your phone does not have a fingerprint sensor");
                break;
            case BiometricManager.BIOMETRIC_ERROR_SECURITY_UPDATE_REQUIRED:
                break;
        }
        Executor executor = ContextCompat.getMainExecutor(Objects.requireNonNull(getContext()));
        final androidx.biometric.BiometricPrompt biometricPrompt = new androidx.biometric.BiometricPrompt
                (getActivity(), executor
                        , new BiometricPrompt.AuthenticationCallback() {
                    @Override
                    public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                        super.onAuthenticationError(errorCode, errString);
                    }

                    @Override
                    public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                        super.onAuthenticationSucceeded(result);
                        deletePrivateKey_Dialog();
                        dismiss();
                    }

                    @Override
                    public void onAuthenticationFailed() {
                        super.onAuthenticationFailed();
                    }
                });
        final androidx.biometric.BiometricPrompt.PromptInfo promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle("Check Password")
                .setDescription("User your fingerprint to check password")
                .setNegativeButtonText("Cancel")
                .build();

        iv_d_checkPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (SharePref.AskUseFinger()) {
                    biometricPrompt.authenticate(promptInfo);
                } else {
                    if (vibrator.hasVibrator()) {
                        vibrator.vibrate(432); // for 432 ms
                    }
                    Toast.makeText(getActivity(), "You have not set use Fingerprint yet!", Toast.LENGTH_SHORT).show();
                }

            }
        });
        // on click eye
        iv_d_checkpass_eye.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setViewDialogCheckPassword();
            }
        });
        return builder.create();
    }

    // show and hind password
    public void setViewDialogCheckPassword() {
        //set show hint password
        if (ShowPassDialogCheck == 0) {
            iv_d_checkpass_eye.setImageResource(R.drawable.ic_hind_password);
            et_d_checkPass.setTransformationMethod(new PasswordTransformationMethod());
            ShowPassDialogCheck = 1;
        } else {
            iv_d_checkpass_eye.setImageResource(R.drawable.ic_show_password);
            et_d_checkPass.setTransformationMethod(null);
            ShowPassDialogCheck = 0;
        }
    }

    //Check password to delete Private key file
    @SuppressLint("SetTextI18n")
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void CheckPass() {
        if (et_d_checkPass.getText().toString().isEmpty()) {
            if (vibrator.hasVibrator()) {
                vibrator.vibrate(432); // for 432 ms
            }
            Toast.makeText(getActivity(), "Fulfil!", Toast.LENGTH_SHORT).show();
        } else {
            if (SharePref.CheckLogin(et_d_checkPass.getText().toString())) {
                deletePrivateKey_Dialog();
                dismiss();
            } else {
                iv_d_checkpass_notify.setImageResource(R.drawable.ic_not_verify);
                if (vibrator.hasVibrator()) {
                    vibrator.vibrate(432); // for 432 ms
                }
                countDialog -= 1;
                SharePref.countLoginInputPassword(countDialog);
                tv_d_checkPass_notify.setText(countDialog + " times left");
                if(countDialog == 1)
                {
                    tv_d_checkPass_notify.setText(countDialog + " time left");
                }
                if (countDialog == 0) {
                    SharePref.countLogin(60000);
                    startActivity(new Intent(getActivity(), Login.class));
                    Objects.requireNonNull(getActivity()).finish();
                    dismiss();
                }
            }
        }


    }

    // delete private key file and path private key and public key
    public void deletePrivateKey_Dialog() {
        SharePref.countLoginInputPassword(5);
        UtilsApplication.deleteFile(SharePref.PathPri());
        SharePref.SaveKey(null, null);
        setView();
    }
}
