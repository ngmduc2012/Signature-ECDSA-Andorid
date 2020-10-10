package com.example.signature.Fragments.Dialogs;


import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.hardware.biometrics.BiometricManager;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.biometric.BiometricPrompt;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import com.example.signature.AES.AESpassword;
import com.example.signature.Activities.Login;
import com.example.signature.ECDSA.PrivateKey;
import com.example.signature.ECDSA.PublicKey;
import com.example.signature.Fragments.Generation_Keys;
import com.example.signature.Modle.PasswordStrength;
import com.example.signature.Modle.SharePref;
import com.example.signature.R;

import java.util.Objects;
import java.util.concurrent.Executor;


public class DialogPassword extends AppCompatDialogFragment {
    Button btn_Enter;
    ImageView iv_d_p_password, iv_d_p_new_password, iv_d_p_retype, iv_d_p_notify_retype, iv_d_p_notify_password;
    TextView tv_dialog, tv_d_pass_notify, tv_d_forget;
    @SuppressLint("UseSwitchCompatOrMaterialCode")
    Switch sw_d_finger;
    ConstraintLayout ct_d_pass, cc_d_p_finger;
    int ShowPassDialog = 1, ShowNewPassDialog = 1, ShowRetypeNewPassDialog = 1, countDialog = 5;
    private EditText et_Password, et_New_Password, et_Retype_New_Password;

