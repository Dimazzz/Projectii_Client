package org.projii.commons;

public class TimeLogger {
    private static final long timeOfStart = System.currentTimeMillis();
    private static boolean active = true;

    public static void d(String... message) {
        if(active){
          System.out.print(System.currentTimeMillis() - timeOfStart);
          System.out.print(" > ");
          for (String s : message) {
              System.out.print(s);
              System.out.print(" ");
          }
          System.out.println();
       }
    }

    public static void setStatus(boolean active) {
        active = active;
    }

    public static boolean isActive() {
        return active;
    }
}
