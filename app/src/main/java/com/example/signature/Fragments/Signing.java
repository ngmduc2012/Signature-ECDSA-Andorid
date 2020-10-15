package com.example.signature.Fragments;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Vibrator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.example.signature.AES.AESpassword;
import com.example.signature.ECDSA.Ecdsa;
import com.example.signature.ECDSA.PrivateKey;
import com.example.signature.ECDSA.Signature;
import com.example.signature.Modle.RealPathUtil;
import com.example.signature.Modle.SharePref;
import com.example.signature.Modle.UtilsApplication;
import com.example.signature.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

import static android.app.Activity.RESULT_OK;

public class Signing extends Fragment {
    public static final int READ_REQUEST_CODE_GENERATE = 100;
    private static final int PICK_FROM_GALLERY = 101;
    @SuppressLint("StaticFieldLeak")
    public static TextView tvPublicKey, tvSignature, tv_s_doc;
    static ConstraintLayout ct_s_hint_doc, ct_s_doc, ct_s_btn_sign, ct_s_pub, ct_s_hint_pub, ct_s_signed;
    @SuppressLint("StaticFieldLeak")
    static ImageView iv_s_notify_signed;
    ArrayList<Uri> uris = new ArrayList<>();
    ImageView iv_s_clear_doc;
    ImageView iv_clear_signed;
    LinearLayout ll_s_share;
    Vibrator vibrator;
    View view;
    String pathDoc, pathSignature;
    private String nameFile;

    public Signing() {
        // Required empty public constructor
    }

