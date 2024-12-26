package com.example.facedetect

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.PixelFormat
import android.os.Bundle
import android.util.Log
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.view.View
import android.view.WindowManager
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat


class MainActivity : AppCompatActivity(), SurfaceHolder.Callback {


    companion object {
        const val REQUEST_CAMERA: Int = 100
    }

    private val scrfdncnn = SCRFDNcnn()
    private var facing = 0

    private var current_model = 0

    private var cameraView: SurfaceView? = null

    /**
     * Called when the activity is first created.
     */
    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main)

        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        cameraView = findViewById<View>(R.id.cameraview) as SurfaceView

        cameraView!!.holder.setFormat(PixelFormat.RGBA_8888)
        cameraView!!.holder.addCallback(this)

        val buttonSwitchCamera = findViewById<View>(R.id.buttonSwitchCamera) as Button
        buttonSwitchCamera.setOnClickListener {
            val new_facing = 1 - facing
            scrfdncnn.closeCamera()

            scrfdncnn.openCamera(new_facing)
            facing = new_facing
        }

        findViewById<View>(R.id.normalDetect).setOnClickListener {
            if (0 != current_model) {
                current_model = 0
                reload()
            }
        }

        findViewById<View>(R.id.kpsDetect).setOnClickListener {
            if (1 != current_model) {
                current_model = 1
                reload()
            }
        }

        reload()
    }

    private fun reload() {
        val ret_init = scrfdncnn.loadModel(assets, current_model, 0)
        if (!ret_init) {
            Log.e("MainActivity", "scrfdncnn loadModel failed")
        }
    }

    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
        scrfdncnn.setOutputWindow(holder.surface)
    }

    override fun surfaceCreated(holder: SurfaceHolder) {
    }

    override fun surfaceDestroyed(holder: SurfaceHolder) {
    }

    public override fun onResume() {
        super.onResume()

        if (ContextCompat.checkSelfPermission(
                applicationContext,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_DENIED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.CAMERA),
                REQUEST_CAMERA
            )
        }

        scrfdncnn.openCamera(facing)
    }

    public override fun onPause() {
        super.onPause()

        scrfdncnn.closeCamera()
    }

}
