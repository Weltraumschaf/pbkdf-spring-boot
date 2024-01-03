package de.weltraumschaf.special.controller;

import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
final class ConnectRequest {
      String username;
      @ToString.Exclude
      String password;
      String server;
      String domain;
      String share;
}
