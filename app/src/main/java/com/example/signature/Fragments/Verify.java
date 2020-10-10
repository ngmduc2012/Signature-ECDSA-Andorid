package com.example.signature.Fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
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

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.signature.ECDSA.Ecdsa;
import com.example.signature.ECDSA.PublicKey;
import com.example.signature.ECDSA.Signature;
import com.example.signature.ECDSA.utils.ByteString;
import com.example.signature.Modle.RealPathUtil;
import com.example.signature.Modle.UtilsApplication;
import com.example.signature.R;

import java.io.File;
import java.util.Objects;

public class Verify extends Fragment {
    public static final int READ_REQUEST_CODE_VERIFY_DOC = 108;
    public static final int READ_REQUEST_CODE_VERIFY_SIGN = 72;
    public static final int READ_REQUEST_CODE_VERIFY_PUB = 432;
    static ConstraintLayout ct_v_hint_pub, ct_v_hint_doc, ct_v_hint_sign, ct_v_pub, ct_v_doc, ct_v_sign, ct_v_result;
    TextView tvResult;
    TextView tv_v_pub;
    TextView tv_v_doc;
    TextView tv_v_Signature;
    ImageView iv_v_result;
    View view;
    ImageView iv_v_clear_sign;
    ImageView iv_v_clear_doc;
    ImageView iv_v_clear_pub, iv_v_notify_pub, iv_v_notify_doc, iv_v_notify_sign;
    LinearLayout ll_v_veriry;
    String pathDoc_v = "";
    String pathSign = "";
    String pathPub = "";
    String verifiedResult = "";


