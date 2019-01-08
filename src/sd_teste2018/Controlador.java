/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sd_teste2018;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 *
 * @author Luís Alves
 */
public class Controlador {
  
  public final int numTerminais = 4;
  public final int capacidadeMax = 20;
  public final int tempoParagem = 60000; // 60 mil milissegundos
  public final int tempoViagem = 180000; // 3*60*1000
  private final Map<Integer,Condition> shuttleChegou;
  private Lock l;
  private int terminalAtual;
  private int passageirosNoShuttle;
  private boolean emTransito;
  
  public Controlador() {
    this.l = new ReentrantLock();
    this.shuttleChegou = new HashMap<>();
    for(int i = 0; i < numTerminais; i++)
      this.shuttleChegou.put(i, l.newCondition());
    this.terminalAtual = 0;
    this.passageirosNoShuttle = 0;
    this.emTransito = false;
  }
  
  public void quero_viajar(int origem, int destino) {
    Condition chegouAqui = shuttleChegou.get(origem);
    try {
      l.lock();
      while(emTransito && terminalAtual != origem && passageirosNoShuttle == capacidadeMax)
        try {
          chegouAqui.await();
        }
        catch (InterruptedException e) {}
      passageirosNoShuttle++;
    }
    finally {
      l.unlock();
    }
  }
  
  public void quero_sair(int destino) {
    Condition chegouAoDestino = shuttleChegou.get(destino);
    try {
      l.lock();
      while (emTransito && terminalAtual != destino)
        try {
          chegouAoDestino.await();
        }
        catch (InterruptedException e) {}
      passageirosNoShuttle--;
      chegouAoDestino.signal();
    }
    finally {
      l.unlock();
    }
  }
  
  public void partida() {
    try {
      l.lock();
      emTransito = true;
    }
    finally {
      l.unlock();
    }
  }
  
  public void chegada() {
    Condition chegou = shuttleChegou.get(terminalAtual);
    try {
      l.lock();
      terminalAtual = (terminalAtual + 1) % numTerminais;
      emTransito = false;
      chegou.signalAll();
    }
    finally {
      l.unlock();
    }
  }
}
