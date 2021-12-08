package com.example.talentium

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Activity.RESULT_CANCELED
import android.app.Activity.RESULT_OK
import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.webkit.PermissionRequest
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.camera.core.CameraSelector
import androidx.camera.core.CameraX
import androidx.camera.core.Preview
import androidx.camera.core.VideoCapture
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.talentium.constants.VideoConstants
import kotlinx.android.synthetic.main.fragment_video.*
import ly.img.android.pesdk.VideoEditorSettingsList
import ly.img.android.pesdk.assets.filter.basic.FilterPackBasic
import ly.img.android.pesdk.assets.font.basic.FontPackBasic
import ly.img.android.pesdk.assets.frame.basic.FramePackBasic
import ly.img.android.pesdk.assets.overlay.basic.OverlayPackBasic
import ly.img.android.pesdk.assets.sticker.emoticons.StickerPackEmoticons
import ly.img.android.pesdk.assets.sticker.shapes.StickerPackShapes
import ly.img.android.pesdk.backend.model.EditorSDKResult
import ly.img.android.pesdk.backend.model.state.LoadSettings
import ly.img.android.pesdk.backend.model.state.VideoEditorSaveSettings
import ly.img.android.pesdk.ui.activity.VideoEditorBuilder
import ly.img.android.pesdk.ui.model.state.*
import ly.img.android.serializer._3.IMGLYFileWriter
import java.io.File
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.Executors

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
private var lensFacing = CameraSelector.DEFAULT_FRONT_CAMERA

/**
 * A simple [Fragment] subclass.
 * Use the [VideoFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class VideoFragment : Fragment() , ly.img.android.pesdk.ui.utils.PermissionRequest.Response {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    companion object {
        const val VESDK_RESULT = 1
        const val GALLERY_RESULT = 2
    }

    private var videoCapture: VideoCapture? = null
    private lateinit var outputDirectory: File
    var camera=false;
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

    @SuppressLint("RestrictedApi", "ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        switchCamera.setOnClickListener {
            flipCamera()
        }
        outputDirectory = getOutputDirectory()
        openVideoGallery.setOnClickListener {
            openSystemGalleryToSelectAVideo()

        }
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
        val executor = Executors.newSingleThreadExecutor()
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

                videoCapture?.startRecording(outputOption, executor,object :VideoCapture.OnVideoSavedCallback{

                    override fun onVideoSaved(outputFileResults: VideoCapture.OutputFileResults) {
                        Log.i("video saved",outputFileResults.savedUri.toString())

                        val intent = Intent(requireContext(), VideoPlayer::class.java)
                        intent.putExtra("video",outputFileResults.savedUri.toString())
                        startActivity(intent)
                        activity?.finish()
                        btn_record.setBackgroundColor(Color.BLUE)


                    }

                    override fun onError(
                        videoCaptureError: Int,
                        message: String,
                        cause: Throwable?
                    ) {
                        Log.i("video error","error $message")

                    }


                })
            }else if(event.action==MotionEvent.ACTION_UP){
                btn_record.setBackgroundColor(Color.RED)
                videoCapture?.stopRecording()
            }
            false


        }
    }

    fun openSystemGalleryToSelectAVideo() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.setDataAndType(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, "video/*")

        try {
            startActivityForResult(intent, GALLERY_RESULT)
        } catch (ex: ActivityNotFoundException) {
            Toast.makeText(
                requireContext(),
                "No Gallery APP installed",
                Toast.LENGTH_LONG
            ).show()
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
    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
        super.onActivityResult(requestCode, resultCode, intent)

        if (resultCode == RESULT_OK && requestCode == GALLERY_RESULT) {
            // Open Editor with some uri in this case with an video selected from the system gallery.
            openEditor(intent?.data)

        } else if (resultCode == RESULT_OK && requestCode == VESDK_RESULT) {
            // Editor has saved an Video.
            val data = EditorSDKResult(intent!!)

            Log.i("VESDK", "Source video is located here ${data.sourceUri}")
            Log.i("VESDK", "Result video is located here ${data.resultUri}")

            // TODO: Do something with the result video

            // OPTIONAL: read the latest state to save it as a serialisation
            val lastState = data.settingsList
            try {
                IMGLYFileWriter(lastState).writeJson(
                    File(
                      //  getExternalFilesDir(null),
                        "serialisationReadyToReadWithPESDKFileReader.json"
                    )
                )
            } catch (e: Exception) {
                e.printStackTrace()
            }

        } else if (resultCode == RESULT_CANCELED && requestCode == VESDK_RESULT) {
            // Editor was canceled
            val data = EditorSDKResult(intent!!)

            val sourceURI = data.sourceUri
            // TODO: Do something with the source...
        }
    }

    fun openEditor(inputSource: Uri?) {
        val settingsList = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            createVesdkSettingsList()
        } else {
            Toast.makeText(requireContext(), "Video support needs Android 4.3", Toast.LENGTH_LONG).show()
            return
        }

        settingsList.configure<LoadSettings> {
            it.source = inputSource
        }

        VideoEditorBuilder(requireActivity())
            .setSettingsList(settingsList)
            .startActivityForResult(this, VESDK_RESULT)
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
    private var lensFacing = CameraSelector.DEFAULT_FRONT_CAMERA
    private fun flipCamera() {
        if (lensFacing == CameraSelector.DEFAULT_FRONT_CAMERA) lensFacing =
            CameraSelector.DEFAULT_BACK_CAMERA else if (lensFacing == CameraSelector.DEFAULT_BACK_CAMERA) lensFacing =
            CameraSelector.DEFAULT_FRONT_CAMERA
        startCamera()
    }
    private fun startCamera() {

        val cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())
        cameraProviderFuture.addListener({
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()
            val preview = Preview.Builder().build()
                .also { mPreview -> mPreview.setSurfaceProvider(videoView.surfaceProvider) }

            videoCapture = VideoCapture.Builder().build()


            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(
                    this, lensFacing, preview, videoCapture
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


    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    private fun createVesdkSettingsList() =
        VideoEditorSettingsList()
            .configure<UiConfigFilter> {
                it.setFilterList(FilterPackBasic.getFilterPack())
            }
            .configure<UiConfigText> {
                it.setFontList(FontPackBasic.getFontPack())
            }
            .configure<UiConfigFrame> {
                it.setFrameList(FramePackBasic.getFramePack())
            }
            .configure<UiConfigOverlay> {
                it.setOverlayList(OverlayPackBasic.getOverlayPack())
            }
            .configure<UiConfigSticker> {
                it.setStickerLists(
                    StickerPackEmoticons.getStickerCategory(),
                    StickerPackShapes.getStickerCategory()
                )
            }
            .configure<VideoEditorSaveSettings> {
                it.setOutputToGallery(Environment.DIRECTORY_DCIM)
            }
    override fun permissionGranted() {}

    override fun permissionDenied() {
        /* TODO: The Permission was rejected by the user. The Editor was not opened,
         * Show a hint to the user and try again. */
    }
}