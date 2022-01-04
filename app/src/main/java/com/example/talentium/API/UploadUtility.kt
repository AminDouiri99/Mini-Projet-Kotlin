package com.mvp.handyopinion

import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Looper

import android.util.Log
import android.webkit.MimeTypeMap
import android.widget.ProgressBar
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.talentium.API.ApiInterface
import com.example.talentium.LandingActivity
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.asRequestBody
import okio.blackholeSink
import java.io.File
import java.io.FileInputStream
import java.io.IOException
import java.util.logging.Handler
import kotlin.coroutines.coroutineContext

class UploadUtility(activity: Activity, userId: String, videoId: String) {


    var activity = activity;
    var dialog: ProgressDialog? = null
    var serverURLPic: String = ApiInterface.BASE_URL + "api/users/changeprofile/pic/" + userId
    var serverURLVideo: String =
        ApiInterface.BASE_URL + "api/publication/upload/video/" + videoId + "/" + userId
    var serverUploadDirectoryPath: String = "https://handyopinion.com/tutorials/uploads/"
    val client = OkHttpClient()


    fun uploadFile(sourceFileUri: Uri, type: String) {
        val pathFromUri = URIPathHelper().getPath(activity, sourceFileUri)

        if (type == "video") {
            uploadFileVideo(File(pathFromUri), null, type)
        } else {
            uploadFile(File(pathFromUri), null, type)
        }
    }

    fun uploadFile(sourceFile: File, uploadedFileName: String? = null, type: String) {
        Thread {
            val mimeType = getMimeType(sourceFile);
            if (mimeType == null) {
                Log.e("file error", "Not able to get mime type")
                return@Thread
            }
            val fileName: String =
                if (uploadedFileName == null) sourceFile.name else uploadedFileName
            //toggleProgressDialog(true)


            val requestBody: RequestBody =
                MultipartBody.Builder().setType(MultipartBody.FORM)
                    .addFormDataPart(
                        "uploaded_file",
                        fileName,
                        sourceFile.asRequestBody(mimeType.toMediaTypeOrNull())
                    )
                    .build()
            Log.i("request body", requestBody.toString())

            if (type == "image") {
                val request = Request.Builder()
                    .method("POST", requestBody)
                    .url(serverURLPic)
                    .build()
                client.newCall(request).enqueue(object : Callback {
                    override fun onFailure(call: Call, e: IOException) {
                        // Handle this
                        Log.i("failed", e.toString())
                    }

                    override fun onResponse(call: Call, response: Response) {
                        // Handle this

                        Log.i("suucess", response.body.toString())
                    }
                })
            } else if (type == "video") {
                val request = Request.Builder()
                    .method("POST", requestBody)
                    .url(serverURLVideo)
                    .build()
                client.newCall(request).enqueue(object : Callback {
                    override fun onFailure(call: Call, e: IOException) {
                        // Handle this
                        Log.i("failed", e.toString())
                    }

                    override fun onResponse(call: Call, response: Response) {
                        // Handle this
                        Log.i("video success", response.toString())
                    }
                })
            }


            // toggleProgressDialog(false)
        }.start()
    }

    fun uploadFileVideo(
        sourceFile: File,
        uploadedFileName: String? = null,
        type: String

    ) {

        Thread {
            val mimeType = getMimeType(sourceFile);
            if (mimeType == null) {
                Log.e("file error", "Not able to get mime type")
                return@Thread
            }
            val fileName: String =
                if (uploadedFileName == null) sourceFile.name else uploadedFileName


            val requestBody: RequestBody =
                MultipartBody.Builder().setType(MultipartBody.FORM)
                    .addFormDataPart(
                        "uploaded_file",
                        fileName,
                        sourceFile.asRequestBody(mimeType.toMediaTypeOrNull())
                    )
                    .build()

// loader


            val request = Request.Builder()
                .method("POST", requestBody)
                .url(serverURLVideo)
                .build()


            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    // Handle this
                    Log.i("failed", e.toString())
                }

                override fun onResponse(call: Call, response: Response) {
                    // Handle this

                    showToast("Uploaded")
                    Log.i("video success", response.body.toString())

                    /*val intent =Intent(activity,LandingActivity::class.java)
                    activity.startActivity(intent)
                    activity.finish()*/

                }
            })
            //  toggleProgressDialog(false,progress_bar)
        }.start()
    }

    // url = file path or whatever suitable URL you want.
    fun getMimeType(file: File): String? {
        var type: String? = null
        val extension = MimeTypeMap.getFileExtensionFromUrl(file.path)
        if (extension != null) {
            type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension)
        }
        return type
    }

    fun showToast(message: String) {
        activity.runOnUiThread {
            Toast.makeText(activity, message, Toast.LENGTH_LONG).show()
        }
    }

    fun toggleProgressDialog(show: Boolean, progress_bar: ProgressBar, uploaded: Long) {
        /* activity.runOnUiThread {
             if (show) {
                 dialog = ProgressDialog.show(activity, "", "Uploading file...", true);
             } else {
                 dialog?.dismiss();
             }
         }*/
        activity.runOnUiThread {
            progress_bar.progress = uploaded.toInt()
        }
    }


    /* inner class Progress(
         private val uploaded: Long,
         private val total: Long
     ) : Runnable {
         override fun run() {
            callback.Progress((100 * uploaded / total).toInt())
         }
     }*/

    companion object {
        private const val DEFAULT_BUFFER_SIZE = 2048
    }

}