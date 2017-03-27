public class BroadcastSystem implements Runnable{

  private static byte bLimit = 0;
  private int value = 0;
  private BroadcastSystem(){

  }

  //only one BroadcastSystem can exist
  public static BroadcastSystem getInstance(){
    if( bLimit < 1 ){
        BroadcastSystem broadcastSystem = new BroadcastSystem();
        bLimit = 1;
        return broadcastSystem;
    }
    return null;
  }
  
  
  public void setValue(int value){
	  this.value= value;
  }

  public void run(){
    PrintToScreen.threadMessage("test", "this is the value" + value);
  }
}
