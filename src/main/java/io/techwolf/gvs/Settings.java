package io.techwolf.gvs;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Settings {

  private String mqttServerUrl;
  private String mqttUsername;
  private String mqttPassword;
  private String forwardEnergyTopic;
  private String reverseEnergyTopic;
  private Boolean periodicalBalancing; // this enables 60 minutes periodical balancing
  private String periodicalFlushCron;
}



