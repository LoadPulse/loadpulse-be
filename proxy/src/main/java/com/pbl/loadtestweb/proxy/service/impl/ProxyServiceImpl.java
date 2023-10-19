package com.pbl.loadtestweb.proxy.service.impl;

import com.pbl.loadtestweb.proxy.payload.response.ProxyResponse;
import com.pbl.loadtestweb.proxy.service.ProxyService;
import io.github.bonigarcia.wdm.WebDriverManager;
import lombok.RequiredArgsConstructor;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.devtools.DevTools;
import org.openqa.selenium.devtools.v116.network.Network;
import org.openqa.selenium.devtools.v116.network.model.Request;
import org.openqa.selenium.devtools.v116.network.model.RequestId;
import org.openqa.selenium.devtools.v116.network.model.Response;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProxyServiceImpl implements ProxyService {

  @Override
  public ProxyResponse proxyBrowser() {
    ProxyResponse proxyResponse = new ProxyResponse();
    WebDriverManager.chromedriver().setup();
    ChromeDriver chromeDriver = new ChromeDriver();
    DevTools devTools = chromeDriver.getDevTools();
    devTools.createSession();
    devTools.send(Network.enable(Optional.empty(), Optional.empty(), Optional.empty()));

    devTools.addListener(
        Network.requestWillBeSent(),
        requestConsumer -> {
          Request request = requestConsumer.getRequest();
          if (request.getUrl().contains("?wc-ajax=")) {
            proxyResponse.setUrl(request.getUrl());
            proxyResponse.setMethod(request.getMethod());
          }
        });

    // Capture Http response
    final RequestId[] requestId = new RequestId[1];
    devTools.addListener(
        Network.responseReceived(),
        responseConsumer -> {
          Response response = responseConsumer.getResponse();
          requestId[0] = responseConsumer.getRequestId();
          if (response.getMimeType().contains("HTML")) {
            proxyResponse.setStatus(String.valueOf(response.getStatus()));
            proxyResponse.setUrl(response.getUrl());
            String responseBody = devTools.send(Network.getResponseBody(requestId[0])).getBody();
            proxyResponse.setResponseBody(responseBody);
          }
        });
    return proxyResponse;
  }
}
