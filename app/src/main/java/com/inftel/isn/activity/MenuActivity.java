package com.inftel.isn.activity;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.inftel.isn.R;
import com.inftel.isn.model.User;
import com.inftel.isn.request.UploadQRDropboxTask;
import com.inftel.isn.utility.DropboxConnection;
import com.inftel.isn.utility.PageAdapterFragment;

import java.io.File;
import java.io.FileOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MenuActivity extends FragmentActivity implements ActionBar.TabListener {
    ActionBar actionbar;
    private ViewPager viewpager;
    private User user = new User();
    private PageAdapterFragment ft;
    static final String ACTION_SCAN = "com.google.zxing.client.android.SCAN";
    private DropboxConnection dc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        viewpager = (ViewPager) findViewById(R.id.pager);

        SharedPreferences prefs = this.getSharedPreferences("MYPREFERENCES", Context.MODE_PRIVATE);
        String emailLogin="";
        // Email del usuario logueado
        if (prefs.contains(LoginGoogleActivity.USER_KEY)) {
            emailLogin = prefs.getString(LoginGoogleActivity.USER_KEY, "");
        }

        ft = new PageAdapterFragment(getSupportFragmentManager(),emailLogin);

        actionbar = getActionBar();
        viewpager.setAdapter(ft);
        actionbar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        actionbar.addTab(actionbar.newTab().setText("Inicio").setTabListener(this));
        actionbar.addTab(actionbar.newTab().setText("Grupos").setTabListener(this));
        actionbar.addTab(actionbar.newTab().setText("Notas").setTabListener(this));
        viewpager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int arg0) {
                actionbar.setSelectedNavigationItem(arg0);
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
            }
        });

        dc = new DropboxConnection(this);
        dc.connect();
    }

    @Override
    public void onResume(){
        super.onResume();
        dc.resume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuItem search = menu.add(Menu.NONE, R.id.action_search, 1, "Search");
        MenuItemCompat.setShowAsAction(search, MenuItem.SHOW_AS_ACTION_ALWAYS);
        search.setIcon(R.drawable.search);
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {


            case R.id.action_search:
                intent = new Intent(this, FinderActivity.class);
                startActivity(intent);
                return true;

            case R.id.createGroup:
                intent = new Intent(this, CreateGroupActivity.class);
                startActivity(intent);
                return true;

            case R.id.Logout:

                intent = new Intent(this, LogoutActivity.class);
                startActivity(intent);


                return true;

            case R.id.seguidos:
                intent = new Intent(this, FollowedActivity.class);
                intent.putExtra("email", user.getEmail());
                startActivity(intent);
                return true;

            case R.id.siguiendo:
                intent = new Intent(this, FollowerActivity.class);
                intent.putExtra("user", user);
                startActivity(intent);
                return true;

            case R.id.perfil:
                intent = new Intent(this, ListPublicCommentActivity.class);
                startActivity(intent);
                return true;

            case R.id.createQR:
                generateQR();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onTabSelected(Tab tab, FragmentTransaction ft) {
        viewpager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabReselected(Tab tab, FragmentTransaction ft) {
    }

    @Override
    public void onTabUnselected(Tab tab, FragmentTransaction ft) {
    }


    public void fab_home(View view) {
        Intent intent = new Intent(this, CreateCommentActivity.class);
        intent.putExtra(CreateCommentActivity.COMMENT_TYPE, "publico");
        startActivity(intent);
        Log.i("fab", "home");
    }


    public void fab_group(View view) {
        Intent intent = new Intent(this, CreateCommentActivity.class);
        intent.putExtra(CreateCommentActivity.COMMENT_TYPE, "grupo");
        startActivity(intent);
        Log.i("fab", "grupo");
    }

    public void fab_nota(View view) {
        Intent intent = new Intent(this, CreateCommentActivity.class);
        intent.putExtra(CreateCommentActivity.COMMENT_TYPE, "nota");
        startActivity(intent);
        Log.i("fab", "nota");
    }

    private void generateQR() {
        QRCodeWriter writer = new QRCodeWriter();
        try {
            SharedPreferences prefs = this.getSharedPreferences("MYPREFERENCES", Context.MODE_PRIVATE);
            String emailLogin = prefs.getString(LoginGoogleActivity.USER_KEY, "");

            BitMatrix bitMatrix = writer.encode(emailLogin, BarcodeFormat.QR_CODE, 512, 512);
            int width = bitMatrix.getWidth();
            int height = bitMatrix.getHeight();
            Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    bmp.setPixel(x, y, bitMatrix.get(x, y) ? Color.BLACK : Color.WHITE);
                }
            }

            Date date = new Date();
            DateFormat df = new SimpleDateFormat("kk-mm-ss");
            String newPicFile = "ISN-Inftel_QR_" + df.format(date) + ".jpg";

            String file_path = Environment.getExternalStorageDirectory() + "/imgInftel/";
            File dir = new File(file_path);
            if (!dir.exists()) dir.mkdirs();

            File file = new File(dir, newPicFile);
            FileOutputStream fOut = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.PNG, 85, fOut);
            fOut.flush();
            fOut.close();

            UploadQRDropboxTask upload = new UploadQRDropboxTask(this, dc.getDropboxApi(), file);
            upload.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static AlertDialog showDialog(final Activity act, CharSequence title, CharSequence message, CharSequence buttonYes, CharSequence buttonNo) {
        AlertDialog.Builder downloadDialog = new AlertDialog.Builder(act);
        downloadDialog.setTitle(title);
        downloadDialog.setMessage(message);
        downloadDialog.setPositiveButton(buttonYes, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                Uri uri = Uri.parse("market://details?id=" + "com.google.zxing.client.android");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                try {
                    act.startActivity(intent);
                } catch (ActivityNotFoundException e) {
                    e.printStackTrace();
                }
            }
        });
        downloadDialog.setNegativeButton(buttonNo, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        return downloadDialog.show();
    }

    //Devolución del programa
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {
                String loginEmail = intent.getStringExtra("SCAN_RESULT");
                //Buscar usuario por email en BBDD pasando "loginEmail".
                //Si alguno concuerda, logearse con dicho email
                //Si no, volver al LoginActivity
            }
        }
    }
}
