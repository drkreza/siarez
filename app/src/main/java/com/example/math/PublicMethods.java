package com.example.math;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Build;
import android.provider.Settings;
import android.text.Html;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import javax.crypto.Cipher;
import javax.crypto.CipherOutputStream;



public class PublicMethods {

    static final byte PAD = (byte) '=';



    public static String getAndroidID(Context context){
        return Settings.Secure.getString(context.getContentResolver(),
                Settings.Secure.ANDROID_ID);
    }
    public static String getAndroidApi(){
        return String.valueOf(Build.VERSION.SDK_INT);
    }
    public static String getAppVersion(Context context){
        String version = "0.0.0";
        try {
            PackageInfo pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            version = pInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return version;
    }
    public static void hideKeyboard(Activity activity) {
        try {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(activity.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
        } catch (Exception e) {

        }
    }
    public static void showKeyboard(Activity activity, EditText editText) {
        try {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
        } catch (Exception e) {}
    }
    public static String getServerPublicKey(String key){
        return key.replace("-----BEGIN PUBLIC KEY-----", "").replace("\n-----END PUBLIC KEY-----", "").replace("\n","");
    }
    public static String getServerPrivateKey(String key){
        return key.replace("-----BEGIN PRIVATE KEY-----", "").replace("\n-----END PRIVATE KEY-----", "").replace("\n","");
    }
    public static String convert(String price){

        try {
            String newPrice = price.trim();
            NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.US);
            String numberAsString = numberFormat.format(Integer.parseInt(newPrice));
            return numberAsString;
        }catch (Exception e){
            return price;
        }

    }
    public static String convertDec(String price)throws JSONException{
        String newPrice = price.trim();
        NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.US);
        numberFormat.setMaximumFractionDigits(4);
        numberFormat.setMinimumFractionDigits(4);
        double v = Double.parseDouble(newPrice);
        String numberAsString = numberFormat.format(v);
        return numberAsString;
    }
    public static String removeZero(String price)throws JSONException{
        String newPrice = price.indexOf(".") < 0 ? price : price.replaceAll("0*$", "").replaceAll("\\.$", "");
        return newPrice;
    }
    public static Spanned loadHtmlText(String html){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return Html.fromHtml(html, Html.FROM_HTML_MODE_COMPACT);
        } else {
            return Html.fromHtml(html);
        }
    }

    public static boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }

    public static Bitmap getBitmapFromView(View view){
        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas c =new Canvas(bitmap);
        view.layout(view.getLeft(), view.getTop(), view.getRight(), view.getBottom());
        view.draw(c);
        return bitmap;
    }
    public static boolean isProbablyPersian(String s) {
        for (int i = 0; i < s.length();) {
            int c = s.codePointAt(i);
            if (c >= 0x0600 && c <= 0x06E0)
                return true;
            i += Character.charCount(c);
        }
        return false;
    }
    public static String verifyInstallerId(Context context) {
        try {
            // A list with valid installers package name
            List<String> validInstallers = new ArrayList<>(Arrays.asList("com.android.vending", "com.google.android.feedback"));

            // The package name of the app that has installed your app
            final String installer = context.getPackageManager().getInstallerPackageName(context.getPackageName());

            // true if your app has been downloaded from Play Store
            return ""+ (installer != null && validInstallers.contains(installer));
        }catch (Exception e){
            return "catch";
        }

    }

    public static String getPackageInstaller(Context context){
        try {
            return context.getPackageManager().getInstallerPackageName(context.getPackageName());
        }catch (Exception e){
            return "getPackageInstallerCatch";
        }
    }


    public static byte[] readFileToByteArray(File file){
        FileInputStream fis = null;
        // Creating a byte array using the length of the file
        // file.length returns long which is cast to int
        byte[] bArray = new byte[(int) file.length()];
        try{
            fis = new FileInputStream(file);
            fis.read(bArray);
            fis.close();

        }catch(IOException ioExp){
            ioExp.printStackTrace();
        }
        return bArray;
    }

    public static String _device;
    public static String getDeviceString() {
        if (_device != null)
            return _device;
        if (Build.MODEL.contains(Build.MANUFACTURER)) {
            _device = Build.MODEL;
            return _device;
        }
        _device = Build.MANUFACTURER + " " + Build.MODEL;
        return _device;
    }


    public static int[] gregorian_to_jalali(int gy, int gm, int gd){
        int[] g_d_m = {0,31,59,90,120,151,181,212,243,273,304,334};
        int jy;
        if(gy>1600){
            jy=979;
            gy-=1600;
        }else{
            jy=0;
            gy-=621;
        }
        int gy2 = (gm > 2)?(gy + 1):gy;
        int days = (365 * gy) + ((int)((gy2 + 3) / 4)) - ((int)((gy2 + 99) / 100)) + ((int)((gy2 + 399) / 400)) - 80 + gd + g_d_m[gm - 1];
        jy += 33 * ((int)(days / 12053));
        days %= 12053;
        jy += 4 * ((int)(days / 1461));
        days %= 1461;
        if(days > 365){
            jy+=(int)((days-1)/365);
            days=(days-1)%365;
        }
        int jm = (days < 186)?1 + (int)(days / 31):7 + (int)((days - 186) / 30);
        int jd = 1 + ((days < 186)?(days % 31):((days - 186) % 30));
        int[] out = {jy,jm,jd};
        return out;
    }

    public static String convertUtcDateToLocalTimeZone(String date){
        SimpleDateFormat utcFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        utcFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date dateeeee = null;
        try {
            dateeeee = utcFormat.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        utcFormat.setTimeZone(TimeZone.getDefault());
        return utcFormat.format(dateeeee);
    }

    public static String createKuknosIdCharacters = "<,*>";
    public static String sendToKuknosIdCharacters = "<,>";

    public static InputFilter createFilter = new InputFilter() {

        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            if (source != null && createKuknosIdCharacters.contains(("" + source))) {
                return "";
            }
            return null;
        }
    };

    public static InputFilter sentToIdFilter = new InputFilter() {

        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            if (source != null && sendToKuknosIdCharacters.contains(("" + source))) {
                return "";
            }
            return null;
        }
    };

    public static String mySubtract(Double a , Double b){
        BigDecimal subtract = new BigDecimal(a.toString()).subtract(new BigDecimal(b.toString()));
        return subtract.toPlainString();
    }


    public static int getNumberOfDecimalWithOutDot (String amount){
        int integerPlaces = amount.indexOf('.');
        if (integerPlaces == -1) {
            return 0;
        }else {
            int decimalPlaces = amount.length() - integerPlaces - 1;
            return decimalPlaces;
        }
    }


    public static String separateWithComma(double value) {
        String convertedValue = new DecimalFormat("#,###,###.###").format(value);
        return convertedValue;
    }


}
