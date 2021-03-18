package com.example.entity;
import lombok.Getter;

@Getter
public class ServerDate {
  private String date;
  private String time;
  private String timezone;
  private String epoch;


  public ServerDate(String date, String time, String timezone, String epoch) {
    this.date = date;
    this.time = time;
    this.timezone = timezone;
    this.epoch = epoch;
  }

}
