#include <iostream>
#include <vector>
#include <map>
#include <algorithm>

using namespace std;


struct Process {
    int arrT, burstT, priority;
    int turnAroundT, waitT, remainT, pid;

    bool operator<(Process const & b) const {
        return arrT < b.arrT;
    }
};


class Scheduling {
private:
    int nProcess;
    int totalSteps;
    vector<Process> processes;
    vector<Process> availProcess;
    vector< int > util;
    float totalBurst;
    map <int, vector <Process> > arrivals;
    map <int, vector <Process> >::iterator it;

public:
    void input() {
        cout << "Enter number of processes: " << endl;
        cin >> nProcess;

        cout << "Enter arrival, burst and priority of each: " << endl;
        totalBurst = 0;
        for (int i = 0; i < nProcess; ++i) {
            Process process;
            cin >> process.arrT;
            cin >> process.burstT;
            cin >> process.priority;

            process.remainT = process.burstT;
            process.pid = i;
            process.waitT = 0;
            process.turnAroundT = 0;
            
            processes.push_back(process);

            arrivals[process.arrT].push_back(process);
            totalBurst += process.burstT;
        }

        // util = new int[totalBurst];

    }

    void schedule() {
        sort(processes.begin(), processes.end());
        int count = 0;
        while (!(arrivals.empty() && availProcess.empty())) {
            util.push_back(-1);
            removeFinished();

            it = arrivals.find(count);
            if (it != arrivals.end()) {
                availProcess.insert(
                    availProcess.end(), arrivals[it->first].begin(), arrivals[it->first].end()
                );
                arrivals.erase(it);
            } 

            sort(availProcess.begin(), availProcess.end(), newPriority);
            int used = usedCpu(count);
            ageAndWait(used);
            count ++;
        }
        totalSteps = count - 1;
        cout << "Total Steps: " << totalSteps << endl;
    }

    
    void removeFinished() {
        vector< int > finished;
        for (int i = 0; i < availProcess.size(); ++i) {
            if (availProcess[i].remainT == 0)
                finished.push_back(i);
        }
        for (int i = 0; i < finished.size(); ++i) {
            int pid = availProcess[finished[i]].pid;
            processes[pid].waitT = availProcess[finished[i]].waitT ;

            availProcess.erase(availProcess.begin() + finished[i]);
        }
    }


    int usedCpu(int i) {
        if (availProcess.size() > 0){
            util[i] = availProcess[0].pid;
            availProcess[0].remainT--;
            return 1;
        }
        return 0;
    }

    void ageAndWait(int used) {
        for (int i = used; i < availProcess.size() ; ++i) {
            availProcess[i].waitT++;
            if (availProcess[i].waitT % 5 == 0) {
                availProcess[i].priority -- ;
            }
        }
        
    }

    static bool newPriority(const Process & a, const Process & b) {
        float alpha = 10;
        float beta = 1;
        return alpha*(float)a.priority + beta*(float)a.remainT < alpha*(float)b.priority + beta*(float)b.remainT; 
    }

    void calculations() {
        cout << "\nThe Gant chart of the processes if as follows: " << endl;
        for (int i = 0; i < util.size(); ++i) {
            cout << util[i] << "  ";
        }
        cout << endl;

        cout << "\nThe wait time and turn around time of each process is as follows: " << endl;

        float totalWait = 0;
        float totalTat = 0;
        for (int i = 0; i < nProcess; ++i) {
            processes[i].turnAroundT = processes[i].waitT + processes[i].burstT;

            totalWait += processes[i].waitT;
            totalTat += processes[i].turnAroundT;

            cout << "For process " << i << ", Wait Time: " << processes[i].waitT << ", TAT: " << processes[i].turnAroundT << endl;
        }

        cout << "\nAverage waiting time is: " <<  totalWait/nProcess << endl;
        cout << "Average TAT time is: " <<  totalTat/nProcess << endl;
        cout << "Throughput is: " <<  totalSteps/(float)nProcess << endl;




    }
    
};


int main() {
    Scheduling scheduling;
    scheduling.input();
    scheduling.schedule();
    scheduling.calculations();

    return 0;
}

/*
cout << "Check map: " << endl;
for (int i = 0; i < nProcess; ++i) {
    it = arrivals.find(processes[i].arrT);
    if (it != arrivals.end()) {
        cout << "For: "<< it->first << endl;
        cout << arrivals[it->first].size() << endl;;
    }
}
*/