package com.example.facedetect

import android.content.res.AssetManager
import android.view.Surface

class SCRFDNcnn {
    external fun loadModel(mgr: AssetManager?, modelid: Int, cpugpu: Int,dir:String): Boolean

    external fun openCamera(facing: Int): Boolean

    external fun closeCamera(): Boolean

    external fun setOutputWindow(surface: Surface?): Boolean

    companion object {
        init {
            System.loadLibrary("scrfdncnn")
        }
    }
}
