import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class VSPworsefit {
    public static int maxSize = 2000;
    public static int formerindex;
    public static long startTime;
    // public static int removedSize;
    // public static boolean status = true;
    public static CopyOnWriteArrayList<GernerateMap> temporary =
            new CopyOnWriteArrayList<>();

    public static ArrayBlockingQueue<Integer> inputQueue = new ArrayBlockingQueue<>(20);
    public static ArrayList<Integer> waitQueue = new ArrayList<>();
    public static AtomicInteger sum = new AtomicInteger();

    public static class TimmerThread implements Runnable{

        private static Object lock1 = new Object();
        @Override
        public void run() {
            TimerRemover();
        }
        public static void TimerRemover() {
            synchronized (lock1) {
                int previous = 0;
                while (true) {

                    if (!temporary.isEmpty()) {
                        for (GernerateMap gernerateMap : temporary) {
                            Calendar calendar = Calendar.getInstance();
                            int seconds = calendar.get(Calendar.SECOND);

                            if(previous < seconds) {
                                sum.addAndGet(1);
                            }
                            previous = seconds;
                            if (sum.get() >= gernerateMap.getTime() && !gernerateMap.getId().equals("0")) {
                                if (!"hole".contains(gernerateMap.getId())) {
                                    System.out.println("proccess id " + gernerateMap.getId() + " is now completed");
                                    gernerateMap.setId("hole");
                                    // maxSize += maxSize + gernerateMap.getEndIndex() + 1;
                                    Map();
                                }
                            }
                        }
                    }
                    //   System.out.println("Alive");
                }
            }
        }
    }



    public static void BF() throws InterruptedException {
        startTime = System.currentTimeMillis();
        new Thread(new TimmerThread()).start();

        int previousArrivaleTime = -1;
        for (int i = 0; i < Main.processes.length; i++) {
            int id = Main.processes[i].getProcessID();
            int Arrivaltime = Main.processes[i].getArrivalTime();
            int size = Main.processes[i].getProcessSize();
            int lifetime = Main.processes[i].getLifetime();

            if (previousArrivaleTime != Arrivaltime) {
                //UpdateFromWait();

                inputQueue.add(id);
                System.out.println("t = " + Arrivaltime + " Proccess " + id + " " + "arives");
                System.out.println("      Input Queue: " + inputQueue + "");
                boolean sample = UpdateAssign(id, size, lifetime);
                if (!sample) {
                    AssignSize(id, size, lifetime);
                }
                if (!inputQueue.isEmpty()) {
                    MemoryMap();
                }
                previousArrivaleTime = Arrivaltime;

            } else {
                inputQueue.add(id);
                System.out.println("      Proccess " + id + " arrives");
                previousArrivaleTime = Arrivaltime;
                System.out.println("      Input Queue: " + inputQueue + "");

                boolean sample = UpdateAssign(id, size, lifetime);
                if (!sample) {
                    AssignSize(id, size, lifetime);
                }
                if (!inputQueue.isEmpty()) {
                    MemoryMap();
                }

            }

        }
    }


    public static  void CombineHoles(){
        ArrayList<Integer> combine =  new ArrayList<>();
        int count = 0;
        int previous = 0;
        for(int i = 0; i < temporary.size(); i++){
            if(temporary.get(i).getId().equals("hole")){
                count++;
                combine.add(i);
                if(count >= 2) {
                    temporary.get(previous).setId("hole");
                    temporary.get(previous).setEndIndex(temporary.get(i).endIndex);
                    temporary.get(i).setId("0");
                }
            }else {
                count = 0;
            }
            previous =i;


        }
        System.out.println("size " + temporary.size());
    }

    public static void MemoryMap() throws InterruptedException {

        //    ArrayList<Integer> temporary = new ArrayList<>(20);
        if (!inputQueue.isEmpty()) {
            while (!inputQueue.isEmpty()) {
                int index = inputQueue.take();
                if (maxSize > Main.processes[index - 1].getProcessSize()) {
                    System.out.println("      MM moves Proccess " + index + " to memory");

                    System.out.println("      Input Queue " + inputQueue + "");
                }
                Map();
                // }
            }
        }
    }

    public static int findLargestHole(){
       int maxIndex =-1;
       int max =0;
        for(int i = 0; i < temporary.size(); i++){
            int sum = (temporary.get(i).getEndIndex()+1) - temporary.get(i).startIndex;

            if(sum > max && temporary.get(i).getId()=="hole"){
                max =sum;
                maxIndex = i;
            }
        }
        return maxIndex;
    }

    public static Boolean UpdateAssign(int processid, int size, int liftime) {
        boolean spaceAvailable = false;
        Calendar calendar = Calendar.getInstance();
        int count  =0;
        int index = findLargestHole();
        if(index != -1){
            temporary.get(index).setId("Proccess " + processid);
            int time = sum.get() + (liftime / 50);
            temporary.get(index).setTime(time);
            spaceAvailable = true;

        }

      /*  for (int i =0; i < temporary.size(); i++) {
            if (temporary.get(i).getEndIndex() >= (size) && temporary.get(i).getId().equals("hole")) {
                temporary.get(i).setId("Proccess " + processid);
                int time = sum.get() + (liftime / 50);
                temporary.get(i).setTime(time);
                spaceAvailable = true;

            }
           }
       */


        return spaceAvailable;
    }

    public static void AssignSize(int processid, int size, int liftime) throws InterruptedException {
        int startIndex = 0;
        int endIndex;
        String id;
        //removedSize = size;
        if (size < maxSize) {
            if (maxSize != 2000) {
                try {
                    startIndex = FindLargestVal() + 1;
                } catch (Exception e) {
                }

            }
            maxSize = maxSize - size;
            endIndex = startIndex + size - 1;
            id = "Proccess " + processid;
            formerindex = processid - 1;
            Calendar calendar = Calendar.getInstance();
            System.out.println(calendar.get
                    (Calendar.SECOND));
            int time = sum.get() + (liftime / 50);
            System.out.println(time);
            GernerateMap ob = new GernerateMap(startIndex, endIndex, "full", id, (time));
            temporary.add(ob);
            Thread.sleep(2000);
        } else {
            waitQueue.add(processid);
            System.out.println("       Added to waiting " + processid);
        }

    }


    public static void Map() {
        CombineHoles();
        for (GernerateMap gernerateMap : temporary) {
            String id = gernerateMap.getId();
            int start = gernerateMap.getStartIndex();
            int end = gernerateMap.getEndIndex();
            if(!gernerateMap.getId().contains("0")) {
                System.out.println("                " + " " + start + " " + end + " :  " + id);
            }
        }
        int setSize = temporary.size() - 1;
        int start = temporary.get(setSize).getEndIndex() + 1;
        if (start <= 2000) {
            int endfinal = 1999;
            System.out.println("                " + start + " " + endfinal + " :  hole");
        }
        System.out.println("Current size " + temporary.size());
    }

    public static int FindLargestVal() {
        int max = 0;
        for (GernerateMap gernerateMap : temporary) {
            if (gernerateMap.getEndIndex() > max && gernerateMap.getEndIndex() < 1999) {
                max = gernerateMap.getEndIndex();
                //   System.out.println (max);
            }
        }
        return max;
    }


    public static class GernerateMap {
        private int startIndex;
        private int endIndex;
        private String state;
        private String id;
        private long time;

        public GernerateMap(int startIndex, int endIndex, String state, String id, long time) {
            this.startIndex = startIndex;
            this.endIndex = endIndex;
            this.state = state;
            this.id = id;
            this.time = time;
        }


        public void setStartIndex(int startIndex) {
            this.startIndex = startIndex;
        }

        public void setEndIndex(int endIndex) {
            this.endIndex = endIndex;
        }

        public void setState(String state) {
            this.state = state;
        }

        public void setId(String id) {
            this.id = id;
        }

        public void setTime(long id) {
            this.time = id;
        }


        public int getStartIndex() {
            return startIndex;
        }

        public int getEndIndex() {
            return endIndex;
        }

        public String getState() {
            return state;
        }

        public String getId() {
            return id;
        }

        public long getTime() {
            return time;
        }


    }

}
