public class Assignment4{
	public static final int numProcessors = 10;
	private static int numReady = 0;

	public static synchronized void incrementNumReady(){
		numReady++;
	}

	public static synchronized boolean isReady(){
		return (numReady == 10)?  true : false;
	}

  public static void main(String[] args){
    //initialize everything
	int[] flag = new int[numProcessors];
	int[] turn = new int[numProcessors-1];

	//This is the only broadcastSystem available
	BroadcastSystem broadcastSystem = BroadcastSystem.getInstance(numProcessors);
	Thread broadcastSysThread = new Thread(broadcastSystem);
//	broadcastSystem.setValue(990);
	broadcastSysThread.start();

	Thread[] procThreadList = new Thread[numProcessors];
	//start all processors
	for(int id = 0; id < numProcessors; id++){
		procThreadList[id] = (new Processor(id, flag, turn, broadcastSysThread, broadcastSystem)).startThread();
	}

	while(!isReady()){

	}

	for(int id = 0; id < numProcessors; id++){
		procThreadList[id].interrupt();
	}
	//start all the processor at the same time

  }
}
