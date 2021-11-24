package com.example.talentium

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.camera.core.CameraSelector
import androidx.camera.core.Preview
import androidx.camera.core.VideoCapture
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.talentium.constants.VideoConstants
import kotlinx.android.synthetic.main.fragment_video.*
import java.io.File
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [VideoFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class VideoFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null


    private var videoCapture: VideoCapture? = null
    private lateinit var outputDirectory: File

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_video, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        outputDirectory = getOutputDirectory()

        if (allPermissionGranted()) {
            startCamera()
        } else {
            Log.i("permission", "request")
            ActivityCompat.requestPermissions(
                requireActivity(),
                VideoConstants.REQUIRED_PERMISSIONS,
                VideoConstants.REQUEST_CODE_PERMISSION
            );


        }
        /*  btn_record.setOnClickListener {
              takeVideo()
          }*/
        val videoCapture = videoCapture ?: return
        val videoFile = File(
            outputDirectory,
            SimpleDateFormat(
                VideoConstants.FILE_NAME_FORMAT,
                Locale.getDefault()
            ).format(System.currentTimeMillis()) + ".mp4"
        )

        val outputOption = VideoCapture.OutputFileOptions.Builder(videoFile).build()

        btn_record.setOnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                btn_record.setBackgroundColor(Color.GREEN)

                videoCapture.startRecording(videoFile, object :VideoCapture.OnVideoSavedCallback{
                    override fun onVideoSaved(file: File?) {
                        Log.i(tag, "Video File : $file")
                    }

                    override fun onError(
                        useCaseError: VideoCapture.UseCaseError?,
                        message: String?,
                        cause: Throwable?
                    ) {
                        Log.i(tag, "Video Error: $message")
                    }

                })
            }else if(event.action==MotionEvent.ACTION_UP){
                btn_record.setBackgroundColor(Color.RED)
                videoCapture.stopRecording()
            }
            false


        }
    }

    private fun getOutputDirectory(): File {
        val mediaDir = requireActivity().externalMediaDirs.firstOrNull()?.let { mFile ->
            File(mFile, resources.getString(R.string.app_name)).apply {
                mkdirs()
            }
        }
        return if (mediaDir != null && mediaDir.exists())
            mediaDir else requireActivity().filesDir
    }

    private fun takeVideo() {


        /* videoCapture.startRecording(
             outputOption,ContextCompat.getMainExecutor(requireContext()),object :VideoCapture.OnVideoSavedCallback{
                 override fun onVideoSaved(outputFileResults: VideoCapture.OutputFileResults) {
                     TODO("Not yet implemented")
                 }

                 override fun onError(videoCaptureError: Int, message: String, cause: Throwable?) {
                     TODO("Not yet implemented")
                 }

             }
         )*/


    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())
        cameraProviderFuture.addListener({
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()
            val preview = Preview.Builder().build()
                .also { mPreview -> mPreview.setSurfaceProvider(videoView.surfaceProvider) }

            videoCapture = VideoCapture.Builder().build()

            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(
                    this, cameraSelector, preview, videoCapture
                )


            } catch (e: Exception) {
                Log.d(VideoConstants.TAG, "start Camera fail ! ", e)
            }
        }, ContextCompat.getMainExecutor(requireContext()))
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        if (requestCode == VideoConstants.REQUEST_CODE_PERMISSION) {
            if (allPermissionGranted()) {

                Toast.makeText(requireContext(), "Permission granted", Toast.LENGTH_SHORT)
                    .show()
                startCamera()

            } else {
                Toast.makeText(requireContext(), "Permission not granted", Toast.LENGTH_SHORT)
                    .show()

            }
        }
    }

    private fun allPermissionGranted() = VideoConstants.REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(requireContext(), it) == PackageManager.PERMISSION_GRANTED
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment VideoFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            VideoFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }


}