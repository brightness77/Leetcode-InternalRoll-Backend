package com.b77.leetcodeapi.leetcodeapi.controller;

import com.b77.leetcodeapi.leetcodeapi.model.billboard.BillboardReturn;
import com.b77.leetcodeapi.leetcodeapi.service.BillboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/billboard")
public class BillboardController {

    @Autowired
    BillboardService billboardService;

    @GetMapping("/top3")
    public BillboardReturn getTop3Now(){
        return billboardService.getTopKSubmissionAccount(3);
    }
}
