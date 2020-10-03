package com.github.moaxcp.x11client.protocol;

import java.io.IOException;

public interface XReply extends XObject {
  short getSequenceNumber();
  default int getLength() {
    return (short) ((getSize() + 4 - getSize() % 4) / 4);
  }
  void write(X11Output out, short sequenceNumber) throws IOException;
}
