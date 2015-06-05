package connectors;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.android.AndroidAuthSession;
import com.dropbox.client2.exception.DropboxException;
import com.dropbox.client2.session.AppKeyPair;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;


/**
 * Connector class, which is responsible for Dropbox connection establishing,
 * its maintaining, file sharing through this service.
 *
 * @author Anton Korenev
 * @version 1.0
 */
public class DropboxConnector {
    //Dropbox variables
    /**
     * Auth key of the app, one of the token pair
     */
    private final static String APP_KEY = "5ofcf92081mlf3k";
    /**
     * Secure key of the app, one of the token pair
     */
    private final static String APP_SECRET = "zrqiyr5vlz959j6";
    /**
     * Dropbox API class
     */
    private DropboxAPI<AndroidAuthSession> mDBApi;
    /**
     * App name in Dropbox
     */
    private final static String DROPBOX_NAME = "SHomeApp";
    /**
     * directory files of the app
     */
    public final static String FILE_DIR = "/SHomeApp/";

    /**
     * The main constructor, is responsible for API initialization
     */
    public DropboxConnector(){
        AppKeyPair appKeys = new AppKeyPair(APP_KEY, APP_SECRET);
        AndroidAuthSession session = new AndroidAuthSession(appKeys);
        mDBApi = new DropboxAPI<>(session);
    }

    /**
     * The method for establishing connection with Dropbox. It will create view on top of the Activity,
     * if it is first connection of the current app, or it would use cached token,
     * if the connection was established before.  It requires to be used in onCreate() method of
     * activity.
     * @param sessionActivity The Activity, which is used for creating session and using Dropbox API
     */
    public void startAuthentification(Activity sessionActivity){
        String token = restoreToken(sessionActivity);
        if (token != null) {
            mDBApi.getSession().setOAuth2AccessToken(token);
        } else {
            mDBApi.getSession().startOAuth2Authentication(sessionActivity);
        }
    }

    /**
     * The method to finish auth process. It requires to be used in onResume() method of activity.
     * @param context Activity context to use SharedPreferences for caching token pair
     */
    public  void finishAuthentification(Context context){
        if (mDBApi.getSession().authenticationSuccessful()) {
            try {
                // Required to complete auth, sets the access token on the session
                mDBApi.getSession().finishAuthentication();
                storeToken(context, mDBApi.getSession().getOAuth2AccessToken());
            } catch (IllegalStateException e) {
                Log.i("DbAuthLog", "Error authenticating", e);
            }
        }
    }

    /**
     * The method for caching token pair.
     * @param context Activity session context
     * @param tokenPair Token pair(APP_KEY, APP_SECRET). It is used to get access to Dropbox files
     */
    private void storeToken(Context context, String tokenPair) {
        // Save the access key for later
        SharedPreferences prefs = context.getSharedPreferences(DROPBOX_NAME, 0);
        SharedPreferences.Editor edit = prefs.edit();
        edit.putString("KEY_TOKEN_PAIR", tokenPair);
        edit.commit();
    }

    /**
     * The method for restoring token pair from app cache.
     * @param context Activity session context
     * @return token pair string
     */
    private String restoreToken(Context context){
        SharedPreferences prefs = context.getSharedPreferences(DROPBOX_NAME, 0);
        return prefs.getString("KEY_TOKEN_PAIR", null);
    }

    /**
     * The method for uploading file into Dropbox
     * @param context Activity session context
     * @param fileNamesInDropbox Array, which represents new names of files in Dropbox; extremely
     *                           valuable for temp files;
     * @param files files for uploading
     */
    public void upload(Context context, String[] fileNamesInDropbox, File... files){
        UploadFileToDropbox upl = new UploadFileToDropbox(context,mDBApi,
                DropboxConnector.FILE_DIR, fileNamesInDropbox);
        upl.execute(files);
    }

