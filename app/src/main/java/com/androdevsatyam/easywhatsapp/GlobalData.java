package com.androdevsatyam.easywhatsapp;

import static android.content.Context.MODE_PRIVATE;

import android.Manifest;
import android.accessibilityservice.AccessibilityService;
import android.app.Activity;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatButton;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class GlobalData {

    public static final CharSequence APPLICATION_ID = "com.androdevsatyam.easywhatsapp";
    static String type = "com.whatsapp";
    static String tag = "GlobalData";
    public static String GroupName = "";
    public static String GroupNumber = "";
    static String countrycode = "91";
    static boolean send = false;
    static boolean fail = false;
    public static boolean SENDWHATS = false, GROUPSCRAPPER = false, local = true, dual = false, first = false, sendimg = false, sendtext = true, search = false;
    private static String seperator = "__/__", apkpath;
    private static ProgressDialog dialog;
    public static SharedPreferences sharedPreferences;
    public static File apkfile;

    public GlobalData(Context context) {
        dialog = new ProgressDialog(context);
        dialog.setMessage("Please Wait....");
        dialog.setCancelable(false);
    }

    public static String encode(String toString) {
        return URLEncoder.encode(toString);
    }

    public static void makeToast(String messagebody, Context context, int Duration) {
        Toast.makeText(context, messagebody, Duration).show();
    }

    public static void showNotification(Context context, String message, String subtext) {
        Log.e("Notification", "run");
        String title = context.getResources().getString(R.string.app_name);
        NotificationCompat.Builder notification = new NotificationCompat.Builder(context);
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        String NOTIFICATION_CHANNEL_ID = "1";
        if (Build.VERSION.SDK_INT >= 26) {
            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "My Notifications", NotificationManager.IMPORTANCE_HIGH);
            notificationChannel.setDescription("Channel description");
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(-65536);
            notificationChannel.setVibrationPattern(new long[]{700});
            notificationChannel.enableVibration(true);
            notificationChannel.setLockscreenVisibility(1);
            notificationManager.createNotificationChannel(notificationChannel);
        }
        notification.setSmallIcon(R.mipmap.new_icon);
        if (title != null)
            notification.setContentTitle(title);
        if (message != null)
            notification.setContentText(message);
        if (subtext != null)
            notification.setSubText(subtext);
        notification.setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.new_icon_round));
        notification.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
        notification.setCategory(Notification.CATEGORY_RECOMMENDATION);
        notification.setChannelId(NOTIFICATION_CHANNEL_ID);

        Log.e("Notification", "build create");
        notificationManager.notify(1, notification.build());
        Log.e("Notification", "notification run");
    }

    /*--- Convert bitmap to byte[]*/
    public static byte[] getBitmapAsByteArray(Bitmap image) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, bos);
        return bos.toByteArray();
    }
    public static boolean isAccessibilityOn(Context context, Class<? extends AccessibilityService> clazz) {
        int accessibilityEnabled = 0;
        final String service = context.getPackageName() + "/" + clazz.getCanonicalName();
        try {
            accessibilityEnabled = Settings.Secure.getInt(context.getApplicationContext().getContentResolver(), Settings.Secure.ACCESSIBILITY_ENABLED);
        } catch (Settings.SettingNotFoundException ignored) {
        }
        TextUtils.SimpleStringSplitter colonSplitter = new TextUtils.SimpleStringSplitter(':');
        if (accessibilityEnabled == 1) {
            String settingValue = Settings.Secure.getString(context.getApplicationContext().getContentResolver(), Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES);
            if (settingValue != null) {
                colonSplitter.setString(settingValue);
                while (colonSplitter.hasNext()) {
                    String accessibilityService = colonSplitter.next();
                    if (accessibilityService.equalsIgnoreCase(service)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
    public static String FormatNumbers(String number) {
        String num = number;
        num = num.replaceFirst("\\+91", "").trim();
        num = num.replaceAll(" ", "");
        num = num.replace("(", "");
        num = num.replace(") ", "").trim();
        num = num.replace("-", "").trim();
        return num;
    }

    public static void saveFile(Context context) {
        AssetManager am = context.getAssets();
        File sampleFile;
        String filepath;
        InputStream is = null;
        OutputStream os = null;
        int read;
        byte[] buffer = new byte[1024];
        File dirfile;
        try {
            String dirpath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/GBW";
            dirfile = new File(dirpath);
            if (!dirfile.exists())
                dirfile.mkdir();

            is = am.open("Sample.csv");
            sampleFile = new File(dirpath, "Sample.csv");
            os = new FileOutputStream(sampleFile);

            while ((read = is.read(buffer)) != -1)
                os.write(buffer, 0, read);

            is.close();
            is = null;

            os.flush();
            os.close();
            os = null;
            filepath = sampleFile.getPath();
            makeToast("File saved in GBW folder", context, Toast.LENGTH_LONG);
            Log.d("InstallService", "Path=" + filepath);
        } catch (Exception e) {
            Log.d("InstallService", "Error=>" + e.getMessage());
            makeToast("Something goes Wrong!", context, Toast.LENGTH_SHORT);
        }
    }

    public static void savePlugin(Context mainActivity) {
        AssetManager am = mainActivity.getAssets();
        InputStream is = null;
        OutputStream os = null;
        int read;
        byte[] buffer = new byte[1024];
        File dirfile;
        try {
            String dirpath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/GBW";
            dirfile = new File(dirpath);
            if (!dirfile.exists())
                dirfile.mkdir();

            is = am.open("gbwplugin.apk");
            apkfile = new File(dirpath, "gbwplugin.apk");
            os = new FileOutputStream(apkfile);

            while ((read = is.read(buffer)) != -1)
                os.write(buffer, 0, read);

            is.close();
            is = null;
            os.flush();
            os.close();
            os = null;
            apkpath = apkfile.getPath();
            Log.d("InstallService", "Path=" + apkpath);
        } catch (Exception e) {
            Log.d("InstallService", "Error=>" + e.getMessage());
            GlobalData.makeToast("Something goes Wrong!", mainActivity, Toast.LENGTH_SHORT);
        }
    }

    public static void hidePlugin(Context mainActivity) {
        File dirfile;
        try {
            String dirpath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/GBW";
            dirfile = new File(dirpath);
            if (!dirfile.exists())
                dirfile.mkdir();
            apkfile = new File(dirfile + "/gbwplugin.apk");
            if (apkfile.exists())
                apkfile.delete();
        } catch (Exception e) {
            Log.d("InstallService", "Error=>" + e.getMessage());
        }
    }

    public static void saveImage(String sourcePath) {
//        String sourcePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/GBW/tt_temp.3gp";
        File source = new File(sourcePath);

        String destinationPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/GBW/tt_1A.3gp";
        File destination = new File(destinationPath);
    }

    /*-------Website Functions---------*/
    public static String getWeblinkId() {
        SimpleDateFormat df = new SimpleDateFormat("ddMMyyyyhhmmss");
        Date date = Calendar.getInstance().getTime();
        String id = df.format(date);
        return id;
    }
    public static Intent getFileChooserIntent() {
        String[] mimeTypes = {"image/*", "application/pdf"};

        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            intent.setType(mimeTypes.length == 1 ? mimeTypes[0] : "*/*");
            if (mimeTypes.length > 0) {
                intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
            }
        } else {
            String mimeTypesStr = "";

            for (String mimeType : mimeTypes) {
                mimeTypesStr += mimeType + "|";
            }

            intent.setType(mimeTypesStr.substring(0, mimeTypesStr.length() - 1));
        }

        return intent;
    }
    //  Taking  Screenshot
    public static Bitmap saveScreenshot(Context context, View view) {
        Bitmap bitmap = null;
        try {
//            View v1 = greetingimg; //Layout to take screenshot
            view.setDrawingCacheEnabled(true);
            bitmap = Bitmap.createBitmap(view.getDrawingCache());
        } catch (Exception e) {
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        return bitmap;
    }
}
