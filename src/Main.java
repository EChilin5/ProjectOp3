import java.io.File;
import java.io.FileNotFoundException;
import java.util.Calendar;
import java.util.Scanner;

public class Main {

private int totalProccess;

public static Process[] processes;
    private void ReadFile(String file){

    }

    private static void RunCommand(int start){
        if(start == 1){
            BestFit();
        }else if(start == 2){
            FirstFit();
        }else if(start == 3){
            WorseFit();
        }
    }


    private static void BestFit(){

    }

    private static void FirstFit(){

    }

    private static  void WorseFit(){

    }

    private static Process[] readWorkloadFile(String inputFileName) throws FileNotFoundException
    {
        Scanner inputFile = new Scanner(new File(inputFileName));
        int processCount = inputFile.nextInt();
       processes = new Process[processCount];

        for (int i = 0; i < processCount; i++)
        {
            int processID = inputFile.nextInt();
            int arrivalTime = inputFile.nextInt();
            int lifetime = inputFile.nextInt();
            int segmentCount = inputFile.nextInt();

            // You should only be using 1 of these two, but to make it a bit easier to manage, here's both.
            int spaceReq = 0;
            int[] segments = new int[segmentCount];

            for (int j = 0; j < segmentCount; j++)
            {
                int nextInt = inputFile.nextInt();
                spaceReq += nextInt;
                segments[j] = nextInt;
            }

            processes[i] = new Process(processID, arrivalTime, lifetime, spaceReq, segments);
             processes[i].printDebugMsg(); // TEST LINE
        }

        inputFile.close();
        return processes;
    }
    
    public static void main(String[] args) throws FileNotFoundException, InterruptedException {
        readWorkloadFile("/Users/edgarchilin/Documents/IntelliJ Programs/ProjectOp3/src/in1.dat");
       // VSPbestFit.BF();
        VSPworsefit.BF();
      /*  int sum = 0;
        boolean state = true;
        int previous = 0;
        while (state){
            Calendar calendar = Calendar.getInstance();
            int seconds = calendar.get(Calendar.SECOND);
            if(previous < seconds) {
                sum = sum + 1;
            }
            System.out.println(sum +  " " + seconds);
            if(seconds >= 100){
                state = false;
            }
            previous = seconds;
        }

       */
    }
}
