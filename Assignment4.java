public class Assignment4{
	private static int numProcessors = 10;
	
  public static void main(String[] args){
    //initialize everything
	int[] flag = new int[numProcessors];
	int[] turn = new int[numProcessors-1];
	
	//This is the only broadcastSystem available
	BroadcastSystem broadcastSystem = BroadcastSystem.getInstance();
	
	//start all processors
	for(int id = 0; id < numProcessors; id++){
		new Processor(id, flag, turn);
	}
    
    
//    while(true){
//    	
//    }
  }
}
