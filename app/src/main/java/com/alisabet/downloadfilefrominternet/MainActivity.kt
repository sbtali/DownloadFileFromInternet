package com.alisabet.downloadfilefrominternet

import android.Manifest
import android.content.DialogInterface
import android.content.pm.PackageManager
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.view.View
import android.widget.Button
import android.widget.Toast
import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import android.os.Environment

class MainActivity : AppCompatActivity() {

    //1.get permissions
    //2.define download function

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (checkPermission()) {
            Toast.makeText(this, "Permission already granted.", Toast.LENGTH_LONG).show()
        } else {
            Toast.makeText(this, "Please request permission.", Toast.LENGTH_LONG).show()
        }

        if (!checkPermission()) {
            requestPermission()
        } else {
            Toast.makeText(this, "Permission already granted.", Toast.LENGTH_LONG).show()
        }

        val button = findViewById<Button>(R.id.button)
        button.setOnClickListener(View.OnClickListener {
            download("http://dl.nex1music.ir/1397/01/03/Amirhossein%20Miri%20-%20Bitabam%20[128].mp3?time=1521806414",
                    "title", "description", "filename.mp3")
        })

    }

    private fun checkPermission(): Boolean {
        val result = ContextCompat.checkSelfPermission(applicationContext, Manifest.permission.WRITE_EXTERNAL_STORAGE)
        val result1 = ContextCompat.checkSelfPermission(applicationContext, Manifest.permission.READ_EXTERNAL_STORAGE)
        return result == PackageManager.PERMISSION_GRANTED && result1 == PackageManager.PERMISSION_GRANTED
    }

    private fun requestPermission() {
        ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE), 100)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            100 -> if (grantResults.size > 0) {

                val locationAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED
                val cameraAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED

                if (locationAccepted && cameraAccepted){
                    Toast.makeText(this, "Permission Granted, Now you can access", Toast.LENGTH_LONG).show()
                }

                else {
                    Toast.makeText(this, "Permission Denied, You cannot access", Toast.LENGTH_LONG).show()
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                            showMessageOKCancel("You need to allow access to both the permissions",
                                    DialogInterface.OnClickListener { dialog, which ->
                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                            requestPermissions(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                                    Manifest.permission.READ_EXTERNAL_STORAGE),
                                                    100)
                                        }
                                    })
                            return
                        }
                    }

                }
            }
        }
    }

    private fun showMessageOKCancel(message: String, okListener: DialogInterface.OnClickListener) {
        AlertDialog.Builder(this@MainActivity)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show()
    }

    private fun download(url: String,title: String, description: String, filename: String){
        val request = DownloadManager.Request(Uri.parse(url))
        request.setTitle(title)
        request.setDescription(description)
        request.allowScanningByMediaScanner()
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, filename)
        val manager = getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        manager.enqueue(request)
    }

}
