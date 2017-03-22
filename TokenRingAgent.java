public class TokenRingAgent implements Runnable{
  private int tokenID;
  private boolean isActive;
  private int processorID;
  private int predecessorID;
  private int sucessorID;

  public int RecieveToken(){
    return tokenID;
  }

  public void SendToken(Token t){
    //send token to the processor
  }

  public void run(){
    
  }
}
