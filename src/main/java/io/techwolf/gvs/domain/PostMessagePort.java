package io.techwolf.gvs.domain;

public interface PostMessagePort {

  void post(String topic, String message, int qos);
}
