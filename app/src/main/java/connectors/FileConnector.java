package connectors;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.Closeable;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by ao on 29.04.15.
 */

public class FileConnector {
    public static File EXTERNAL_DIR = Environment.getExternalStorageDirectory();

    public final static String LINE_SEPARTOR = System.getProperty("line.separator");
    private static final int BUFFER_SIZE = 2048;
    private static final int EOF_MARK = -1;

    private FileConnector() {}


    public static boolean writeInternal(Context context, InputStream source, String fileName, int mode) {
        BufferedOutputStream dest = null;

        try {
            dest = new BufferedOutputStream(context.openFileOutput(fileName, mode));
            writeFromInputToOutput(source, dest);
            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        } finally {
            close(dest);
        }
    }

    public static InputStream readFromInternal(Context context, String fileName) {
        BufferedInputStream source = null;
        try {
            source = new BufferedInputStream(context.openFileInput(fileName));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return source;
    }


    public static boolean close(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
                return true;
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        } else {
            return false;
        }
    }

    public static String convertStreamToString(InputStream is) {
        try {
            return new java.util.Scanner(is).useDelimiter("\\A").next();
        } catch (java.util.NoSuchElementException e) {
            return "";
        }
    }

    public static int writeFromInputToOutput(InputStream source, OutputStream dest) {
        byte[] buffer = new byte[BUFFER_SIZE];
        int bytesRead = EOF_MARK;
        int count = 0;
        try {
            while ((bytesRead = source.read(buffer)) != EOF_MARK) {
                dest.write(buffer, 0, bytesRead);
                count += bytesRead;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return count;
    }

    public static boolean isExternalAvailable() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    public static void show(File file, String TAG) throws IOException {
        FileReader freader = new FileReader(file);
        BufferedReader br = new BufferedReader(freader);
        String s;
        while((s = br.readLine()) != null) {
            Log.d(TAG, s);
        }
    }

    public static void writeTo(File file, String content) throws IOException {
        FileWriter fr = new FileWriter(file);
        fr.write(content);
        fr.close();
    }
}
