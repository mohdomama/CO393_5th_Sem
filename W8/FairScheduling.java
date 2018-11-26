import java.util.*;


public class FairScheduling {
    
    class Process {
        int bTime, pid, rTime, priority, VRT = 0;

    }

    private int nProcesses;
    private ArrayList<Process> procList;
    private int quantum, tSlice;
    private Scanner input = new Scanner(System.in);

    public void takeInput() {
        System.out.println("Provide quantum size and number of processes:");
        quantum = input.nextInt();
        nProcesses = input.nextInt();

        procList = new ArrayList<Process>();

        System.out.println("Provide burst, priority of all processes:");        
        for (int i = 0; i < nProcesses; i++) {
            Process newProcess = new Process();
            newProcess.bTime = input.nextInt();
            newProcess.priority = input.nextInt();
            newProcess.rTime = newProcess.bTime;
            newProcess.pid = i;
            procList.add(newProcess);
        }
    }

    public void removeUsed() {
        boolean[] finished = new boolean[procList.size()];
        for(int i = 0; i < procList.size(); i++) {
            finished[i] = false;
        }

        for (int i = 0; i < procList.size(); i++) {
            if (procList.get(i).rTime <= 0){
                finished[i] = true;
            }
        }

        for (int i = 0; i < procList.size(); i++) {
            if (finished[i] == true){
                procList.remove(i);
            }
        }
    }

    public void priortize() {
        tSlice = quantum / procList.size();

        for (int i = 0; i < procList.size(); i++) {
            procList.get(i).VRT += tSlice * procList.get(i).priority;
        }

        /*
        procList.sort(new Comparator<Process>() {
            public int compare(Process p1, Process p2) {
                return p1.VRT < p2.VRT ? 1:0;
            }
        });
        */

        Collections.sort(procList, new Comparator<Process>() {
            @Override 
            public int compare(Process p1, Process p2) {
                return p1.VRT- p2.VRT;
            }

        });

    }

    public void printList() {
        System.out.print("Present List:");
        for (int i = 0; i < procList.size(); i++) {
            System.out.print(procList.get(i).pid + 1 + "  ");
        }
        System.out.println();
    }

    public void utilizeCPU() {
        for (int i = 0; i < procList.size(); i++) {
            System.out.println(procList.get(i).pid + 1);
            procList.get(i).rTime -= tSlice;
        }
    }

    public void run() {
        priortize();
        printList();
        utilizeCPU();
        removeUsed();
        while(procList.size() > 0) {
            priortize();
            printList();
            utilizeCPU();
            removeUsed();
        }
    }



    public static void main(String[] args) {
        FairScheduling fairScheduling = new FairScheduling();
        fairScheduling.takeInput(); 
        fairScheduling.run();  
    }
}