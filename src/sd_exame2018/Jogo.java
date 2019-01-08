/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sd_exame2018;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 *
 * @author Luís Alves
 */
public class Jogo {
  
  private List<String> inscritos;
  private Lock l;
  private Condition start;
  private boolean canStart;
  
  public Jogo() {
    this.inscritos = new ArrayList<>();
    this.l = new ReentrantLock();
    this.start = l.newCondition();
    this.canStart = false;
  }
  
  public List<String> inscrever(String nome) {
    try {
      l.lock();
      if (inscritos.size() < 30)
        inscritos.add(nome);
      else
        return inscritos;
      boolean flag = inscritos.size() >= 20 && (inscritos.size() % 2 == 0);
      while (!flag && !canStart)
        flag = start.await(1,TimeUnit.MINUTES);
      
      canStart = inscritos.size() >= 20 && (inscritos.size() % 2 == 0);
      
      if (canStart)
        start.signalAll();
      else
        while(!canStart)
          start.await();
        
    }
    catch (Exception e) {}
    finally {
      l.unlock();
    }
    return inscritos;
  }
}
