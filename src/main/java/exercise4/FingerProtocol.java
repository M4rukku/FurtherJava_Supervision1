package exercise4;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class FingerProtocol {
  private static final int port = 79;
  private String server;
  private final Socket socket;
  private final BufferedReader inputStream;
  private final OutputStreamWriter outputStream;
  private boolean initialised = false;

  private String user;

  FingerProtocol(String mail) {
    String[] data = mail.split("@");

    if (data.length != 2) {
      System.err.println("Invalid mail address");
      System.exit(0);
    }

    this.user = data[0];
    this.server = data[1];

    Socket tempSocket = null;
    try {
      tempSocket = new Socket(server, port);
    } catch (IOException e) {
      e.printStackTrace();
    }

    socket = tempSocket;

    BufferedReader inputStream = null;
    OutputStreamWriter outputStream = null;
    try {
      inputStream = new BufferedReader(new InputStreamReader(socket.getInputStream()));
      outputStream = new OutputStreamWriter(socket.getOutputStream());
    } catch (IOException e) {
      e.printStackTrace();
    }

    this.inputStream = inputStream;
    this.outputStream = outputStream;
    initialised = true;
  }

  void receiveData() {
    Thread listener = new Thread() {
      @Override
      public void run() {
        char[] buffer = new char[1024];
        while (true) {
          try {
            int read = inputStream.read(buffer);
            if (read != -1) {
              System.out.println(Arrays.toString(buffer).replace(",", ""));
            }
          } catch (IOException e) {
            e.printStackTrace();
            System.err.println("An error occured when reading from the input stream");
          }
        }
      }
    };
    listener.setDaemon(true);
    listener.start();

    try {
      outputStream.write(user + "\r\n");
      outputStream.flush();
    } catch (IOException e) {
      e.printStackTrace();
    }

    processInputs();
    try {
      socket.close();
    } catch (IOException e) {
      System.out.println("Failed to close Socket");
    }
  }

  private void processInputs() {
    try (BufferedReader r = new BufferedReader(new InputStreamReader(System.in));) {
      while (true) {
        String read = r.readLine();
        if (read.equals("quit")) {
          break;
        } else {
          System.out.println("Please write \"quit\"to exit !!");
        }
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public static void main(String[] args) {
    FingerProtocol protocol = new FingerProtocol("arb33@hermes.cam.ac.uk");
    protocol.receiveData();
  }
}
