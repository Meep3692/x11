package com.github.moaxcp.x11client.protocol;

import java.io.IOException;
import java.lang.String;

public interface X11Input {
  boolean readBool() throws IOException;

  byte readByte() throws IOException;

  byte readInt8() throws IOException;

  short readInt16() throws IOException;

  int readInt32() throws IOException;

  int[] readInt32(int length) throws IOException;

  long readInt64() throws IOException;

  byte readCard8() throws IOException;

  byte[] readCard8(int length) throws IOException;

  short readCard16() throws IOException;

  short[] readCard16(int length) throws IOException;

  int readCard32() throws IOException;

  int[] readCard32(int length) throws IOException;

  long readCard64() throws IOException;

  byte[] readChar(int length) throws IOException;

  String readString8(int length) throws IOException;

  byte[] readByte(int length) throws IOException;

  byte[] readVoid(int length) throws IOException;

  default void readPad(int length) throws IOException {
    readByte(length);
  }

  default void readPadAlign(int forLength) throws IOException {
    readPadAlign(4, forLength);
  }

  default void readPadAlign(int pad, int forLength) throws IOException {
    readByte((pad - forLength % pad) % pad);
  }
}
