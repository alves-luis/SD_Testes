/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sd_teste2018;

/**
 *
 * @author Luís Alves
 */
public class Shuttle implements Runnable {
  
  private Controlador c;

  public Shuttle(Controlador c) {
    this.c = c;
  }
  
  public void run() {
    while(true) {
      try {
        c.chegada();
        wait(c.tempoParagem);
        c.partida();
        wait(c.tempoViagem);
      }
      catch (InterruptedException e) {}
    }
  }
  
}
