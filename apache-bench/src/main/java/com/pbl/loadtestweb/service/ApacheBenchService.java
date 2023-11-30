package com.pbl.loadtestweb.service;

import com.pbl.loadtestweb.payload.response.ApacheBenchResponse;

public interface ApacheBenchService {

  ApacheBenchResponse loadTestABDefaultWithParams(int request, int concurrent, String url);
}
