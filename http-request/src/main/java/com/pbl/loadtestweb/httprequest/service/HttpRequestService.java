package com.pbl.loadtestweb.httprequest.service;

import com.pbl.loadtestweb.httprequest.payload.response.HttpDataResponse;

public interface HttpRequestService {
  HttpDataResponse handleMethodGetLoadTestWeb(String url);
}
