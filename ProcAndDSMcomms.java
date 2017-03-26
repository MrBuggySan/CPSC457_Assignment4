public class ProcAndDSMComms{
  private int index;
  private int value;
  private boolean doALoad;
  private boolean doAWrite;

  public ProcAndDSMComms(){
    doALoad = false;
    doAWrite = false;
    finishedLoad = false;
  }

  private void setLoadCommand(){
    doALoad = true;
    doAWrite = false;
  }

  private void setWriteCommand(){
    doALoad = false;
    doAWrite = true;
  }

//used by processor only
  public synchronized void doALoad(int index){
    this.index = index;
    finishedLoad = false;
    setLoadCommand();
  }


//used by DSM only
  public synchronized boolean doILoad(){
    return doALoad;
  }

  public synchronized boolean doIStore(){
    return doAWrite;
  }

//Either processor or dsm can use these
  public synchronized int getIndex(){return index;}

  public synchronized int getValue(){return value;}

  public synchronized void setValue(){return value;}
}
