package com.medandev.sspl2.packaging.kotak.detail

import android.Manifest
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Intent
import android.content.pm.PackageManager
import android.media.AudioManager
import android.media.ToneGenerator
import android.os.Bundle
import android.util.SparseArray
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.view.View
import android.view.ViewTreeObserver
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.vision.CameraSource
import com.google.android.gms.vision.Detector
import com.google.android.gms.vision.barcode.Barcode
import com.google.android.gms.vision.barcode.BarcodeDetector
import com.medandev.sspl2.databinding.ActivityScanBarcodeBinding
import java.io.IOException

class ScanBarcodeActivity : AppCompatActivity() {

    private lateinit var cameraView: SurfaceView
    private lateinit var barcodeInfo: TextView
    private lateinit var barcodeDetector: BarcodeDetector
    private lateinit var cameraSource: CameraSource
    private lateinit var  scannerLayout: LinearLayout
    private lateinit var scannerBar: View
    private lateinit var vto: ViewTreeObserver
    private lateinit var animator: ObjectAnimator

    private lateinit var binding: ActivityScanBarcodeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityScanBarcodeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        cameraView = findViewById(binding.qrCameraSurfaceView.id)
        barcodeInfo = findViewById(binding.tvScan.id)
        scannerLayout = findViewById(binding.qrScannerLayout.id)
        scannerBar = findViewById(binding.qrScannerBar.id)

        val titleName = intent.getStringExtra(TITLE).toString()
        binding.tvLabelQrscan.text = titleName

        vto = scannerLayout.viewTreeObserver
        vto.addOnGlobalLayoutListener(
            object : ViewTreeObserver.OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    scannerLayout.viewTreeObserver.removeOnGlobalLayoutListener(this)

                    animator = ObjectAnimator.ofFloat(
                        scannerBar, "translationY",
                        scannerLayout.y - 320,
                        (scannerLayout.y - 320 + scannerLayout.height)
                    )

                    animator.repeatMode = ValueAnimator.REVERSE
                    animator.repeatCount = ValueAnimator.INFINITE
                    animator.interpolator = AccelerateDecelerateInterpolator()
                    animator.duration = 3000
                    animator.start()
                }
            }
        )

        barcodeDetector = BarcodeDetector.Builder(this)
            .setBarcodeFormats(Barcode.ALL_FORMATS)
            .build()

        cameraSource = CameraSource.Builder(this, barcodeDetector)
            .setFacing(CameraSource.CAMERA_FACING_BACK)
            .setAutoFocusEnabled(true)
            .build()

        cameraView.holder.addCallback(object : SurfaceHolder.Callback {
            override fun surfaceCreated(holder: SurfaceHolder) {
                try {
                    if (ActivityCompat.checkSelfPermission(
                            this@ScanBarcodeActivity,
                            Manifest.permission.CAMERA
                        ) != PackageManager.PERMISSION_GRANTED
                    ) {
                        cameraSource.start(cameraView.holder)
                    } else {
                        cameraSource.start(cameraView.holder)
                    }
                } catch (ie: IOException) {
                    Toast.makeText(
                        this@ScanBarcodeActivity,
                        "Error : " + ie.toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun surfaceChanged(
                holder: SurfaceHolder,
                format: Int,
                width: Int,
                height: Int
            ) {
                //no action
            }

            override fun surfaceDestroyed(holder: SurfaceHolder) {
                //no action
            }
        })

        barcodeDetector.setProcessor(object : Detector.Processor<Barcode?> {
            override fun release() {
                //
            }

            override fun receiveDetections(detections: Detector.Detections<Barcode?>) {
                val barcodeResult: SparseArray<Barcode?> = detections.detectedItems


                runOnUiThread {
                    if (barcodeResult.size() != 0) {
                        val toneGen1 = ToneGenerator(AudioManager.STREAM_MUSIC, 100)
                        toneGen1.startTone(ToneGenerator.TONE_CDMA_PIP, 150)
                        cameraSource.stop()

                        val mIntent = Intent()
                        mIntent.putExtra("strBarcode", barcodeResult.valueAt(0)?.displayValue.toString())
                        setResult(RESULT_OK, mIntent)
                        finish()
                    } else {
                        barcodeInfo.setText("Scanning...")
                    }
                }
            }
        })
    }

    override fun onBackPressed() {
        super.onBackPressed()
        cameraSource.stop()
        finish()
    }

    companion object {
        const val TITLE = "title"
    }
}