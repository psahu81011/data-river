package com.github.psahu81011.river.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class MessageKey {

  private String database;

  private String table;

}