    //set view
    public static void setViewSigningFragment() {
        // set view Public Key
        if (SharePref.PathPub().isEmpty()) {
            ct_s_pub.setVisibility(View.GONE);
            ct_s_hint_pub.setVisibility(View.VISIBLE);
        } else {
            tvPublicKey.setText(UtilsApplication.nameFile(SharePref.PathPub()));
            ct_s_pub.setVisibility(View.VISIBLE);
            ct_s_hint_pub.setVisibility(View.GONE);
        }
        //set view Document file
        if (tv_s_doc.getText().toString().isEmpty()) {
            ct_s_hint_doc.setVisibility(View.VISIBLE);
            ct_s_doc.setVisibility(View.GONE);
        } else {
            ct_s_hint_doc.setVisibility(View.GONE);
            ct_s_doc.setVisibility(View.VISIBLE);
        }
        // set view Signature File
        if (tvSignature.getText().toString().isEmpty()) {
            ct_s_btn_sign.setVisibility(View.VISIBLE);
            ct_s_signed.setVisibility(View.GONE);
            iv_s_notify_signed.setVisibility(View.GONE);
        } else {
            ct_s_btn_sign.setVisibility(View.GONE);
            ct_s_signed.setVisibility(View.VISIBLE);
            iv_s_notify_signed.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //init
        view = inflater.inflate(R.layout.fragment_signing, container, false);
        vibrator = (Vibrator) Objects.requireNonNull(getActivity()).getSystemService(Context.VIBRATOR_SERVICE);
        tv_s_doc = view.findViewById(R.id.tv_s_doc);
        ct_s_hint_doc = view.findViewById(R.id.ct_s_hint_doc);
        ct_s_signed = view.findViewById(R.id.ct_s_signed);
        ll_s_share = view.findViewById(R.id.ll_s_share);
        iv_s_clear_doc = view.findViewById(R.id.iv_s_clear_doc);
        iv_clear_signed = view.findViewById(R.id.iv_clear_signed);
        iv_s_notify_signed = view.findViewById(R.id.iv_s_notify_signed);
        tvPublicKey = view.findViewById(R.id.tvPublicKey);
        tvSignature = view.findViewById(R.id.tvSignature);
        ct_s_doc = view.findViewById(R.id.ct_s_doc);
        ct_s_hint_pub = view.findViewById(R.id.ct_s_hint_pub);
        ct_s_pub = view.findViewById(R.id.ct_s_pub);
        ct_s_btn_sign = view.findViewById(R.id.ct_s_btn_sign);
        // select file
        ct_s_hint_doc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnChooseFile();
            }
        });
        // signing
        ct_s_btn_sign.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                btnSigning();
            }
        });
        // share
        ll_s_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFolder();
            }
        });
        // clear path Doc
        iv_s_clear_doc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearPathDocSigningFragment();
            }
        });
        //clear path Sign
        iv_clear_signed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearPathSignatureSigningFreagment();
            }
        });
        // Show path Documnet File
        ct_s_doc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), pathDoc, Toast.LENGTH_SHORT).show();
            }
        });
        // Show path Public Key File
        ct_s_pub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), SharePref.PathPub(), Toast.LENGTH_SHORT).show();
            }
        });
        // Show path Signature File
        ct_s_signed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), pathSignature, Toast.LENGTH_SHORT).show();
            }
        });
        ct_s_hint_pub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (vibrator.hasVibrator()) {
                    vibrator.vibrate(432); // for 432 ms
                }
                Toast.makeText(getActivity(), "Back and generate Keys!", Toast.LENGTH_SHORT).show();
            }
        });
        return view;
    }

    // clear path signature
    public void clearPathSignatureSigningFreagment() {
        pathSignature = null;
        tvSignature.setText(null);
        setViewSigningFragment();
    }

    // clear path document
    public void clearPathDocSigningFragment() {
        pathDoc = null;
        tv_s_doc.setText(null);
        pathSignature = null;
        tvSignature.setText(null);
        setViewSigningFragment();
    }

    //Select file document
    public void btnChooseFile() {
        Intent fileIntent;
        fileIntent = new Intent(Intent.ACTION_GET_CONTENT);
        fileIntent.addCategory(Intent.CATEGORY_OPENABLE);
        fileIntent.setType("*/*");
        startActivityForResult(fileIntent, READ_REQUEST_CODE_GENERATE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == READ_REQUEST_CODE_GENERATE) {
            if (data != null) {
                //Read true path on phone
                pathDoc = RealPathUtil.getRealPath(getContext(), data.getData());
                for (String i : pathDoc.split("/")) {
                    nameFile = i;
                    if (i.equals("home")) {
                        File path = Environment.getExternalStorageDirectory();
                        pathDoc = path + "/Documents/" + Objects.requireNonNull(Objects.requireNonNull(data.getData()).getPath()).substring(
                                Objects.requireNonNull(data.getData().getPath()).indexOf(":") + 1);
                        nameFile = data.getData().getPath().substring(
                                data.getData().getPath().indexOf(":") + 1);
                        break;
                    }
                }
                tv_s_doc.setText(UtilsApplication.nameFile(pathDoc));
                setViewSigningFragment();
            }
        }
        if (requestCode == PICK_FROM_GALLERY && resultCode == RESULT_OK) {
            uris.clear();
            assert data != null;
            if (data.getClipData() != null) {
                //multiple data received
                ClipData clipData = data.getClipData();
                for (int count = 0; count < clipData.getItemCount(); count++) {
                    uris.add(clipData.getItemAt(count).getUri());
                }
            } else {
                uris.add(data.getData());
            }
            sendEmail();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void btnSigning() {
        if (SharePref.PathPri().isEmpty()) {
            if (vibrator.hasVibrator()) {
                vibrator.vibrate(432); // for 432 ms
            }
            Toast.makeText(getContext(), "You don't have key! Back to create key", Toast.LENGTH_SHORT).show();
        } else {
            if (tv_s_doc.getText().toString().isEmpty()) {
                if (vibrator.hasVibrator()) {
                    vibrator.vibrate(432); // for 432 ms
                }
                Toast.makeText(getContext(), "Select a file Document!", Toast.LENGTH_SHORT).show();
            } else {
                //Read encrypt private key file
                String encryptedTextBase64 = com.example.signature.ECDSA.utils.File.read(SharePref.PathPri());

                // Decrypt AES with encrypt private ky file
                String decryptedText = null;
                try {
                    decryptedText = AESpassword.decrypt(encryptedTextBase64, SharePref.SellectAESPass());
                } catch (Exception e) {
                    if (vibrator.hasVibrator()) {
                        vibrator.vibrate(432); // for 432 ms
                    }
                    Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
//            Toast.makeText(this, decryptedText, Toast.LENGTH_SHORT).show();
                String privateKeyPem = decryptedText;
                //Signature with document and privateKey.pem
                try {
                    assert privateKeyPem != null;
                    PrivateKey privateKey = PrivateKey.fromPem(privateKeyPem);
                    //Read document
                    String message = com.example.signature.ECDSA.utils.File.read(pathDoc);
                    //Signing
                    Signature signature = Ecdsa.sign(message, privateKey);
                    //Save byte file: signature.txt
                    byte[] derSign = signature.toDer().getBytes();
                    saveTest(derSign);
                    setViewSigningFragment();
                    Toast.makeText(getContext(), "Signed!", Toast.LENGTH_SHORT).show();

                } catch (Exception e) {
                    if (vibrator.hasVibrator()) {
                        vibrator.vibrate(432); // for 432 ms
                    }
                    Toast.makeText(getContext(), "privateKey.pem does not exist!", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    //save signature.txt
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void saveTest(byte[] byteData) {

        File path = Environment.getExternalStorageDirectory();
        //Save Signature Binary Text
        File dirSign = new File(path + "/Signature/");
        // create path file
        dirSign.mkdir();
        String filenameSign = "signatureBinary_" + nameFile + ".txt";
        File fileSign = new File(dirSign, filenameSign);
        try {
            FileOutputStream fos = new FileOutputStream(fileSign);
            fos.write(byteData);
            fos.close();
            pathSignature = "" + dirSign + "/" + filenameSign;
            tvSignature.setText(UtilsApplication.nameFile(pathSignature));
//            Toast.makeText(this, "Signature: " + dirSign + "/" + filenameSign, Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            if (vibrator.hasVibrator()) {
                vibrator.vibrate(432); // for 432 ms
            }
            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public void sendEmail() {
        try {
            Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
            emailIntent.setType("plain/text");
            emailIntent.setAction(Intent.ACTION_SEND_MULTIPLE);
            if (uris != null) {
                emailIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, uris);
            }
            this.startActivity(Intent.createChooser(emailIntent, "Sending"));

        } catch (Throwable t) {
            if (vibrator.hasVibrator()) {
                vibrator.vibrate(432); // for 432 ms
            }
            Toast.makeText(getContext(), "Request failed try again: " + t.toString(), Toast.LENGTH_LONG).show();
        }
    }

    // select file document
    public void openFolder() {
        Intent fileIntent;
        fileIntent = new Intent(Intent.ACTION_GET_CONTENT);
        fileIntent.addCategory(Intent.CATEGORY_OPENABLE);
        fileIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        fileIntent.setType("*/*");
        startActivityForResult(fileIntent, PICK_FROM_GALLERY);

    }


}