package com.example.entity;
import lombok.Getter;

@Getter
public class ServerDate {
  private String date;
  private String time;
  private String timezone;

  public ServerDate(String date, String time, String timezone) {
    this.date = date;
    this.time = time;
    this.timezone = timezone;
  }

}
