package com.inftel.isn.request;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.ImageView;

import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.exception.DropboxException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

public class ImageDropboxTask extends AsyncTask<String, Void, File> {
    private DropboxAPI<?> dropbox;
    private String path;
    private ImageView imageview;
    private Context context;
    OnAsyncRequestComplete caller;

    public ImageDropboxTask(Context context, DropboxAPI<?> dropbox, ImageView imageview) {
        this.dropbox = dropbox;
        this.imageview = imageview;
        this.context = context;
        path = "/images/";
        this.caller = (OnAsyncRequestComplete) context;
    }

    public interface OnAsyncRequestComplete {
        public void asyncResponse(File result);
    }

    @Override
    protected File doInBackground(String... params) {
        File file=null;
        try {
            file = new File(context.getFilesDir().getPath() + "/" + params[0]);
            FileOutputStream outputStream = new FileOutputStream(file);
            dropbox.getFile(path + params[0], null, outputStream, null);
           /* Recorrer el directorio:
            DropboxAPI.Entry directory = dropbox.metadata(path, 100, null, true, null);
            for (DropboxAPI.Entry entry : directory.contents) {
                if(entry.fileName().equals(params[0])) {
                    file = new File(context.getFilesDir().getPath() + "/" + entry.fileName());
                    FileOutputStream outputStream = new FileOutputStream(file);
                    dropbox.getFile(entry.path, null, outputStream, null);
                }
            }*/
        } catch (DropboxException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return file;
    }

    @Override
    protected void onPostExecute(File result) {
        caller.asyncResponse(result);
    }

}