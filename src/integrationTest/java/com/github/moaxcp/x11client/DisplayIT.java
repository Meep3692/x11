package com.github.moaxcp.x11client;

import com.github.moaxcp.x11client.display.Display;
import com.github.moaxcp.x11client.display.GraphicsContext;
import com.github.moaxcp.x11client.display.Window;
import com.github.moaxcp.x11client.protocol.xproto.ImageText8Request;
import com.github.moaxcp.x11client.protocol.xproto.PolyFillRectangleRequest;
import com.github.moaxcp.x11client.protocol.xproto.RectangleStruct;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class DisplayIT {
  private XephyrRunner runner;

  @BeforeEach
  void setup() throws IOException {
    runner = XephyrRunner.builder()
      .ac(true)
      .br(true)
      .noreset(true)
      .arg(":1")
      .build();
    runner.start();
  }

  @AfterEach
  void teardown() throws InterruptedException {
    runner.stop();
  }

  @Test
  void displayTest() throws IOException {
    try (Display display = new Display(X11Client.connect(new DisplayName(":1")))) {
      Window window = display.createSimpleWindow(0, 10, 10, 600, 480, 5);
      window.map();
      GraphicsContext gc = window.createGC();
      window.exposeEvent((d, e) -> {
        List<RectangleStruct> rectangles = new ArrayList<>();
        rectangles.add(RectangleStruct.builder()
          .x((short) 20)
          .y((short) 20)
          .width((short) 10)
          .height((short) 10)
          .build());
        d.send(PolyFillRectangleRequest.builder()
          .drawable(window.getId())
          .gc(gc.getId())
          .rectangles(rectangles)
          .build());
        d.send(ImageText8Request.builder()
          .drawable(window.getId())
          .gc(gc.getId())
          .string("Hello World!")
          .x((short) 10)
          .y((short) 50)
          .build());
      });
      window.keyPressEvent((d, e) -> {
        d.stop();
      });
      display.start();
    }
  }
}
