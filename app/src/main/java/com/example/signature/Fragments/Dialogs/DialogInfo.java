package com.example.signature.Fragments.Dialogs;


import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.signature.Adapters.RecycleViewInfoAdapter;
import com.example.signature.Modle.InfoApp;
import com.example.signature.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class DialogInfo extends AppCompatDialogFragment {
    private List<InfoApp> list;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = Objects.requireNonNull(getActivity()).getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_dialog_info, null);
        // adapter recycler View
        RecyclerView recyclerView = view.findViewById(R.id.rv_info);
        RecycleViewInfoAdapter recycleViewAdapter = new RecycleViewInfoAdapter(getContext(), list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(recycleViewAdapter);
        builder.setView(view).create();
        return builder.create();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // add data info app
        list = new ArrayList<>();
        list.add(new InfoApp(R.drawable.ic_generate_keys, "Use ECDSA to generate Keys"));
        list.add(new InfoApp(R.drawable.ic_clear_path_public_key, "Delete path selected file")); // add image = R.drawable.image type int
        list.add(new InfoApp(R.drawable.ic_baseline_control_point_24, "Add path selected file"));
        list.add(new InfoApp(R.drawable.ic_baseline_fingerprint_24, "Use Finger Print"));
        list.add(new InfoApp(R.drawable.ic_close_app, "Close app"));
        list.add(new InfoApp(R.drawable.ic_share, "Share Files"));
        list.add(new InfoApp(R.drawable.ic_hind_password, "Hint password input"));
        list.add(new InfoApp(R.drawable.ic_show_password, "Show password input"));
        list.add(new InfoApp(R.drawable.ic_show_info, "Info App"));
        list.add(new InfoApp(R.drawable.ic_signature_file, "Signature"));
        list.add(new InfoApp(R.drawable.ic_document, "Document File"));
        list.add(new InfoApp(R.drawable.ic_pri_key, "Private Key"));
        list.add(new InfoApp(R.drawable.ic_pub_key, "Public Key"));
        list.add(new InfoApp(R.drawable.ic_signing, "Signing"));
        list.add(new InfoApp(R.drawable.ic_table_sign, "Signature Page"));
        list.add(new InfoApp(R.drawable.ic_verify, "Verify Page"));
        list.add(new InfoApp(R.drawable.ic_key, "Generate Keys Page"));
        list.add(new InfoApp(R.drawable.ic_verify_checking, "True"));
        list.add(new InfoApp(R.drawable.ic_wrong_file, "Warning"));
        list.add(new InfoApp(R.drawable.ic_not_verify, "Not True"));
        list.add(new InfoApp(R.drawable.ic_password_aes, "Use AES Password to encryption \nPrivate Key"));
        list.add(new InfoApp(R.drawable.ic_saved, "Saved File"));

    }

}