    @SuppressLint({"SetTextI18n", "SwitchIntDef"})
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = Objects.requireNonNull(getActivity()).getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_dialog_password, null);
        // init
        builder.setView(view).create();
        tv_d_forget = view.findViewById(R.id.tv_d_forget);
        cc_d_p_finger = view.findViewById(R.id.cc_d_p_finger);
        ct_d_pass = view.findViewById(R.id.ct_d_pass);
        iv_d_p_notify_password = view.findViewById(R.id.iv_d_p_notify_password);
        iv_d_p_notify_retype = view.findViewById(R.id.iv_d_p_notify_retype);
        iv_d_p_retype = view.findViewById(R.id.iv_d_p_retype);
        iv_d_p_password = view.findViewById(R.id.iv_d_p_password);
        iv_d_p_new_password = view.findViewById(R.id.iv_d_p_new_password);
        tv_dialog = view.findViewById(R.id.tv_dialog);
        tv_d_pass_notify = view.findViewById(R.id.tv_d_pass_notify);
        btn_Enter = view.findViewById(R.id.btn_Enter);
        sw_d_finger = view.findViewById(R.id.sw_d_finger);
        et_New_Password = view.findViewById(R.id.et_New_Password);
        et_Retype_New_Password = view.findViewById(R.id.et_Retype_New_Password);
        et_Password = view.findViewById(R.id.et_Password);
        // check when the first time enter app
        if (!SharePref.CheckPassExsit()) {
            tv_dialog.setText("  Create Password");
            ct_d_pass.setVisibility(View.GONE);
            tv_d_forget.setVisibility(View.GONE);
        }
        // check count Input Wrong Password
        if (SharePref.SellectCountLoginInputPassword() != 4
                && SharePref.SellectCountLoginInputPassword() != 3
                && SharePref.SellectCountLoginInputPassword() != 2
                && SharePref.SellectCountLoginInputPassword() != 1
                && SharePref.SellectCountLoginInputPassword() != 0) {
            SharePref.countLoginInputPassword(5);
        } else {
            countDialog = SharePref.SellectCountLoginInputPassword();
            tv_d_pass_notify.setText(countDialog + " times left");
            tv_d_pass_notify.setTextColor(Objects.requireNonNull(getContext()).getResources().getColor(R.color.colorPrimaryDark));
        }
        //on click
        btn_Enter.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                AESpass();
            }
        });
        sw_d_finger.setChecked(SharePref.AskUseFinger());
        sw_d_finger.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                SharePref.SetUseFinger(b);
                if (!b)
                    Toast.makeText(getActivity(), "You need finger print when forgetting password!", Toast.LENGTH_SHORT).show();
            }
        });
        // check new password and retype new password are the same
        et_Retype_New_Password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (et_New_Password.getText().toString().equals(et_Retype_New_Password.getText().toString())
                        && !et_New_Password.getText().toString().isEmpty()
                        && !et_Retype_New_Password.getText().toString().isEmpty()) {
                    iv_d_p_notify_retype.setImageResource(R.drawable.ic_verify_checking);
                } else {
                    iv_d_p_notify_retype.setImageResource(R.drawable.ic_wrong_file);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        // check: is that strong password
        et_New_Password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().isEmpty()) {
                    // check count Input Wrong Password
                    if (SharePref.SellectCountLoginInputPassword() != 4
                            && SharePref.SellectCountLoginInputPassword() != 3
                            && SharePref.SellectCountLoginInputPassword() != 2
                            && SharePref.SellectCountLoginInputPassword() != 1
                            && SharePref.SellectCountLoginInputPassword() != 0) {
                        tv_d_pass_notify.setText(null);
                    } else {
                        tv_d_pass_notify.setText(countDialog + " times left");
                        tv_d_pass_notify.setTextColor(Objects.requireNonNull(getContext()).getResources().getColor(R.color.colorPrimaryDark));
                    }
                } else {
                    calculatePasswordStrength(s.toString());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        iv_d_p_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setViewDialogPassword1();
            }
        });
        iv_d_p_new_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setViewDialogPassword2();
            }
        });
        iv_d_p_retype.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setViewDialogPassword3();
            }
        });
        androidx.biometric.BiometricManager biometricManager = androidx.biometric.BiometricManager.from(getActivity());
        switch (biometricManager.canAuthenticate()) {
            case BiometricManager.BIOMETRIC_SUCCESS:
                break;
            case BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE:
            case BiometricManager.BIOMETRIC_ERROR_SECURITY_UPDATE_REQUIRED:
            case BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE:
            case BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED:
                cc_d_p_finger.setVisibility(View.GONE);
                tv_d_forget.setVisibility(View.GONE);
                break;
        }
        // finger print
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
                        et_Password.setText(SharePref.SellectAESPass());
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

        tv_d_forget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                biometricPrompt.authenticate(promptInfo);

            }
        });
        return builder.create();
    }

    // show and hind password
    public void setViewDialogPassword1() {
        //set show hint password
        if (ShowPassDialog == 0) {
            iv_d_p_password.setImageResource(R.drawable.ic_hind_password);
            et_Password.setTransformationMethod(new PasswordTransformationMethod());
            ShowPassDialog = 1;
        } else {
            iv_d_p_password.setImageResource(R.drawable.ic_show_password);
            et_Password.setTransformationMethod(null);
            ShowPassDialog = 0;
        }
    }

    // set view for new password edit text
    public void setViewDialogPassword2() {
        //set show hint new password
        if (ShowNewPassDialog == 0) {
            iv_d_p_new_password.setImageResource(R.drawable.ic_hind_password);
            et_New_Password.setTransformationMethod(new PasswordTransformationMethod());
            ShowNewPassDialog = 1;
        } else {
            iv_d_p_new_password.setImageResource(R.drawable.ic_show_password);
            et_New_Password.setTransformationMethod(null);
            ShowNewPassDialog = 0;
        }
    }

    // set view for retype new password edit text
    public void setViewDialogPassword3() {
        // set show hint retype new password
        if (ShowRetypeNewPassDialog == 0) {
            iv_d_p_retype.setImageResource(R.drawable.ic_hind_password);
            et_Retype_New_Password.setTransformationMethod(new PasswordTransformationMethod());
            ShowRetypeNewPassDialog = 1;
        } else {
            iv_d_p_retype.setImageResource(R.drawable.ic_show_password);
            et_Retype_New_Password.setTransformationMethod(null);
            ShowRetypeNewPassDialog = 0;
        }
    }

    // check strong password
    private void calculatePasswordStrength(String str) {
        // Now, we need to define a PasswordStrength enum
        // with a calculate static method returning the password strength
        PasswordStrength passwordStrength = PasswordStrength.calculate(str);
        tv_d_pass_notify.setText(passwordStrength.msg);
        tv_d_pass_notify.setTextColor(passwordStrength.color);
        et_New_Password.setTextColor(passwordStrength.color);
        et_Retype_New_Password.setTextColor(passwordStrength.color);
    }

    //Save new pass AES
    @SuppressLint("SetTextI18n")
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void AESpass() {
        //When pass AES is exist!
        if (SharePref.CheckPassExsit()) {
            if (et_New_Password.getText().toString().isEmpty() || et_Retype_New_Password.getText().toString().isEmpty()
                    || et_Password.getText().toString().isEmpty()) {
                Toast.makeText(getActivity(), "Fulfil!", Toast.LENGTH_SHORT).show();
            } else {
                // if Password is true
                if (SharePref.CheckLogin(et_Password.getText().toString())) {
                    if (et_New_Password.getText().toString().equals(et_Retype_New_Password.getText().toString())) {
                        // Decrypt old pass AES and Encrypt new pass AES
                        SharePref.countLoginInputPassword(5);
                        change_file_key();
                        dismiss();
                    } else {
                        iv_d_p_notify_retype.setImageResource(R.drawable.ic_not_verify);
                    }
                }
                // if Password is false
                else {
                    iv_d_p_notify_password.setImageResource(R.drawable.ic_not_verify);
                    countDialog -= 1;
                    SharePref.countLoginInputPassword(countDialog);
                    tv_d_pass_notify.setText(countDialog + " times left");
                    tv_d_pass_notify.setTextColor(Objects.requireNonNull(getContext()).getResources().getColor(R.color.colorPrimaryDark));
                    if (countDialog == 0) {
                        SharePref.countLogin(60000);
                        startActivity(new Intent(getActivity(), Login.class));
                        Objects.requireNonNull(getActivity()).finish();
                        dismiss();
                    }
                }
            }

        }
        // AES password is not exist. Just make a new pass
        else {
            if (et_New_Password.getText().toString().isEmpty() || et_Retype_New_Password.getText().toString().isEmpty()) {
                Toast.makeText(getActivity(), "Fulfil!", Toast.LENGTH_SHORT).show();
            } else {
                //Check AES Password?
                if (SharePref.CheckLogin(et_Password.getText().toString())) {
                    if (et_New_Password.getText().toString().equals(et_Retype_New_Password.getText().toString())) {
                        //Save new AES Key to SharePref
                        SharePref.AESPass(et_New_Password.getText().toString());
                        Toast.makeText(getActivity(), "Success!", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getActivity(), Login.class));
                        Objects.requireNonNull(getActivity()).finish();
                        dismiss();
                    } else {
                        iv_d_p_notify_retype.setImageResource(R.drawable.ic_not_verify);
                    }
                } else {
                    Toast.makeText(getActivity(), "False Pass!", Toast.LENGTH_SHORT).show();
                }
            }
        }

    }

    //changing when we change AES pass
    // Decrypt old pass AES and Encrypt new pass AES
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void change_file_key() {
        // Read file encrypt privateKey.pem
        String encryptedTextBase64 = null;
        try {
            encryptedTextBase64 = com.example.signature.ECDSA.utils.File.read(SharePref.PathPri());
        } catch (Exception e) {
            e.printStackTrace();
        }
        // Decrypt AES
        String decryptedText = null;
        try {
            assert encryptedTextBase64 != null;
            decryptedText = AESpassword.decrypt(encryptedTextBase64, SharePref.SellectAESPass());
        } catch (Exception e) {
            e.printStackTrace();
        }
        //Save new AES password
        SharePref.AESPass(et_New_Password.getText().toString());
        // Make new publicKey.pem for new privateKey.pem
        PrivateKey privateKey;
        PublicKey publicKey = null;
        try {
            assert decryptedText != null;
            privateKey = PrivateKey.fromPem(decryptedText);
            publicKey = privateKey.publicKey();
        } catch (Exception e) {
            Toast.makeText(getActivity(), "Changed Password!", Toast.LENGTH_SHORT).show();
            Toast.makeText(getActivity(), "Couldn't Encrypted privateKey.pem!", Toast.LENGTH_SHORT).show();
        }
        // Encrypt privateKey with new AES password
        try {
            encryptedTextBase64 = AESpassword.encrypt(decryptedText.getBytes(AESpassword.UTF_8), SharePref.SellectAESPass());
        } catch (Exception e) {
            e.printStackTrace();
        }
        //Save Keys
        try {
            Generation_Keys.StringPrivateKey = encryptedTextBase64;
            assert publicKey != null;
            Generation_Keys.StringPublicKey = publicKey.toPem();
            Generation_Keys.saveKey(getContext());
            Toast.makeText(getActivity(), "Change Password Success!", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
