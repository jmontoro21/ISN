package com.inftel.isn.activity;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.content.IntentSender.SendIntentException;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;
import com.google.gson.Gson;
import com.inftel.isn.model.Comment;
import com.inftel.isn.model.User;
import com.inftel.isn.request.RestServicePost;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginGoogleActivity extends Activity implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    private String email;
    private String name;
    private String googleId;
    private String imgUrl;
    public static final String USER_KEY = "es.inftel.isn.user.google.id.email";
    public static final String USER_NAME = "es.inftel.isn.user.google.id.name";
    public static final String USER_ID = "es.inftel.isn.user.google.id.id";
    public static final String USER_URL = "es.inftel.isn.user.google.id.url";
    private static final String TAG = "LoginGoogleActivity";

    private static final String KEY_IN_RESOLUTION = "is_in_resolution";

    /**
     * Request code for auto Google Play Services error resolution.
     */
    protected static final int REQUEST_CODE_RESOLUTION = 1;

    /**
     * Google API client.
     */
    private GoogleApiClient mGoogleApiClient;

    /**
     * Determines if the client is in a resolution state, and
     * waiting for resolution intent to return.
     */
    private boolean mIsInResolution;

    /**
     * Called when the activity is starting. Restores the activity state.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            mIsInResolution = savedInstanceState.getBoolean(KEY_IN_RESOLUTION, false);
        }
    }

    /**
     * Called when the Activity is made visible.
     * A connection to Play Services need to be initiated as
     * soon as the activity is visible. Registers {@code ConnectionCallbacks}
     * and {@code OnConnectionFailedListener} on the
     * activities itself.
     */
    @Override
    protected void onStart() {
        super.onStart();
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addApi(Plus.API)
                    .addScope(Plus.SCOPE_PLUS_LOGIN)
                            // Optionally, add additional APIs and scopes if required.
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .build();
        }
        mGoogleApiClient.connect();
    }

    /**
     * Called when activity gets invisible. Connection to Play Services needs to
     * be disconnected as soon as an activity is invisible.
     */
    @Override
    protected void onStop() {
        if (mGoogleApiClient != null) {
            mGoogleApiClient.disconnect();
        }
        super.onStop();
    }

    /**
     * Saves the resolution state.
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(KEY_IN_RESOLUTION, mIsInResolution);
    }

    /**
     * Handles Google Play Services resolution callbacks.
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_CODE_RESOLUTION:
                retryConnecting();
                break;
        }
    }

    private void retryConnecting() {
        mIsInResolution = false;
        if (!mGoogleApiClient.isConnecting()) {
            mGoogleApiClient.connect();
        }
    }

    /**
     * Called when {@code mGoogleApiClient} is connected.
     */
    @Override
    public void onConnected(Bundle connectionHint) {

        email = Plus.AccountApi.getAccountName(mGoogleApiClient);

        Person persona = Plus.PeopleApi.getCurrentPerson(mGoogleApiClient);

        name = persona.getDisplayName();
        googleId = persona.getId();
        imgUrl =persona.getImage().getUrl();

        SharedPreferences prefs = this.getSharedPreferences("MYPREFERENCES", Context.MODE_PRIVATE);
        SharedPreferences.Editor e = prefs.edit();
        e.putString(USER_KEY, email);
        e.putString(USER_NAME, name);
        e.putString(USER_ID, googleId);
        e.putString(USER_URL, imgUrl);
        e.commit();

        try {

        User userData = new User();
        //userData.setId(null);
        userData.setEmail(email);
        userData.setGoogleId(googleId);
        userData.setName(name);
        userData.setImageUrl(imgUrl);






            /*
            Comment com1 = new Comment();
            com1.setImageUrl();
            com1.setAuthor();
            com1.setCreationDate();
            com1.setText();
            com1.setVideoUrl();
*/


        Gson gson = new Gson();
        String json = gson.toJson(userData, User.class);

            System.out.println("gson " + json);

            JSONObject user = new JSONObject(json);

           // new RestServicePost(user).execute("http://192.168.1.117:8080/InftelSocialNetwork-web/webresources/users/create");



            // comentario
            String nuevo = "{ \"text\" : \"privado nuevo\" , \"creationDate\" : { \"$date\" : \"2015-02-19T11:00:53.967Z\"} , \"author\" : { \"_id\" : \"5509b11dbfcba77ba8877e14\" , \"googleId\" : \"104611636054460643894\" , \"email\" : \"jmontoro21@gmail.com\" , \"name\" : \"josé Fernández Montoro\" , \"imageUrl\" : \"https://lh3.googleusercontent.com/-XdUIqdMkCWA/AAAAAAAAAAI/AAAAAAAAAAA/4252rscbv5M/photo.jpg?sz=250\"} , \"imageUrl\" : \"\" , \"videoUrl\" : \"\"}";

            JSONObject coment = new JSONObject(nuevo);

            String formatEmail = email.replaceAll("\\.", "___") ;

            new RestServicePost(coment).execute("http://192.168.1.117:8080/InftelSocialNetwork-web/webresources//insert/userEmail/"+ formatEmail );





        } catch (JSONException eq) {
            eq.printStackTrace();
        }






        // String formatEmail = email.replaceAll("\\.", "___") ;









        // Compruebo si el usuario está en la bd y sino, lo inserto.










        //new RestServiceGet().execute("http://192.168.1.117:8080/InftelSocialNetwork-web/webresources/users/" + formatEmail);

        //System.out.println("resultado " + isEmailInserted);


        //Intent i = new Intent(this, ListPublicCommentActivity.class);
        //startActivity(i);
    }

    /**
     * Called when {@code mGoogleApiClient} connection is suspended.
     */
    @Override
    public void onConnectionSuspended(int cause) {
        Log.i(TAG, "GoogleApiClient connection suspended");
        retryConnecting();
    }

    /**
     * Called when {@code mGoogleApiClient} is trying to connect but failed.
     * Handle {@code result.getResolution()} if there is a resolution
     * available.
     */
    @Override
    public void onConnectionFailed(ConnectionResult result) {
        Log.i(TAG, "GoogleApiClient connection failed: " + result.toString());
        if (!result.hasResolution()) {
            // Show a localized error dialog.
            GooglePlayServicesUtil.getErrorDialog(
                    result.getErrorCode(), this, 0, new OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface dialog) {
                            retryConnecting();
                        }
                    }).show();
            return;
        }
        // If there is an existing resolution error being displayed or a resolution
        // activity has started before, do nothing and wait for resolution
        // progress to be completed.
        if (mIsInResolution) {
            return;
        }
        mIsInResolution = true;
        try {
            result.startResolutionForResult(this, REQUEST_CODE_RESOLUTION);
        } catch (SendIntentException e) {
            Log.e(TAG, "Exception while starting resolution activity", e);
            retryConnecting();
        }
    }

