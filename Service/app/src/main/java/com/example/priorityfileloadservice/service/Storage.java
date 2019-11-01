package com.example.priorityfileloadservice.service;

import android.content.Context;

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

    public Response SaveFromInputStream(InputStream aInputStream, int aSize, String aFileName,Context aContext) {
        try {
            BufferedInputStream bis = new BufferedInputStream(aInputStream);
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(new File(aFileName)));

            int c;
            while ( (c=bis.read()) != -1) {
                bos.write(c);
            }
            bos.close();
            bis.close();

            //TODO FILEProvider
            Response response = new Response(INTERACTION_CONSTANTS.RESPONSE_STATUS_OK, "" , aSize)
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
