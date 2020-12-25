package com.example.math;

import android.content.Context;
import android.widget.Toast;

public class MyToast {

    public static void showMessage(Context context,String message){
        try {
            Toast.makeText(context,message , Toast.LENGTH_LONG).show();
        }catch (Exception e){

        }
    }
}
