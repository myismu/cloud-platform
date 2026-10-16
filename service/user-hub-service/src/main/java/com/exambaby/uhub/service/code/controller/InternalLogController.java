package com.exambaby.uhub.service.code.controller;

import com.exambaby.common.core.entity.GlobalLog;
import com.exambaby.uhub.service.code.service.GlobalLogService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Log4j2
@RestController
@RequestMapping("/internal/log")
public class InternalLogController {

    @Autowired
    private GlobalLogService globalLogService;

    @PostMapping("/save")
    public boolean saveLog(@RequestBody GlobalLog globalLog) {
        try {
            boolean result = globalLogService.saveLog(globalLog);
            log.info("Internal log saved: {}", globalLog.getRequestUrl());
            return result;
        } catch (Exception e) {
            log.error("Failed to save internal log: {}", e.getMessage());
            return false;
        }
    }
}
