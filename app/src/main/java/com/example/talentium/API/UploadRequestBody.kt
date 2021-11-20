package com.example.talentium.API

import android.os.Looper
import okhttp3.Callback
import okhttp3.MediaType
import okhttp3.RequestBody
import okio.BufferedSink
import java.io.File
import java.io.FileInputStream

class UploadRequestBody(val file: File, val contentType:String,val callback:UploadCallBack):
    RequestBody() {
    interface  UploadCallBack{
        fun onProgressUpdate(percentage : Int)
    }

    inner class progressupdate(val uploaded:Long,val total:Long):Runnable{
        override fun run() {
            callback.onProgressUpdate((100*uploaded/total).toInt())
        }

    }
    //override fun contentType()= MediaType.parse("$contentType/*")
    override fun contentLength()=file.length()
    override fun contentType(): MediaType? {
        TODO("Not yet implemented")
    }

    override fun writeTo(sink: BufferedSink) {
        val length = file.length()
        val buffer = ByteArray(DEFAULT_BUFFER_SIZE)
        val fileinputstream = FileInputStream(file)
        var uploaded = 0L
        fileinputstream.use { inputstream->
            var read:Int
            val handler = android.os.Handler(Looper.getMainLooper())
            while (inputstream.read(buffer).also {
                    read = it
                }!= -1){
            handler.post(progressupdate(uploaded,length))
                uploaded+=read
                sink.write(buffer,0,read)            }
        }

    }
    companion object{
        private const val DEFAULT_BUFFER_SIZE = 1048
    }
}