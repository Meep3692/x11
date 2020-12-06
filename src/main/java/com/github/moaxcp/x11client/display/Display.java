package com.github.moaxcp.x11client.display;

import com.github.moaxcp.x11client.X11Client;
import com.github.moaxcp.x11client.protocol.OneWayRequest;
import com.github.moaxcp.x11client.protocol.XEvent;
import com.github.moaxcp.x11client.protocol.xproto.ExposeEvent;
import com.github.moaxcp.x11client.protocol.xproto.KeyPressEvent;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Display implements AutoCloseable {
  private final X11Client client;
  private volatile boolean running;
  private final Map<Integer, Window> windows = new HashMap<>();

  public Display(X11Client client) {
    this.client = client;
  }

  public GraphicsContext createRootGC(int screen) {
    return new GraphicsContext(this, client.getRoot(screen), client.getWhitePixel(screen), client.getBlackPixel(screen));
  }

  public Window createSimpleWindow(int screen, int x, int y, int width, int height, int borderWidth) {
    //todo make sure values are valid range for protocol
    Window window = new Window(this, screen, (short) x, (short) y, (short) width, (short) height, (short) borderWidth);
    windows.put(window.getId(), window);
    return window;
  }

  public void send(OneWayRequest request) {
    client.send(request);
  }

  int nextResourceId() {
    return client.nextResourceId();
  }
  
  @Override
  public void close() throws IOException {
    client.close();
  }

  public byte getDepth(int screen) {
    return client.getDepth(screen);
  }

  public int getRoot(int screen) {
    return client.getRoot(screen);
  }

  public int getVisualId(int screen) {
    return client.getVisualId(screen);
  }

  public int getWhitePixel(int screen) {
    return client.getWhitePixel(screen);
  }

  public int getBlackPixel(int screen) {
    return client.getBlackPixel(screen);
  }

  public void stop() {
    running = false;
  }

  public void start() {
    running = true;
    while(running) {
      XEvent event = client.getNextEvent();
      if(event instanceof ExposeEvent) {
        ExposeEvent expose = (ExposeEvent) event;
        Window window = windows.get(expose.getWindow());
        window.exposeEvent(expose);
      }
      if(event instanceof KeyPressEvent) {
        KeyPressEvent keyPress = (KeyPressEvent) event;
        Window window = windows.get(keyPress.getRoot());
        window.keyPressEvent(keyPress);
      }
    }
  }
}
