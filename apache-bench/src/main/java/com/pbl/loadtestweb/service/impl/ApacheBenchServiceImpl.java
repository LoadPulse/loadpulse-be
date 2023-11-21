package com.pbl.loadtestweb.service.impl;

import com.pbl.loadtestweb.payload.response.ApacheBenchResponse;
import com.pbl.loadtestweb.service.ApacheBenchService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ApacheBenchServiceImpl implements ApacheBenchService {
  @Override
  public ApacheBenchResponse loadTestABDefaultWithParams(int request, int concurrent, String path) {
    return null;
  }
}
