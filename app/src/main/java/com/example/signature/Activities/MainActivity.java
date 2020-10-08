package com.example.signature.Activities;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import com.example.signature.Adapters.AdapterFragments;
import com.example.signature.Fragments.Dialogs.DialogInfo;
import com.example.signature.Fragments.Generation_Keys;
import com.example.signature.Fragments.Signing;
import com.example.signature.Fragments.Verify;
import com.example.signature.R;
import com.google.android.material.tabs.TabLayout;


public class MainActivity extends AppCompatActivity {
    public static final int REQUIRED_CODE = 1;
    CountDownTimer countDownTimer;
    ImageView iv_g_info;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private AdapterFragments adapterFragment;
    private int countTime = 216000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        timeCount();
        adapterViewPager();
        permission();
        // show info
        iv_g_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialogInfo();
            }
        });
    }

    public void init() {
        tabLayout = findViewById(R.id.tableLayout);
        viewPager = findViewById(R.id.viewPager);
        iv_g_info = findViewById(R.id.iv_g_info);
        adapterFragment = new AdapterFragments(getSupportFragmentManager());

    }

    // open info
    public void openDialogInfo() {
        DialogInfo exampleDialogInfo = new DialogInfo();
        exampleDialogInfo.show(MainActivity.this.getSupportFragmentManager(), "dialog_info");
    }

    // count time to out app
    public void countTime() {
        countDownTimer.cancel();
        timeCount();
    }

    // out to login after 7.2 minutes
    public void timeCount() {
        countDownTimer = new CountDownTimer(countTime, 1000) {
            public void onTick(long millisUntilFinished) {
            }

            public void onFinish() {
                startActivity(new Intent(MainActivity.this, Login.class));
                MainActivity.this.onStop();
            }

        }.start();
    }

    // set view pager
    public void adapterViewPager() {
        adapterFragment.AddFragment(new Generation_Keys(), "");
        adapterFragment.AddFragment(new Signing(), "");
        adapterFragment.AddFragment(new Verify(), "");
        viewPager.setAdapter(adapterFragment);
        tabLayout.setupWithViewPager(viewPager);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 0) {
                    countTime();
                } else if (position == 1) {
                    Signing.setViewSigningFragment();
                    countTime();
                } else if (position == 2) {
                    countTime();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    //Permission
    private void permission() {
        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                + ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    || ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Grant those permission!");
                builder.setMessage("Write and read external storage, use biometric, internet, access network state, read phone state");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ActivityCompat.requestPermissions(MainActivity.this,
                                new String[]{
                                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                        Manifest.permission.READ_EXTERNAL_STORAGE,
                                }, REQUIRED_CODE);
                    }
                });
                builder.setNegativeButton("Cancel", null);
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            } else {
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{
                                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                Manifest.permission.READ_EXTERNAL_STORAGE,
                        }, REQUIRED_CODE);
            }

        } else {
            Toast.makeText(this, "Pemission already granted!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUIRED_CODE) {
            if ((grantResults.length > 0) && (grantResults[0] + grantResults[1]) == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission Granded!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Premission Denied!", Toast.LENGTH_SHORT).show();
            }

        }
    }

}