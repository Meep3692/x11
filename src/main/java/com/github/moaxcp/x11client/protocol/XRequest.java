package com.github.moaxcp.x11client.protocol;

import java.io.IOException;

public interface XRequest extends XObject {
  byte getOpCode();
  default int getLength() {
    return (short) ((getSize() + 4 - getSize() % 4) / 4);
  }
  void write(X11Output out) throws IOException;
}
