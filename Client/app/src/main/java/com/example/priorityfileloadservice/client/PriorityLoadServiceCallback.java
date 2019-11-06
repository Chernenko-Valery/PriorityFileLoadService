package com.example.priorityfileloadservice.client;

import com.example.priorityfileloadservice.library.Response;

public interface PriorityLoadServiceCallback {

    void onAnswerRequest(int Code);

    void onRecieveResponse(Response response);

}
