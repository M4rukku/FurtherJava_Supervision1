package exercise3;

public class ThreadCreator {
  private static class UsefulParentClass {
    void doSomethingUseful() {
      System.out.println("Parent Class Job: I am being very useful.");
    }
  }

  private static interface BarkingInterface {
    default void bark() {
      System.out.println("Interface Job: Bark Bark!");
    }
  }

  static void createThreadByImplementingRunnable() {
    //Advantage of implementing Runnable is that we can still extend another parent class
    //e.g.
    class LocalRunnable extends UsefulParentClass implements Runnable, BarkingInterface  {
      public void run() {
        System.out.println("I am barking and being useful.");
        doSomethingUseful();
        bark();
      }
    }

    Thread thread = new Thread(new LocalRunnable());
    thread.start();
    try {
      thread.join();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    System.out.println("Closing Runnable Thread \n");
  }

  static void createThreadByExtendingThread() {
    //We cannot do this when extending Thread since Java does not allow for multiple inheritance
    //Though we can still implement multiple interfaces

    class LocalThread extends Thread implements BarkingInterface {
      @Override
      public void run() {
        System.out.println("I can bark, but I am not particularly useful.");
        bark();
      }
    }

    Thread extendedThread = new LocalThread();
    extendedThread.start();

    try {
      extendedThread.join();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    System.out.println("Closing Thread");
  }

  public static void main(String[] args) {
    System.out.println("Creating Thread using a Runnable: ");
    createThreadByImplementingRunnable();

    System.out.println("Creating Thread extending Thread: ");
    createThreadByExtendingThread();
  }
}