    /**
     * The method for downloading files from Dropbox
     * @param fileNamesInDropbox The name of files, which you want to get
     * @param files The initialized files, where you want to write files from dropbox
     * @return ArrayList with files from Dropbox
     * @throws ExecutionException in case of wrong file name or some other clauses, which may result
     * in not finding file with your filename in Dropbox folder
     * @throws InterruptedException in case of interrupting this thread by another one
     */
    public ArrayList<File> download(String[] fileNamesInDropbox, File... files) throws ExecutionException, InterruptedException {
        DropboxConnector.DownloadFileFromDropbox dwl = new DropboxConnector.DownloadFileFromDropbox(
                DropboxConnector.FILE_DIR, mDBApi, fileNamesInDropbox);
        return dwl.execute(files).get();
    }

    //Async classes

    /**
     * The Async class for downloading file from Dropbox in new Thread. Have to be invoked in UI
     * thread
     */
    private class DownloadFileFromDropbox extends AsyncTask<File, Void, ArrayList<File>> {
        /**
         * The path to app folder in Dropbox
         */
        private String path;
        /**
         * A Dropbox API class
         */
        private DropboxAPI<?> dropbox;
        /**
         * The name of files, which you want to get
         */
        private String[] fileNamesInDropbox;

        /**
         * The constructor of async download class
         * @param path The path to app folder in Dropbox
         * @param dropbox A Dropbox API class
         * @param fileNamesInDropbox The name of files, which you want to get
         */
        public DownloadFileFromDropbox(String path, DropboxAPI<?> dropbox, String[] fileNamesInDropbox) {
            this.path = path;
            this.dropbox = dropbox;
            this.fileNamesInDropbox = fileNamesInDropbox;
        }

        @Override
        protected ArrayList<File> doInBackground(File... params) {
            ArrayList<File> files = new ArrayList<>(params.length);

            int count = 0;
            for(File file: params){
                FileOutputStream fileOutputStream;
                try {
                    fileOutputStream = new FileOutputStream(file);
                    dropbox.getFile(path + fileNamesInDropbox[count], null, fileOutputStream, null);
                    files.add(file);
                    count++;
                } catch (DropboxException | IOException e) {
                    e.printStackTrace();
                }

            }
            return files;
        }

        @Override
        protected void onPostExecute(ArrayList<File> resFiles) {

        }
    }

    /**
     * The Async class for uploading file into Dropbox in new Thread. Have to be invoked in UI
     * thread
     */
    private class UploadFileToDropbox extends AsyncTask<File, Void, Boolean> {
        /**
         * A Dropbox API class
         */
        private DropboxAPI<?> dropbox;
        /**
         * The path to app folder in Dropbox
         */
        private String path;
        /**
         * Activity context to create a toast with uploading state
         */
        private Context context;
        /**
         * The name of files, which you want to get
         */
        private String[] fileNamesInDropbox;

        /**
         * The constructor of async upload class
         * @param context Activity context to create a toast with uploading state
         * @param dropbox A Dropbox API class
         * @param path The path to app folder in Dropbox
         * @param fileNamesInDropbox The name of files, which you want to get
         */
        public UploadFileToDropbox(Context context, DropboxAPI<?> dropbox,
                                   String path, String[] fileNamesInDropbox) {
            this.context = context.getApplicationContext();
            this.dropbox = dropbox;
            this.path = path;
            this.fileNamesInDropbox = fileNamesInDropbox;
        }

        @Override
        protected Boolean doInBackground(File... params) {
            boolean isAllUploaded = true;

            int count = 0;
            for(File file: params){
                FileInputStream fileInputStream;
                try {
                    fileInputStream = new FileInputStream(file);
                    dropbox.putFile(path + fileNamesInDropbox[count], fileInputStream,
                            file.length(), null, null);
                    count++;
                } catch (FileNotFoundException | DropboxException e) {
                    e.printStackTrace();
                    isAllUploaded = false;
                }

            }
            return isAllUploaded;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            if (result) {
                Toast.makeText(context, "File Uploaded Sucesfully!",
                        Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(context, "Failed to upload file", Toast.LENGTH_LONG)
                        .show();
            }
        }
    }
}
