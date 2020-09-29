package com.example.signature.Fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.example.signature.AES.AESpassword;
import com.example.signature.ECDSA.PrivateKey;
import com.example.signature.ECDSA.PublicKey;
import com.example.signature.Fragments.Dialogs.DialogCheckPassword;
import com.example.signature.Fragments.Dialogs.DialogInfo;
import com.example.signature.Fragments.Dialogs.DialogPassword;
import com.example.signature.Modle.SharePref;
import com.example.signature.Modle.UtilsApplication;
import com.example.signature.R;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Objects;

public class Generation_Keys extends Fragment {
    public static String StringPrivateKey, StringPublicKey;
    @SuppressLint("StaticFieldLeak")
    static TextView tv_g_pri, tv_g_pub;
    static ConstraintLayout ct_g_pri, ct_g_pub, ct_g_generate;
    View view;
    LinearLayout ll_g_pass, ii_g_close;
    ImageView iv_g_clear_key, iv_g_info;

    public Generation_Keys() {

    }

    // setView
    public static void setView() {
        if (SharePref.PathPri().isEmpty() || SharePref.PathPub().isEmpty()) {
            ct_g_pub.setVisibility(View.GONE);
            ct_g_pri.setVisibility(View.GONE);
            ct_g_generate.setVisibility(View.VISIBLE);
        } else {
            ct_g_generate.setVisibility(View.GONE);
            ct_g_pub.setVisibility(View.VISIBLE);
            ct_g_pri.setVisibility(View.VISIBLE);
            tv_g_pri.setText(UtilsApplication.nameFile(SharePref.PathPri()));
            tv_g_pub.setText(UtilsApplication.nameFile(SharePref.PathPub()));
        }
    }

    public static void saveKey(Context context) {
        try {
            File path = Environment.getExternalStorageDirectory();

            // create path dir
            File dir = new File(path + "/Signature/");
            // create path file
            dir.mkdir();

            //Save Private Key
            File dirPri = new File(path + "/Signature/PrivateKey/");
            // create path file
            dirPri.mkdir();
            String filenamePri = "privateKey.pem";
            File filePri = new File(dirPri, filenamePri);
            FileWriter fileWriterPri = new FileWriter(filePri.getAbsoluteFile());
            BufferedWriter bufferedWriterPri = new BufferedWriter(fileWriterPri);
            bufferedWriterPri.write(StringPrivateKey);
            bufferedWriterPri.close();

            //Save Public Key
            File dirPub = new File(path + "/Signature/");
            // create path file
            dirPub.mkdir();
            String filenamePub = "publicKey.pem";
            File filePub = new File(dirPub, filenamePub);
            FileWriter fileWriterPub = new FileWriter(filePub.getAbsoluteFile());
            BufferedWriter bufferedWriterPub = new BufferedWriter(fileWriterPub);
            bufferedWriterPub.write(StringPublicKey);
            bufferedWriterPub.close();

            SharePref.SaveKey("" + dirPri + "/" + filenamePri, "" + dirPub + "/" + filenamePub);
            setView();
        } catch (IOException e) {
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_generate__keys, container, false);
        // init
        tv_g_pri = view.findViewById(R.id.tv_g_pri);
        tv_g_pub = view.findViewById(R.id.tv_g_pub);
        ll_g_pass = view.findViewById(R.id.ll_g_pass);
        ii_g_close = view.findViewById(R.id.ii_g_close);
        ct_g_pri = view.findViewById(R.id.ct_g_pri);
        ct_g_pub = view.findViewById(R.id.ct_g_pub);
        ct_g_generate = view.findViewById(R.id.ct_g_generate);
        iv_g_clear_key = view.findViewById(R.id.iv_g_clear_pri);
        iv_g_info = view.findViewById(R.id.iv_g_info);
        //set Views
        setView();
        if (!SharePref.CheckPassExsit()) {
            openDialogPassword();
        }
        // generate keys
        ct_g_generate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                create_private_key();
            }
        });
        // open Dialog to create or change password
        ll_g_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialogPassword();
            }
        });
        // Show path public key
        ct_g_pub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), SharePref.PathPub(), Toast.LENGTH_SHORT).show();
            }
        });
        // Show path private key
        ct_g_pri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), SharePref.PathPri(), Toast.LENGTH_SHORT).show();
            }
        });
        //close app
        ii_g_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Objects.requireNonNull(getActivity()).finishAndRemoveTask();
            }
        });
        // new pri-pub keys
        iv_g_clear_key.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearPrivateKey();
            }
        });
        // show info
        iv_g_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialogInfo();
            }
        });
        return view;
    }

    // open info
    public void openDialogInfo() {
        DialogInfo exampleDialogInfo = new DialogInfo();
        exampleDialogInfo.show(Objects.requireNonNull(getActivity()).getSupportFragmentManager(), "dialog_info");
    }


    // add new private key
    public void clearPrivateKey() {

        DialogCheckPassword exampleCheckDialog = new DialogCheckPassword();
        exampleCheckDialog.show(Objects.requireNonNull(getActivity()).getSupportFragmentManager(), "dialog_check");
    }

    // create or change password
    public void openDialogPassword() {
        DialogPassword exampleDialog = new DialogPassword();
        exampleDialog.show(Objects.requireNonNull(getActivity()).getSupportFragmentManager(), "dialog");
    }

    // Create a new private Key: privateKey.pem
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void create_private_key() {
        //When these is no private Key
        if (SharePref.CheckPassExsit()) {
            //Create private key
            PrivateKey privateKey = new PrivateKey();
            //Create public key
            PublicKey publicKey = privateKey.publicKey();
            //Encrypt Private Key
            String encryptedTextBase64 = null;
            try {
                encryptedTextBase64 = AESpassword.encrypt(privateKey.toPem().getBytes(AESpassword.UTF_8), SharePref.SellectAESPass());
            } catch (Exception e) {
                e.printStackTrace();
            }
            //save Key
            StringPrivateKey = encryptedTextBase64;
            StringPublicKey = publicKey.toPem();
            saveKey(getContext());
            Toast.makeText(getActivity(), "Generated Keys Success!", Toast.LENGTH_SHORT).show();
        } else {
            openDialogPassword();
        }

    }

}