/*
    public void restoreBackup(){
        datasource.open();
        datasource.deleteDB();
        getBackup(googleUser);

    }

    private void getBackup(String googleId) {
        new RestServiceGet().execute(GET_BACKUP_URL + googleId);
    }


    public void createBackup() throws UnsupportedEncodingException, JSONException {
        datasource.open();
        List<JournalEntry> values = datasource.getAllJournalEntries();
        int i;
        createUserDB(googleUser);
        deleteDB(googleUser);
        for(i=0; i< values.size();i++){
            createJournalEntry(googleUser, values.get(i));
        }
        datasource.close();
    }

    public void createUserDB(String googleId){
        String json;
        json = "{\"googleid\":\""+googleId+"\"}";
        try {
            JSONObject user = new JSONObject(json);
            new RestServiceCreateUser(user)
                    .execute(REST_CREATE_USER_URL);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void deleteDB(String googleId){
        new RestServiceDelete().execute(DELETE_EVENT_URL+googleId);
    }

    public void createJournalEntry(String googleId, JournalEntry je) throws JSONException, UnsupportedEncodingException {
        String json;
        json = createJson(je, googleId);
        JSONObject dato = new JSONObject(json);
        new RestServicePost(dato).execute(CREATE_EVENT_URL);
    }

    public String createJson(JournalEntry je, String googleId){
        String json;
        json = "{\"address\":\" \",\"description\":\""+je.getDesc()+"\",\"eventdate\":\" \",\"latitude\":\""+je.getGps_lat()+"\",\"longitude\":\""+je.getGps_long()+"\",\"title\":\""+je.getTitle()+"\",\"urlimage\":\""+je.getImg()+"\",\"userdataGoogleid\":{\"googleid\":\""+googleId+"\"}}";
        return json;
    }



    private class RestServiceGet extends AsyncTask<String, Integer, String> {

        protected String doInBackground(String... urls) {
            HttpClient httpClient = new DefaultHttpClient();
            HttpGet g = new HttpGet(urls[0]);
            g.setHeader("Accept", "application/json");
            g.setHeader("Content-type", "application/json");
            try
            {
                System.out.println("LLEGA 1");
                HttpResponse resp = httpClient.execute(g);
                String respStr = EntityUtils.toString(resp.getEntity());
                int i;
                JSONArray respJSON = new JSONArray(respStr);
                System.out.println(respJSON.toString());
                for(i=0; i<respJSON.length(); i++){
                    JSONObject object = respJSON.getJSONObject(i);
                    String title = object.getString("title");
                    String entryContent = object.getString("description");
                    String lat = object.getString("latitude");
                    String lg = object.getString("longitude");
                    String im = object.getString("urlimage");
                    datasource.open();
                    datasource.createEntry(title, entryContent, lat, lg, im);
                    datasource.close();
                }
            }
            catch(Exception ex)
            {
                Log.e("ServicioRest","Error!", ex);
            }
            return null;
        }

        protected void onProgressUpdate(Integer... progress){

        }

        protected void onPostExecute(String result) {
            reloadActivity();
        }
        public void reloadActivity(){
            Intent intent = new Intent(this, MainActivity.class);
            this.startActivity(intent);





        }
*/
}
