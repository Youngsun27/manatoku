package com.manatoku.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.manatoku.model.Today;
import com.manatoku.service.TodayService;

@RestController
@RequestMapping("/today")
public class TodayController {

    @Autowired
    private TodayService todayService;

    @GetMapping("/current")
    public Today getCurrent() {
        return todayService.getCurrentKanji();
    }

    @GetMapping("/next")
    public Today getNext() {
        return todayService.getNextKanji();
    }

    @GetMapping("/prev")
    public Today getPrev() {
        return todayService.getPrevKanji();
    }
}