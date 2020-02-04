package com.example.drinkshop.Utils;

import android.os.Looper;

import androidx.annotation.Nullable;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import android.os.Handler;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okio.BufferedSink;

public class ProgressRequestBody extends RequestBody {

    private File file ;
    private static final int DEFAULT_BUFFER_SIZE = 4096 ;
    private UploadCallBack listner;

    public ProgressRequestBody(File file , UploadCallBack listner) {
        this.file = file;
        this.listner =  listner;
    }

    @Override
    public long contentLength() throws IOException {
        return file.length();
    }

    @Nullable
    @Override
    public MediaType contentType() {
        return MediaType.parse("image/*");
    }

    @Override
    public void writeTo(BufferedSink sink) throws IOException {
        long fileLength = file.length();
        byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
        FileInputStream in = new FileInputStream(file);
        long uploaded = 0 ;
        try{
            int read;
            Handler handler = new Handler(Looper.getMainLooper());
            while ((read = in.read(buffer)) != -1){
                handler.post(new ProgressUpdater(uploaded,fileLength));
                uploaded+=read;
                sink.write(buffer,0,read);

            }

        }finally {
            {
                in.close();
            }
        }

    }

    private class ProgressUpdater implements Runnable {
        private long uploaded,filelength;
        public ProgressUpdater(long uploaded, long fileLength) {
            this.filelength = fileLength;
            this.uploaded = uploaded;
        }

        @Override
        public void run() {
            listner.onProgressUpdate((int)(100*uploaded/filelength));
        }
    }
}
