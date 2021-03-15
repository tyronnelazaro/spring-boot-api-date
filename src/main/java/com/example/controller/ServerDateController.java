package com.example.controller;

import com.example.entity.ServerDate;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

@RestController
public class ServerDateController {
    private static final DateTimeFormatter dateonly = DateTimeFormatter.ofPattern("MM-dd-yyyy");
    private static final DateTimeFormatter timeonly = DateTimeFormatter.ofPattern("HH:mm:ss");
    private static final DateTimeFormatter timezone = DateTimeFormatter.ofPattern("Z");

    @GetMapping(value = "/serverdate", produces = MediaType.APPLICATION_JSON_VALUE)
    public ServerDate getDates() {
      LocalDateTime dateTime = LocalDateTime.now();
      LocalTime Time = LocalTime.now();
      ZonedDateTime zonedDateTime = ZonedDateTime.now();
      return new ServerDate(dateTime.format(dateonly).toString(), Time.format(timeonly).toString(), zonedDateTime.format(timezone).toString());
    }
}
