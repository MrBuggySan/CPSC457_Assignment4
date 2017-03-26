public class Assignment4{

  public static void main(String[] args){
    //initialize everything
    Thread processorThread = new Thread(new Processor());
    processorThread.start();

    while(1){

    }
  }
}
