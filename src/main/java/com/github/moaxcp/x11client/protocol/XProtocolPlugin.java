package com.github.moaxcp.x11client.protocol;

import com.github.moaxcp.x11client.XProtocolService;

import java.io.IOException;

public interface XProtocolPlugin {
  String getName();
  byte getOffset();
  void setupOffset(XProtocolService service) throws IOException;
  boolean supportedRequest(XRequest request);
  boolean supportedEvent(byte number);
  boolean supportedError(byte code);
  XEvent readEvent(byte number, boolean sentEvent, X11Input in) throws IOException;
  XError readError(byte code, X11Input in) throws IOException;
}