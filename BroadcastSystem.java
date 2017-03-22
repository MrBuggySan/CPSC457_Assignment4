public class BroadcastSystem implements Runnable{

  private static byte = 0;

  private BroadcastSystem(){

  }

  //only one Broadcastsystem can exist
  public static BroadcastSystem(){
    if( bLimit < 1 ){
        BroadcastSystem broadcastSystem = new BroadcastSystem();
        bLimit = 1;
        return broadcastSystem;
    }
    return null;
  }

  public void run(){
    
  }
}
