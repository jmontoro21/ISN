package com.inftel.isn.activity;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.inftel.isn.R;

public class JoseActivity extends Activity {

    private EditText textNombre;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jose);


        textNombre = (EditText) this.findViewById(R.id.textPrueba);


      // Button botonBorrar = (Button) this.findViewById(R.id.btnDeleteComments);





    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_jose, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        Intent intent;
        switch (item.getItemId()) {

            case R.id.loginGoogle:
                intent = new Intent(this, LoginGoogleActivity.class);
                startActivity(intent);
                return true;

            case R.id.logout:

                intent = new Intent(this, Logout.class);
                startActivity(intent);

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void cargarperfil(View view)
    {
        System.out.println("here   " + textNombre.getText());

        Intent i = new Intent(this, ListPublicCommentActivity.class);
        i.putExtra(ListPublicCommentActivity.EMAIL_USER_PROFILE,textNombre.getText().toString());

        startActivity(i);
    }
}
