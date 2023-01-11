package com.androdevsatyam.easywhatsapp.helpers;

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

import com.androdevsatyam.easywhatsapp.R;

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

    public static final long API_CONNECTION_TIMEOUT = 1201;
    public static final long API_READ_TIMEOUT = 901;
    static ArrayList<String> listname, listnum;
    static String type = "com.whatsapp";
    static String tag = "GlobalData";
    public static String GroupName = "";
    public static String GroupNumber = "";
    public static String GroupStatus = "";
    static String name, number, countrycode = "91";
    static ArrayList<String>
            SendedNumber = new ArrayList<>(),
            FailedNumber = new ArrayList<>(),
            FinalNumber = new ArrayList<>();
    static boolean send = false;
    static boolean fail = false;
    public static boolean SENDWHATS = false, GROUPSCRAPPER = false, local = true, dual = false, first = false, sendimg = false, sendtext = true, search = false;
    static int I = 0;
    static Uri imguri;
    static File img;
    private static String seperator = "__/__", apkpath;
    ;
    private static ProgressDialog dialog;
    private static CountDownTimer countDownTimer;
    public static SharedPreferences sharedPreferences;
    public static File apkfile;

    public GlobalData(Context context) {
        dialog = new ProgressDialog(context);
        dialog.setMessage("Please wait....");
    }

    static String formatDate(String purchasedate) {
        Log.d("FormatDate", purchasedate);
        SimpleDateFormat convertdate = new SimpleDateFormat("yyyy-MM-dd"),
                formatDate = new SimpleDateFormat("dd-MM-yyyy");
        Date date;
        try {
            date = convertdate.parse(purchasedate);
            if (date != null) {
                Log.d("FormatDate", formatDate.format(date));
                return formatDate.format(date);
            } else
                return null;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String encode(String toString) {
        return URLEncoder.encode(toString);
    }

    public static String decode(String toString) {
        if (toString != null)
            return URLDecoder.decode(toString);
        else
            return "";
    }

    public static void makeToast(String messagebody, Context context, int Duration) {
        Toast.makeText(context, messagebody, Duration).show();
    }

    public static String getBaseforBitmap(Bitmap bitmap) {
        byte[] bitmaparray = GlobalData.getBitmapAsByteArray(bitmap);
        return Base64.encodeToString(bitmaparray, Base64.DEFAULT);
    }

    public static void hideCustomDialogWarning() {
        dialog.dismiss();
        dialog.cancel();
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
        notification.setSmallIcon(R.mipmap.ic_launcher);
        if (title != null)
            notification.setContentTitle(title);
        if (message != null)
            notification.setContentText(message);
        if (subtext != null)
            notification.setSubText(subtext);
        notification.setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher));
        notification.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
        notification.setCategory(Notification.CATEGORY_RECOMMENDATION);
        notification.setChannelId(NOTIFICATION_CHANNEL_ID);

        Log.e("Notification", "build create");
        notificationManager.notify(1, notification.build());
        Log.e("Notification", "notification run");
    }

    public static String convertArrayToString(ArrayList<String> array) {
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < array.size(); i++) {
            str.append(array.get(i).replace("-", ""));
            // Do not append comma at the end of last element
            if (i < array.size() - 1) {
                str.append(seperator);
            }
        }
        return str.toString();
    }

    static ArrayList<String> convertStringToArray(String str) {
        String[] arr = str.split(seperator);
        return new ArrayList<>(Arrays.asList(arr));
    }

    /*--- Convert bitmap to byte[]*/
    public static byte[] getBitmapAsByteArray(Bitmap image) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, bos);
        return bos.toByteArray();
    }

    public static Bitmap getByteAsBitmap(byte[] array) {
        return BitmapFactory.decodeByteArray(array, 0, array.length);
    }

    public static Bitmap getBitmapbyUri(String path) throws IOException {
        Bitmap bitmap = null;
        try {
            File f = new File(path);
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
            bitmap = BitmapFactory.decodeStream(new FileInputStream(f), null, options);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
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

    public static boolean isPackageInstalled(String str, PackageManager packageManager) {
        try {
            packageManager.getPackageInfo(str, 0);
            return true;
        } catch (PackageManager.NameNotFoundException unused) {
            return false;
        }
    }

    static boolean getShowStatus(Context context) {
        SharedPreferences preferences = context.getSharedPreferences("ShowDirection", MODE_PRIVATE);
        return preferences.getBoolean("Warning", true);
    }

    static void setShowStatus(Context context, boolean value) {
        SharedPreferences preferences = context.getSharedPreferences("ShowDirection", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("Warning", value);
        editor.apply();
    }

    static void setSendPrefrence(Context context) {
        SharedPreferences preferences = context.getSharedPreferences("Platform", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("pack", type);
        editor.putBoolean("type", dual);
        editor.apply();
    }

    static void getSendPrefrence(Context context) {
        try {
            SharedPreferences preferences = context.getSharedPreferences("Platform", MODE_PRIVATE);
            type = preferences.getString("pack", "com.whatsapp");
            dual = preferences.getBoolean("type", false);
        } catch (Exception e) {
            type = "com.whatsapp";
            dual = false;
        }
    }

    static String getCountryCode(Context context) {
        SharedPreferences preferences = context.getSharedPreferences("CountryCode", MODE_PRIVATE);
        countrycode = preferences.getString("CCD", "91");
        return preferences.getString("CCD", "91");
    }

    static void setCountrycode(Context context, String value) {
        countrycode = value;
        SharedPreferences preferences = context.getSharedPreferences("CountryCode", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("CCD", value);
        editor.apply();
    }

    public static void setSendType(Context context, String value) {
        SharedPreferences preferences = context.getSharedPreferences("SendType", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("ISOld", value);
        editor.apply();
    }

    public static String getSendType(Context context) {
        SharedPreferences preferences = context.getSharedPreferences("SendType", MODE_PRIVATE);
        return preferences.getString("ISOld", "No_Check");
    }

    public static void setFirebaseToken(Context context, String token) {
        try {
            SharedPreferences preferences = context.getSharedPreferences("Firebase", MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("Token", token);
            editor.apply();
        } catch (Exception e) {
            if (e.getLocalizedMessage() != null)
                Log.d(tag, "getsharError=>" + e.getLocalizedMessage());
            else
                Log.d(tag, "getsharError");
        }
    }

    public static String getFirebaseToken(Context context) {
        SharedPreferences preferences = context.getSharedPreferences("Firebase", MODE_PRIVATE);
        return preferences.getString("Token", "No_Token");
    }

    public static void setShared(Context context, String name, String value) {
        sharedPreferences = context.getSharedPreferences("WhatsAppService", MODE_PRIVATE);
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putString(name, value);
        edit.apply();
    }

    public static String getShared(Context context, String name, String defvalue) {
        sharedPreferences = context.getSharedPreferences("WhatsAppService", MODE_PRIVATE);
        return sharedPreferences.getString(name, defvalue);
    }

    public static String FormatNumbers(String number) {
        String num = number;
//        num = num.replaceFirst("\\+91", "").trim();
        num = num.replace(" ", "");
        num = num.replace("(", "");
        num = num.replace(") ", "").trim();
        num = num.replace("-", "").trim();
        return num;
    }

    public static String getDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String dates;
        Date date = new Date();
        date.getTime();
        dates = sdf.format(date);
        return dates;
    }

    public static String getDateTime() {
        return String.valueOf(Calendar.getInstance().getTimeInMillis());
    }

    static String getDeviceId(Context context) {
        String deviceIdentifier;
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (context.checkSelfPermission(Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_DENIED) {
                return "Permission Denied";
            }
        }
        try {
            deviceIdentifier = tm.getDeviceId().toUpperCase();
            if (deviceIdentifier.isEmpty()) {
                deviceIdentifier = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID).toUpperCase();
            }
        } catch (Exception e) {
            if (Settings.Secure.getString(context.getContentResolver(), "android_id") != null)
                deviceIdentifier = Settings.Secure.getString(context.getContentResolver(), "android_id").toUpperCase();
            else
                deviceIdentifier = "No_id";
            Log.d("DeviceId", e.getLocalizedMessage());
        }//
        Log.d("DeviceId", deviceIdentifier);
        return deviceIdentifier /*"866297042074216"*/;
    }

    public static void goTo(Context context, Class destclass, String type) {
        Intent intent = new Intent(context, destclass);
        if (type != null) {
            if (type.equalsIgnoreCase("register")) {
                intent.putExtra("type", "newUser");
                context.startActivity(intent);
                ((Activity) context).finish();
            }
            if (type.equalsIgnoreCase("change_mobile")) {
                intent.putExtra("type", "change_mobile");
                context.startActivity(intent);
            } else if (type.equalsIgnoreCase("finish")) {
                context.startActivity(intent);
                ((Activity) context).finish();
            }
        } else
            context.startActivity(intent);
    }

    static void requestPermission(Context context, String[] permissions) {
        ActivityCompat.requestPermissions((Activity) context, permissions, 0);
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

    public static byte[] getBasetobyte(String userprofile) {
        byte[] profilearray = Base64.decode(userprofile, Base64.DEFAULT);
        return profilearray;
    }

    public void showProgress() {
        dialog.show();
    }

    public void stopProgress() {
        dialog.dismiss();
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

    public static byte[] getByteForUri(Context context, Uri uri) {
        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), uri);
            byte[] imagearray = GlobalData.getBitmapAsByteArray(bitmap);

            return imagearray;
        } catch (IOException e) {
            GlobalData.makeToast("Unable to load image", context, Toast.LENGTH_SHORT);
            Log.w("Portfoliyo", e.getMessage());
        }
        return null;
    }

    public static final String PREF_NAME = "whatsapp_service";
    public static final String ACCESSIBILITY_SERVICE = "accessibility_service";
    public static final String ACCESSIBILITY_SERVICE_GROUP = "accessibility_service_group";
    public static final String GROUP_CONTACTS = "group_contacts";
    public static final String GROUP_NAME = "grabbed_group_name";
    public static final String MESSAGE_SEND_GAP = "message_send_gap";
    public static final String MESSAGE_SEND_GAP_DEFAULT = "200";
    public static final String SENT_MESSAGE_COUNT = "sent_message_count";
    public static final String SENT_MESSAGE_FLAG = "sent_message_flag";
    public static final String WHATSAPP = "whatsapp";
    public static final String WHATSAPP_BUSINESS_DEFAULT = "com.whatsapp.w4b";
    public static final String WHATSAPP_COUNT = "whatsapp_count";
    public static final String WHATSAPP_DEFAULT = "com.whatsapp";
}
