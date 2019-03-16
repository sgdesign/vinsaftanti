package com.saeedsoft.security.service;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

//import com.saeedsoft.security.BuildConfig;
import com.saeedsoft.security.BuildConfig;
import com.saeedsoft.security.db.BlackListTable;
import com.saeedsoft.security.CallBlocker.FileException;
import com.saeedsoft.security.stuff.Util;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

import hugo.weaving.DebugLog;

public class ImportExportWrapper {
    private static final String TAG = ImportExportWrapper.class.getSimpleName();
    private static final String LINE_SEPARATOR = "\r\n";
    private final Context context;
    private final BlackListWrapper blackListWrapper;

    public ImportExportWrapper(Context context, BlackListWrapper blackListWrapper) {
        this.context = context;
        this.blackListWrapper = blackListWrapper;
    }

    private String getApplicationName() {
        ApplicationInfo applicationInfo = context.getApplicationInfo();
        int stringId = applicationInfo.labelRes;
        return stringId == 0 ? applicationInfo.nonLocalizedLabel.toString() : context.getString
                (stringId);
    }

    private File[] checkAndObtainExportFiles() throws FileException {
        String state = Environment.getExternalStorageState();
        if (!state.equals(Environment.MEDIA_MOUNTED)) {
            throw new FileException(FileException.MEDIA_ERROR);
        }
        File path = new File(Environment.getExternalStorageDirectory(), getApplicationName());
        File file = new File(path, "blackList.txt");
        return new File[]{path, file};
    }

    @DebugLog
    public boolean isAlreadyExistFile() throws FileException {
        return checkAndObtainExportFiles()[1].exists();
    }

    @DebugLog
    public String exportBlackList() throws FileException {
        File files[] = checkAndObtainExportFiles();
        File path = files[0];
        File file = files[1];

        PrintWriter writer = null;
        Cursor cursor = context.getContentResolver().query(
                BlackListTable.CONTENT_URI, new String[]{BlackListTable.NORMALIZED_NUMBER,
                        BlackListTable.BEGIN_WITH},
                null, null, null);
        try {
            path.mkdirs();
            file.delete();
            file.createNewFile();

            writer = new PrintWriter(new BufferedOutputStream(new FileOutputStream(file)));
            if (cursor != null) {
                int indexNN = cursor.getColumnIndex(BlackListTable.NORMALIZED_NUMBER);
                int indexBW = cursor.getColumnIndex(BlackListTable.BEGIN_WITH);
                while (cursor.moveToNext()) {
                    String normalizedNumber = cursor.getString(indexNN);
                    boolean beginWith = cursor.getInt(indexBW) != 0;
                    writer.print(normalizedNumber + ((beginWith) ? BlackListTable
                            .BEGIN_WITH_SYMBOL + LINE_SEPARATOR :
                            LINE_SEPARATOR));
                }
            }
            return file.getAbsolutePath();
        } catch (IOException | SecurityException e) {
            throw new FileException(FileException.WRITE_FILE_ERROR, e.getMessage(), e);
        } finally {
            Util.close(cursor);
            Util.close(writer);
        }
    }

    @DebugLog
    public int importBlackList(Uri uri) throws FileException {
        if (uri == null) {
            throw new FileException(FileException.READ_FILE_ERROR, "Uri is null");
        }
        File file = new File(uri.getPath());
        BufferedReader br = null;
        String line = null;
        try {
            br = new BufferedReader(new FileReader(file));
            int count = 0;
            while ((line = br.readLine()) != null) {
                if (line.length() < 2 || line.length() > 30) continue;
                if (line.endsWith(BlackListTable.BEGIN_WITH_SYMBOL)) {
                    count += blackListWrapper.addNumberToBlackList(line.substring(0, line.length
                            () - 1), null, true);
                } else {
                    count += blackListWrapper.addNumberToBlackList(line, null, false);
                }
            }
            return count;
        } catch (IOException e) {
            throw new FileException(FileException.READ_FILE_ERROR, "Text line: " + line, e);
        } finally {
            if (br != null) try {
                br.close();
            } catch (IOException e) {
                if (BuildConfig.DEBUG) Log.e(TAG, e.getMessage(), e);
            }
        }
    }
}
