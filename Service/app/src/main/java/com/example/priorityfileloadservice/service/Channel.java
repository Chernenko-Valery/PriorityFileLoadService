package com.example.priorityfileloadservice.service;

import android.os.Bundle;
import android.os.Message;
import android.os.RemoteException;

import com.example.priorityfileloadservice.library.INTERACTION_CONSTANTS;
import com.example.priorityfileloadservice.library.Request;
import com.example.priorityfileloadservice.library.RequestWithMessenger;
import com.example.priorityfileloadservice.library.Response;

import org.apache.commons.io.FilenameUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/** Class - Proxy for connection */
public class Channel {

    /** Field - HTTPConnection */
    private HttpURLConnection mConnection;

    public synchronized Response downloadFile(final RequestWithMessenger aRequest, Storage aStorage) {
        try {
            //Open connection
            URL url = new URL(aRequest.getUrl());
            mConnection = (HttpURLConnection) url.openConnection();
            mConnection.setRequestMethod("GET");
            mConnection.connect();

            InputStream inputStream = mConnection.getInputStream();
            //Return response
            Response response = aStorage.SaveFromInputStream(inputStream, mConnection.getContentLength(), FilenameUtils.getName(url.getPath()));

            inputStream.close();
            return response;

        } catch (IOException e) {
            e.printStackTrace();
            //TODO Exception
        }
        return null;
    }
}
