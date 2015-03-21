package com.inftel.isn.request;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.AsyncTask;
import android.widget.Toast;

import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.DropboxAPI.UploadRequest;
import com.dropbox.client2.ProgressListener;
import com.inftel.isn.activity.CreateGroupActivity;

import java.io.File;
import java.io.FileInputStream;

public class UploadPicture extends AsyncTask<Void, Long, Boolean> {

    private DropboxAPI<?> mApi;
    private String mPath;
    private File mFile;
    private UploadRequest mRequest;
    public CreateGroupActivity mContext;
    private final ProgressDialog mDialog;

    public UploadPicture(CreateGroupActivity context, DropboxAPI<?> api, File file) {
        mContext = context;
        mApi = api;
        mPath = "/images/";
        mFile = file;

        mDialog = new ProgressDialog(context);
        mDialog.setMax(100);
        mDialog.setMessage("Uploading " + file.getName());
        mDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        mDialog.setProgress(0);
        mDialog.setButton("Cancel", new OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                mRequest.abort();
            }
        });
        mDialog.show();
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        try {
            FileInputStream fis = new FileInputStream(mFile);
            String path = mPath + mFile.getName();
            mRequest = mApi.putFileOverwriteRequest(path, fis, mFile.length(),
                    new ProgressListener() {
                        @Override
                        public long progressInterval() {
                            return 500;
                        }
                        @Override
                        public void onProgress(long bytes, long total) {
                            publishProgress(bytes);
                        }
                    });
            if (mRequest != null) {
                mRequest.upload();
                return true;
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

    @Override
    protected void onProgressUpdate(Long... progress) {
        int percent = (int)(100.0*(double)progress[0]/mFile.length() + 0.5);
        mDialog.setProgress(percent);
    }

    @Override
    protected void onPostExecute(Boolean result) {
        mDialog.dismiss();
        if (result) {
            showToast("Image successfully uploaded");
        } else {
            showToast("Image not uploaded");
        }

        //Funcion provisional de descarga desde Dropbox, mFIle.getName() es lo que debo guardar en la BD
        mContext.downloadImageDropbox(mFile.getName());
        mFile.delete();
    }

    private void showToast(String msg) {
        Toast error = Toast.makeText(mContext, msg, Toast.LENGTH_LONG);
        error.show();
    }
}
