package com.example.priorityfileloadservice.service;

import android.content.Context;
import android.net.Uri;

import androidx.core.content.FileProvider;

import com.example.priorityfileloadservice.library.INTERACTION_CONSTANTS;
import com.example.priorityfileloadservice.library.Response;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class Storage {

    Context mAppContext;

    private static final String FILE_PROVIDER_AUTHORITY = "com.example.priorityfileloadservice.service";

    public Storage(Context mAppContext) {
        this.mAppContext = mAppContext;
    }

    /** Save File From Input Stream (Input Stream not closed in ending)
     *
     * @param aInputStream - InputStream with file
     * @param aSize  - File's size
     * @param aFileName - File's Name
     * @return Uri - Uri for file*/
    public Uri SaveFromInputStream(InputStream aInputStream, int aSize, String aFileName) {
        try {
            File file = new File(aFileName);
            BufferedInputStream bis = new BufferedInputStream(aInputStream);
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));

            int c;
            while ( (c=bis.read()) != -1) {
                bos.write(c);
            }
            bos.close();
            bis.close();

            Uri uri = FileProvider.getUriForFile(mAppContext, FILE_PROVIDER_AUTHORITY, file);
            return uri;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
