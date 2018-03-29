
import java.io.*;
import java.net.*;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;

public class Server implements Runnable {

  //variables for the program
  protected int serverPort = 6666;
  protected ServerSocket ss = null;
  protected Socket sample = null;
  protected Thread runningThread = null;
  protected boolean isStopped = false;

  //input for the socket
  public Server(int userPort){
    this.serverPort = userPort;
  }

  public void run(){
    synchronized(this){ this.runningThread = Thread.currentThread();}

    try{
    this.ss = new ServerSocket(this.serverPort); //opens ServerSocket

    while(! isStopped()){
          Socket socket = null;
          try{
            socket = this.ss.accept(); //establishes connection
            DataInputStream dis = new DataInputStream(socket.getInputStream());
            String  str = (String)dis.readUTF(); //ensures message between client and server is connected
            System.out.println(str);
          } catch (RuntimeException e) {}
          new Thread(new ClientConnectionHandler(socket, "Multithreaded Server")).start();
    }
    System.out.println("Server has now stopped.");

    } catch (IOException e){}

  }

  //ensures
  private synchronized boolean isStopped(){
    return this.isStopped;
  }

  public synchronized void stop(){
        this.isStopped = true;
        try {
            this.ss.close();
        } catch (IOException e) {
            throw new RuntimeException("Error closing server", e);
        }
    }


  public static void main(String[] args){

    Server server = new Server(6666);
    new Thread(server).start(); //allows for multhreading

    try {
        Thread.sleep(3600 * 1000);
    } catch (InterruptedException e) {
        e.printStackTrace();
    }
    server.stop();
    }


}
