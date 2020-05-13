
public class Process
{
    private int processID; // line 1
    private int arrivalTime; // line 2, 1st number
    private int lifetime; // line 2, 2nd number

    private int turnaround;
    private int processSize; // line 3, 1st number is the number of things to add, then add them all up
    private int[] segments; // line 3 alt for SEG, can ignore this if you're not using it

    private boolean active;

    public Process(int processID, int arrivalTime, int lifetime, int spaceReq, int[] segments)
    {
        this.processID = processID;
        this.arrivalTime = arrivalTime;
        this.lifetime = lifetime;
        this.processSize = spaceReq;
        this.segments = segments;
        this.active = false;
    }

    public int getProcessID()
    {
        return processID;
    }

    public int getArrivalTime()
    {
        return arrivalTime;
    }

    public int getLifetime()
    {
        return lifetime;
    }

    public int getProcessSize()
    {
        return processSize;
    }

    public boolean isActive()
    {
        return active;
    }

    public void activate()
    {
        active = true;
    }

    public void decrementLifetime()
    {
        if (active)
        {
            lifetime--;
        }
    }

    public void deactivate()
    {
        active = false;
    }

    public void printDebugMsg()
    {
        System.out.print("Process - ID: " + processID + " / Arrival Time: "
                + arrivalTime + " / Lifetime: " + lifetime + " / Total Size: " + processSize
                + " / Segments: [");
        for (int i = 0; i < segments.length - 1; i++)
        {
            System.out.print(segments[i] + ",");
        }
        System.out.println(segments[segments.length - 1] + "]");
    }
}