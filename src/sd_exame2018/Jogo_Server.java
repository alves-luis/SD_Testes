/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sd_exame2018;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

/**
 *
 * @author Luís Alves
 */
public class Jogo_Server implements Runnable{

  private Jogo j;
  private Socket s;
  
  public Jogo_Server(Jogo j, Socket s) {
    this.j = j;
    this.s = s;
  }
  
  public void run() {
    try {
      BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
      PrintWriter out = new PrintWriter(s.getOutputStream(),true);
      String msg = null;
      while((msg = in.readLine()) != null) {
        List<String> inscritos = j.inscrever(msg);
        for(String i : inscritos)
          out.println(i);
      }
    }
    catch (Exception e) {}
  }
  
  public static void main(String[] args) throws IOException{
    ServerSocket ss = new ServerSocket(9999);
    Jogo j = new Jogo();
    
    while(true) {
      Socket s = ss.accept();
      new Thread(new Jogo_Server(j,s)).start();
    }
    
  }
}
