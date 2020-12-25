package com.example.math.activites

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AlertDialog.*
import com.example.math.MyToast
import com.example.math.R
import com.example.math.main.MainActivity
import com.example.math.retrofit.mymodels.GetVersionCallback
import com.example.math.retrofit.mymodels.GetVersionResult
import com.example.math.retrofit.mymodels.Server
import kotlinx.android.synthetic.main.dialog_update_version.view.*

class SplashActivity : AppCompatActivity() {
    private var isActivityLive = true
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        hideStatusAndNavigationBar()
        Server.getVersion(this, object : GetVersionCallback {
            override fun callback(isOk: Boolean,data : GetVersionResult?, message: String) {
                if (isOk){
                    Log.i("MyError","1")
                    manageUpdate(data)
                }else{
                    this@SplashActivity?.let {
                        MyToast.showMessage(it,message)
                    }
                }
            }
        })
    }

    override fun onPause() {
        super.onPause()
        isActivityLive = false
    }

    override fun onRestart() {
        super.onRestart()
        isActivityLive = true
    }

    private fun manageUpdate(data : GetVersionResult?) {
        if (data?.forceUpdate!! || data?.showUpdate!!) {
            showDialogUpdate(data)
        } else {
            goNextPage()
        }
    }

    private fun showDialogUpdate(data: GetVersionResult) {
        this@SplashActivity?.let {
            if (isActivityLive) {
                var builder = Builder(this)
                var inflater = getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater
                var rootView = inflater.inflate(R.layout.dialog_update_version, null)
                builder.setView(rootView)
                builder.create()
                var show = builder.show();
                show.getWindow()?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                show.setCancelable(false)
                //var update_dialog_title: TextView = rootView.findViewById(R.id.update_dialog_title)
                rootView.update_dialog_title.text = getString(R.string.new_version_is_available)

                if (data?.forceUpdate!!) {
                    rootView?.update_dialog_cancel_button?.setText(R.string.exit)
                } else {
                    rootView?.update_dialog_cancel_button?.setText(R.string.skip)
                }

                rootView?.update_dialog_cancel_button?.setOnClickListener {
                    show.dismiss()


                    if (data.forceUpdate!!) {
                        onBackPressed()
                    } else if (data.showUpdate!!) {
                        goNextPage()
                    } else {
                        goNextPage()
                    }


                }

                rootView?.update_dialog_download_button?.setOnClickListener {
                    show.dismiss()
                    try{
                        var i = Intent(Intent.ACTION_VIEW)
                        i.setData(Uri.parse(data.link))
                        startActivity(i)
                    }catch (e:Exception){}


                }
            }
        }
    }

    private fun goNextPage() {
        Log.i("MyError","go")
        startActivity(MainActivity.getInstanse(this))
        finish()
    }
    private fun hideStatusAndNavigationBar() {
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                // Set the content to appear under the system bars so that the
                // content doesn't resize when the system bars hide and show.
                /* or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                 or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                 or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN*/
                // Hide the nav bar and status bar
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_FULLSCREEN)
    }
}
