/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sd_teste2018;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * @author Luís Alves
 */
public class Controlador_Server implements Runnable {
  
  private Controlador control;
  private Socket s;
  
  public Controlador_Server(Controlador c, Socket s) {
    this.control = c;
    this.s = s;
  }
  
  public static void main(String[] args) throws IOException {
    Controlador c = new Controlador();
    ServerSocket ss = new ServerSocket(9999);
    new Thread(new Shuttle(c)).start();
    while (true) {
      Socket s = ss.accept();
      new Thread(new Controlador_Server(c,s)).start();
    }
  }
  public void run() {
    try {
      PrintWriter out = new PrintWriter(s.getOutputStream(),true);
      BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));

      String msg = null;
      while((msg = in.readLine()) != null) {
        String[] split = msg.split(" ");
        control.quero_viajar(Integer.parseInt(split[0]), Integer.parseInt(split[1]));
        out.println("Podes entrar!");
        control.quero_sair(Integer.parseInt(split[1]));
        out.println("Podes sair!");
      }
    }
    catch (IOException e) {}
  }
  
}
