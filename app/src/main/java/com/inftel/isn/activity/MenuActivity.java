package com.inftel.isn.activity;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import com.inftel.isn.R;
import com.inftel.isn.model.User;
import com.inftel.isn.utility.PageAdapterFragment;

public class MenuActivity extends FragmentActivity implements ActionBar.TabListener {
    ActionBar actionbar;
    private User user = new User();
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    ViewPager viewpager;
    PageAdapterFragment ft;
    static final String ACTION_SCAN = "com.google.zxing.client.android.SCAN";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        viewpager = (ViewPager) findViewById(R.id.pager);
        ft = new PageAdapterFragment(getSupportFragmentManager());

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
                } catch (ActivityNotFoundException anfe) {

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
                String contents = intent.getStringExtra("SCAN_RESULT");

            }
        }
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
            case R.id.qr_reader:
                try {
                    intent = new Intent(ACTION_SCAN);
                    intent.putExtra("SCAN_MODE", "QR_CODE_MODE");
                    startActivityForResult(intent, 0);
                } catch (ActivityNotFoundException anfe) {
                    showDialog(MenuActivity.this, "No Scanner Found", "Download a scanner code activity?", "Yes", "No").show();
                }
                return true;
            case R.id.action_search:
                intent = new Intent(this, UserSearchActivity.class);
                startActivity(intent);
                return true;

            case R.id.createGroup:
                intent = new Intent(this, CreateGroupActivity.class);
                startActivity(intent);
                return true;

            case R.id.loginGoogle:
                intent = new Intent(this, LoginGoogleActivity.class);
                startActivity(intent);
                return true;

            case R.id.buscarGroup:
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

    public void fab_home(View view){
        Intent intent = new Intent(this, CreateCommentActivity.class);
        intent.putExtra(CreateCommentActivity.COMMENT_TYPE, "publico");
        startActivity(intent);
        Log.i("fab", "home");
    }


    public void fab_group(View view){
        Intent intent = new Intent(this, CreateCommentActivity.class);
        intent.putExtra(CreateCommentActivity.COMMENT_TYPE, "grupo");
        startActivity(intent);
        Log.i("fab", "grupo");
    }

    public void fab_nota(View view){
        Intent intent = new Intent(this, CreateCommentActivity.class);
        intent.putExtra(CreateCommentActivity.COMMENT_TYPE, "nota");
        startActivity(intent);
        Log.i("fab", "nota");
    }
}
