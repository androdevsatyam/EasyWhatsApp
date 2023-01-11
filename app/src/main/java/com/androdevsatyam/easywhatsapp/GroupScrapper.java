package com.androdevsatyam.easywhatsapp;

import static com.androdevsatyam.easywhatsapp.GlobalData.*;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.Settings;
import android.view.View;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.androdevsatyam.easywhatsapp.adapter.GroupNumbers;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Timer;
import java.util.TimerTask;

public class GroupScrapper extends AppCompatActivity {
    final int CREATE_FILE = 101;
    RecyclerView list;
    ProgressDialog dialog;
    ArrayList<String> numberlist = new ArrayList<>();
    ArrayList<String> names = new ArrayList<>();
    FloatingActionButton scrap;
    public FloatingActionButton create;
    public Toolbar toolbar;
    String unsaved="safe";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_scrapper);

        initView();

    }

    private void initView() {
        list = findViewById(R.id.data_list);
        scrap = findViewById(R.id.scrap);
        create = findViewById(R.id.create_group);

        list.setLayoutManager(new LinearLayoutManager(this));
        list.setHasFixedSize(true);

        dialog = new ProgressDialog(this);
        dialog.setMessage("Please Wait While Loading...");

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (GROUPSCRAPPER) {
            dialog.show();
            GROUPSCRAPPER = false;
            String[] data = GroupNumber.split(",");
            GroupNumber = "";
            numberlist.clear();
            if (data.length > 0) {
                ArrayList<String> filter = new ArrayList<>();
                numberlist.addAll(Arrays.asList(data));
                runOnUiThread(() -> {
                    filter.addAll(numberlist);
//                    for (String number : numberlist) {
//                        if (number.contains("+"))
//                            filter.add(number);
//                    }
                });
                numberlist.clear();
                numberlist.addAll(filter);

                names.addAll(Collections.nCopies(numberlist.size(), "group"));
                GroupNumbers adapter = new GroupNumbers(this, GlobalData.encode(GroupName), numberlist, names);
                list.setAdapter(adapter);
                unsaved="Data";

                if (getSupportActionBar() != null)
                    getSupportActionBar().setSubtitle("" + numberlist.size() + " Members");
                GroupNumber = "";
                if (numberlist.size() > 0)
                    create.setVisibility(View.VISIBLE);
                dialog.dismiss();
            } else {
                dialog.dismiss();
                toolbar.setSubtitle("No Contacts");
                GlobalData.makeToast("No Contacts Found", GroupScrapper.this, Toast.LENGTH_SHORT);
            }
        }
    }

    public void startGrabbing(View vw) {
        GroupNumber = "";
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose Platform");
        AlertDialog dialog = builder.create();
        dialog.setButton(DialogInterface.BUTTON_POSITIVE, "WhatsApp", (dialogInterface, i) -> {
            if (isAccessibilityOn(GroupScrapper.this, WhatsAccessService.class)) {
//            if (isAccessibilityOn(this, MyService.class)) {
                try {
                    GROUPSCRAPPER = true;
                    Intent launchIntent = getPackageManager().getLaunchIntentForPackage("com.whatsapp");
                    startActivity(launchIntent);
                    GlobalData.makeToast("After Choose Group Wait & Work Me :) :)", GroupScrapper.this, Toast.LENGTH_SHORT);
                } catch (Exception e) {
                    GROUPSCRAPPER = false;
                    GlobalData.makeToast("WhatsApp Not Found", GroupScrapper.this, Toast.LENGTH_SHORT);
                }
                dialog.dismiss();
            } else {
                Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
                makeToast("Turn On Accessibility", GroupScrapper.this, Toast.LENGTH_LONG);
                startActivity(intent);
            }
        });
        dialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Bus. WhatsApp", (dialogInterface, i) -> {
            if (isAccessibilityOn(this, WhatsAccessService.class)) {
//            if (isAccessibilityOn(this, MyService.class)) {
                try {
                    GROUPSCRAPPER = true;
                    Intent launchIntent = this.getPackageManager().getLaunchIntentForPackage("com.whatsapp.w4b");
                    startActivity(launchIntent);
                    GlobalData.makeToast("After Choose Group Wait & Work Me :) :)", GroupScrapper.this, Toast.LENGTH_SHORT);
                } catch (Exception e) {
                    GROUPSCRAPPER = false;
                    GlobalData.makeToast("WhatsApp Not Found", this, Toast.LENGTH_SHORT);
                }
                dialog.dismiss();
            } else {
                Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
                makeToast("Turn On Accessibility", this, Toast.LENGTH_LONG);
                startActivity(intent);
            }
        });

        dialog.show();
    }

    public void pickMannually(View view) {
        File file = new File(Environment.getExternalStorageDirectory() + "/EasyWhatsApp");
        if (!file.isDirectory())
            file.mkdir();

        File save = new File(file + "/" + GroupName + ".csv");
        if (save.isFile())
            save.delete();
        try {
            FileWriter writer = new FileWriter(save);
            writer.append("Name,Number");
            writer.append("\n");
            for (int i = 0; i < numberlist.size(); i++) {
                writer.append(names.get(i)).append(",").append(GlobalData.FormatNumbers(numberlist.get(i)));
                writer.append("\n");
            }
            writer.close();
            GlobalData.showNotification(GroupScrapper.this, "Exported", "file at " + save.getAbsolutePath());
            Toast.makeText(this, "File Saved at " + save.getAbsolutePath(), Toast.LENGTH_SHORT).show();
            unsaved="Saved";
        } catch (IOException e) {
            Toast.makeText(this, "Something goes wrong...", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Warning!");
        builder.setMessage("You have unsaved list, Are you sure to exit?");
        AlertDialog dialog = builder.create();
        dialog.setButton(DialogInterface.BUTTON_POSITIVE, "Yes", (dialogInterface, i) -> finish());
        dialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Save First", (dialogInterface, i) -> {
            pickMannually(null);
            dialog.dismiss();
        });

        if(unsaved.equalsIgnoreCase("data"))
            dialog.show();
        else
            super.onBackPressed();
    }
}