    public Verify() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_verify, container, false);
        tv_v_doc = view.findViewById(R.id.tv_v_doc);
        tv_v_pub = view.findViewById(R.id.tv_v_pub);
        tv_v_Signature = view.findViewById(R.id.tv_v_Signature);
        tvResult = view.findViewById(R.id.tvResult);
        ct_v_pub = view.findViewById(R.id.ct_v_pub);
        ct_v_doc = view.findViewById(R.id.ct_v_doc);
        ct_v_sign = view.findViewById(R.id.ct_v_sign);
        ct_v_hint_doc = view.findViewById(R.id.ct_v_hint_doc);
        ct_v_hint_pub = view.findViewById(R.id.ct_v_hint_pub);
        ct_v_hint_sign = view.findViewById(R.id.ct_v_hint_sign);
        ct_v_result = view.findViewById(R.id.ct_v_result);
        ll_v_veriry = view.findViewById(R.id.ll_v_veriry);
        iv_v_notify_sign = view.findViewById(R.id.iv_v_notify_sign);
        iv_v_result = view.findViewById(R.id.iv_v_result);
        iv_v_notify_doc = view.findViewById(R.id.iv_v_notify_doc);
        iv_v_notify_pub = view.findViewById(R.id.iv_v_notify_pub);
        iv_v_clear_pub = view.findViewById(R.id.iv_v_clear_pub);
        iv_v_clear_doc = view.findViewById(R.id.iv_v_clear_doc);
        ll_v_veriry = view.findViewById(R.id.ll_v_veriry);
        iv_v_clear_sign = view.findViewById(R.id.iv_v_clear_sign);
        setViewVerifyFragment();
        ll_v_veriry.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                btnVerify();
            }
        });
        ct_v_hint_pub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnPub();
            }
        });
        ct_v_hint_doc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnDoc();
            }
        });
        ct_v_hint_sign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnSign();
            }
        });
        iv_v_clear_sign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearPathSignatureFileVerify();
            }
        });
        iv_v_clear_doc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearPathDocumentFileVerify();
            }
        });
        iv_v_clear_pub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearPathPublicKeyFileVerify();
            }
        });
        ct_v_pub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), pathPub, Toast.LENGTH_SHORT).show();
            }
        });
        ct_v_doc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), pathDoc_v, Toast.LENGTH_SHORT).show();
            }
        });
        ct_v_sign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), pathSign, Toast.LENGTH_SHORT).show();
            }
        });
        return view;

    }

    public void clearPathSignatureFileVerify() {
        iv_v_notify_sign.setImageResource(R.drawable.ic_wrong_file);
        pathSign = "";
        verifiedResult = "";
        tv_v_Signature.setText(null);
        setViewVerifyFragment();
    }

    public void clearPathDocumentFileVerify() {
        iv_v_notify_doc.setImageResource(R.drawable.ic_wrong_file);
        pathDoc_v = "";
        verifiedResult = "";
        tv_v_doc.setText(null);
        setViewVerifyFragment();
    }

    public void clearPathPublicKeyFileVerify() {
        iv_v_notify_pub.setImageResource(R.drawable.ic_wrong_file);
        pathPub = "";
        verifiedResult = "";
        tv_v_pub.setText(null);
        setViewVerifyFragment();
    }

    //set view Verify Fragment
    @SuppressLint("SetTextI18n")
    public void setViewVerifyFragment() {
        // set public file
        if (pathPub.equals("")) {
            ct_v_pub.setVisibility(View.GONE);
            ct_v_hint_pub.setVisibility(View.VISIBLE);
            ct_v_result.setVisibility(View.GONE);
        } else {
            ct_v_pub.setVisibility(View.VISIBLE);
            ct_v_hint_pub.setVisibility(View.GONE);
            tv_v_pub.setText(UtilsApplication.nameFile(pathPub));
        }
        //set document file
        if (pathDoc_v.equals("")) {
            ct_v_doc.setVisibility(View.GONE);
            ct_v_hint_doc.setVisibility(View.VISIBLE);
            ct_v_result.setVisibility(View.GONE);
        } else {
            ct_v_doc.setVisibility(View.VISIBLE);
            ct_v_hint_doc.setVisibility(View.GONE);
            tv_v_doc.setText(UtilsApplication.nameFile(pathDoc_v));
        }
        // set signature file
        if (pathSign.equals("")) {
            ct_v_sign.setVisibility(View.GONE);
            ct_v_hint_sign.setVisibility(View.VISIBLE);
            ct_v_result.setVisibility(View.GONE);
        } else {
            ct_v_sign.setVisibility(View.VISIBLE);
            ct_v_hint_sign.setVisibility(View.GONE);
            tv_v_Signature.setText(UtilsApplication.nameFile(pathSign));

        }
        // set view result verify
        if (verifiedResult.equals("true")) {
            ct_v_result.setVisibility(View.VISIBLE);
            iv_v_result.setImageResource(R.drawable.ic_verify_checking);
            tvResult.setText("Signature is " + verifiedResult + "!");
            tvResult.setTextColor(ContextCompat.getColor(Objects.requireNonNull(getActivity()), R.color.green_check_true));
            iv_v_notify_pub.setImageResource(R.drawable.ic_verify_checking);
            iv_v_notify_doc.setImageResource(R.drawable.ic_verify_checking);
            iv_v_notify_sign.setImageResource(R.drawable.ic_verify_checking);
        } else if (verifiedResult.equals("false")) {
            ct_v_result.setVisibility(View.VISIBLE);
            iv_v_result.setImageResource(R.drawable.ic_not_verify);
            tvResult.setText("Signature is " + verifiedResult + "!");
            tvResult.setTextColor(ContextCompat.getColor(Objects.requireNonNull(getActivity()), R.color.red_check_false));

        } else {
            ct_v_result.setVisibility(View.GONE);
        }
    }

    //Select Document
    public void btnDoc() {
        Intent fileIntent;
        fileIntent = new Intent(Intent.ACTION_GET_CONTENT);
        fileIntent.addCategory(Intent.CATEGORY_OPENABLE);
        fileIntent.setType("*/*");
        startActivityForResult(fileIntent, READ_REQUEST_CODE_VERIFY_DOC);
    }

    //select signature.txt
    public void btnSign() {
        Intent fileIntent;
        fileIntent = new Intent(Intent.ACTION_GET_CONTENT);
        fileIntent.addCategory(Intent.CATEGORY_OPENABLE);
        fileIntent.setType("*/*");
        startActivityForResult(fileIntent, READ_REQUEST_CODE_VERIFY_SIGN);
    }

    //select publicKey.pem
    public void btnPub() {
        Intent fileIntent;
        fileIntent = new Intent(Intent.ACTION_GET_CONTENT);
        fileIntent.addCategory(Intent.CATEGORY_OPENABLE);
        fileIntent.setType("*/*");
        startActivityForResult(fileIntent, READ_REQUEST_CODE_VERIFY_PUB);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == READ_REQUEST_CODE_VERIFY_DOC) {
            if (data != null) {
                pathDoc_v = pathFileData(data);
                tv_v_doc.setText(UtilsApplication.nameFile(pathDoc_v));
                setViewVerifyFragment();
            }
        }
        if (requestCode == READ_REQUEST_CODE_VERIFY_SIGN) {
            if (data != null) {
                pathSign = pathFileData(data);
                tv_v_Signature.setText(UtilsApplication.nameFile(pathSign));
                setViewVerifyFragment();
            }
        }
        if (requestCode == READ_REQUEST_CODE_VERIFY_PUB) {
            if (data != null) {
                pathPub = pathFileData(data);
                tv_v_pub.setText(UtilsApplication.nameFile(pathPub));
                setViewVerifyFragment();
            }
        }
    }

    //select true path form phone
    public String pathFileData(Intent data) {
        String pathFile = null;
        if (data != null) {
            pathFile = RealPathUtil.getRealPath(getActivity(), data.getData());
            for (String i : pathFile.split("/")) {
                if (i.equals("home")) {
                    File path = Environment.getExternalStorageDirectory();
                    pathFile = path + "/Documents/" + Objects.requireNonNull(Objects.requireNonNull(data.getData()).getPath()).substring(
                            Objects.requireNonNull(data.getData().getPath()).indexOf(":") + 1);
                    break;
                }
            }
//            Toast.makeText(this, pathFile, Toast.LENGTH_SHORT).show();
        }
        return pathFile;
    }

    //Verify
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void btnVerify() {
        if (tv_v_Signature.getText().toString().isEmpty()
                || tv_v_pub.getText().toString().isEmpty()
                || tv_v_doc.getText().toString().isEmpty()) {
            Toast.makeText(getActivity(), "Select Files!", Toast.LENGTH_SHORT).show();
        } else {
            //Read publicKey.pem
            String publicKeyPem = com.example.signature.ECDSA.utils.File.read(pathPub);
            PublicKey publicKey = null;
            try {
                publicKey = PublicKey.fromPem(publicKeyPem);
                iv_v_notify_pub.setImageResource(R.drawable.ic_verify_checking);
            } catch (Exception e) {
                iv_v_notify_pub.setImageResource(R.drawable.ic_not_verify);
            }
            //Read signature.txt
            byte[] signatureBin = com.example.signature.ECDSA.utils.File.readBytes(pathSign);
            ByteString byteString = new ByteString(signatureBin);
            Signature signature = null;
            try {
                signature = Signature.fromDer(byteString);
                iv_v_notify_sign.setImageResource(R.drawable.ic_verify_checking);
            } catch (Exception e) {
                iv_v_notify_sign.setImageResource(R.drawable.ic_not_verify);
            }
            //Read document
            String message = com.example.signature.ECDSA.utils.File.read(pathDoc_v);
            //Verify
            try {
                boolean verified;
                verified = Ecdsa.verify(message, signature, publicKey);
                verifiedResult = "" + verified;
                setViewVerifyFragment();
                Toast.makeText(getActivity(), "Signature is " + verified + "!", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                Toast.makeText(getActivity(), "Reason: Permission or Type of File or Not exist!", Toast.LENGTH_SHORT).show();
            }
        }
    }
}