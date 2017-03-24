public class DSM implements Runnable{
  private LocalMemory localMemory;
  private BroadcastAgent broadcastAgent;

  public void run(){

  }

  public int load(int address){
    return localMemory.load(address);
  }

  public void store(int address, int value){
    //while(not have the token);
    localMemory.store(address, value);
    broadcastAgent.broadcast(address, value);
  }
}