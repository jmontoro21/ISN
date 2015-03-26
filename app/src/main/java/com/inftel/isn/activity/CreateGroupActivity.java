package com.inftel.isn.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.inftel.isn.R;
import com.inftel.isn.model.Group;
import com.inftel.isn.request.DownloadDropboxTask;
import com.inftel.isn.request.DownloadImageTask;
import com.inftel.isn.request.UploadDropboxTask;
import com.inftel.isn.utility.DropboxConnection;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CreateGroupActivity extends Activity implements DownloadDropboxTask.OnAsyncRequestComplete{
    EditText editText;
    ImageView imageView;
    DropboxConnection dc;
    Button button;
    View loadingPanel;
    View imagePanel;
    File outFile;
    String urlImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group);

        editText = (EditText) findViewById(R.id.editText);
        imageView = (ImageView) findViewById(R.id.imageView);
        loadingPanel = findViewById(R.id.loadingPanel);
        imagePanel = findViewById(R.id.imagePanel);
        button = (Button) findViewById(R.id.button);
        downloading(false);

        dc = new DropboxConnection(this);
        dc.connect();

        urlImage= "http://png-1.findicons.com/files/icons/131/software/48/user_group.png";

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.toString().equals("")) {
                    button.setEnabled(false);
                } else {
                    button.setEnabled(true);
                }
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    @Override
    public void onResume(){
        super.onResume();
        dc.resume();
    }

    public void setUrlImage(String urlImage){
        this.urlImage = urlImage;
    }

    public void openAddPhoto(View v) {

        String[] addPhoto=new String[]{ "Cámara", "Galería", "URL"};
        AlertDialog.Builder dialog=new AlertDialog.Builder(this);
        dialog.setTitle("Selecciona entre:");
        dialog.setItems(addPhoto,new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int id) {
                if(id==0){
                    Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                    Date date = new Date();
                    DateFormat df = new SimpleDateFormat("kk-mm-ss");
                    String newPicFile = "ISN-Inftel_Camera_" + df.format(date) + ".jpg";

                    String outPath = Environment.getExternalStorageDirectory() + "/imgInftel/" + newPicFile;
                    outFile = new File(outPath);

                    takePicture.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(outFile));

                    startActivityForResult(takePicture, 0);
                }
                if(id==1){
                    Intent pickPhoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(pickPhoto , 1);
                }
                if(id==2){
                    LayoutInflater li = LayoutInflater.from(CreateGroupActivity.this);
                    View promptsView = li.inflate(R.layout.prompts, null);

                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(CreateGroupActivity.this);

                    // set prompts.xml to alertdialog builder
                    alertDialogBuilder.setView(promptsView);

                    final EditText userInput = (EditText) promptsView.findViewById(R.id.editTextDialogUserInput);
                    urlImage = userInput.getText().toString();

                    // set dialog message
                    alertDialogBuilder
                            .setCancelable(false)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,int id) {
                                    new DownloadImageTask(imageView).execute(urlImage);
                                }
                            })
                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });
                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
                }
            }
        });

        dialog.setNeutralButton("Cancelar",new android.content.DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }});
        dialog.show();
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        switch(requestCode) {
            case 0:
                if(resultCode == RESULT_OK) {
                    /*Bundle extras = imageReturnedIntent.getExtras();
                    Bitmap imageBitmap = (Bitmap) extras.get("data");
                    imageView.setImageBitmap(imageBitmap);*/
                    uploadPictureDropbox(outFile);
                }
                break;
            case 1:
                if(resultCode == RESULT_OK){
                    try {
                        Uri selectedImage = imageReturnedIntent.getData();

                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);

                        Date date = new Date();
                        DateFormat df = new SimpleDateFormat("kk-mm-ss");
                        String newPicFile = "ISN-Inftel_Gallery_" + df.format(date) + ".jpg";

                        String file_path = Environment.getExternalStorageDirectory() + "/imgInftel/";
                        File dir = new File(file_path);
                        if (!dir.exists()) dir.mkdirs();

                        File file = new File(dir, newPicFile);
                        FileOutputStream fOut = new FileOutputStream(file);
                        bitmap.compress(Bitmap.CompressFormat.PNG, 85, fOut);
                        fOut.flush();
                        fOut.close();
                        //imageView.setImageURI(selectedImage);
                        uploadPictureDropbox(file);
                    } catch (Exception e){
                        e.printStackTrace();
                    }
                }
                break;
        }
    }

    public void downloading(boolean dl) {
        if(dl){
            loadingPanel.setVisibility(View.VISIBLE);
            imagePanel.setVisibility(View.GONE);
        } else {
            loadingPanel.setVisibility(View.GONE);
            imagePanel.setVisibility(View.VISIBLE);
        }
    }

    public void downloadImageDropbox(String imageName) {
        downloading(true);
        DownloadDropboxTask list = new DownloadDropboxTask(this, dc.getDropboxApi(), imageView);
        list.execute(imageName);
    }

    @Override
    public void asyncResponse(File result) {
        Picasso.with(this).load(result).into(imageView);
        downloading(false);
    }

    private void uploadPictureDropbox(File image) {
        UploadDropboxTask upload = new UploadDropboxTask(this, dc.getDropboxApi(), image);
        upload.execute();
    }

    public void createGroup(View v) {
        Group group = new Group();
        group.setName(editText.getText().toString());
        if(urlImage!=null) group.setImageUrl(urlImage);

        SharedPreferences prefs = getSharedPreferences("MYPREFERENCES", Context.MODE_PRIVATE);
        String emailLogged = prefs.getString(LoginGoogleActivity.USER_KEY, "");
        group.setAdmin(emailLogged);

        Gson gson = new Gson();
        Intent intent = new Intent(this, UserSearchActivity.class);
        intent.putExtra("group", gson.toJson(group));
        startActivity(intent);
    }
}
