package com.apm.test.guide.web;

import com.apm.test.guide.domain.AvailableAdCache;
import com.apm.test.guide.service.AssignService;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/assign")
public class AssignController {

    @Setter(onMethod = @__({ @Autowired }))
    private AssignService assignService;

    @GetMapping
    public AvailableAdCache assignAd() {
        return assignService.extractAd();
    }
}
