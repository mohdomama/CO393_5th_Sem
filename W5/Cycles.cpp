#include <iostream>
#include <vector>
#include <algorithm>

using namespace std;

class GraphCycles {
private:
    int **matrix; 
    int nNodes;
    vector< vector< int > > cycles;
    vector< vector< int > > filteredCycles;
    bool *marked;

public:
    void initialize() {
        cout << "Enter the number of nodes:" << endl;
        cin >> nNodes;

        matrix = new int*[nNodes];
        marked = new bool[nNodes];

        for (int i = 0; i < nNodes; ++i) {
            matrix[i] = new int[nNodes];
        }

        for (int i = 0; i < nNodes; ++i) {
            marked[i] = false;
        }

        cout << "Enter adjacency matrix: " << endl;
        for (int i = 0; i < nNodes; ++i) {
            for (int j = 0; j < nNodes; ++j) {
                    cin >> matrix[i][j];
            }
        }
    }

    void findCycles() {
        for (int i = 0; i < nNodes; ++i) {
            vector< int > nodeCycle;
            findCyclesFromNode(i, nodeCycle, i);
            clearNode(i);
        }

        sort(cycles.begin(),cycles.end(),compVectors);

        cout << "\n\nThe cycles are:" << endl;
        printCycles(cycles);        
    }   

    void filterCycles() {
        bool isSubSet[cycles.size()];
        for (int i = 0; i < cycles.size(); ++i) {
            isSubSet[i] = false;
        }

        for (int i = 0; i < cycles.size() - 1; ++i) {
            for (int j = i+1; j < cycles.size(); ++j) {
                if (!isSubSet[i]) {
                    vector<int> set = cycles[i];
                    vector<int> subSet = cycles[j];
                   
                    sort(set.begin(), set.end());
                    sort(subSet.begin(), subSet.end());

                    if ( includes(set.begin(), set.end(), subSet.begin(), subSet.end()) ) {
                        isSubSet[j] = true;
                    }
                }
            }
        }

        for (int i = 0; i < cycles.size(); ++i) {
            if(!isSubSet[i]) {
                filteredCycles.push_back(cycles[i]);
            }
        }

        cout << "\n\nThe filtered cycles are:" << endl;
        printCycles(filteredCycles);

    } 

    void findCyclesFromNode(int node, vector< int > nodeCycle, int present) {
        nodeCycle.push_back(present);
        marked[present] = true;

        for (int i = 0; i < nNodes; ++i) {
            if (matrix[present][i] == 1) {
                if ( i == node) {
                    cycles.push_back(nodeCycle);
                }
                else if (marked[i] == false) {
                    findCyclesFromNode(node, nodeCycle, i);
                }
            }
        }
        marked[present] = false;
        nodeCycle.pop_back();
    }

    void clearNode(int node) {
        for (int i = 0; i < nNodes; ++i) {
            matrix[node][i] = 0;    
            matrix[i][node] = 0;    
        }
    }

    static bool compVectors(const vector<int> & a,const vector<int> & b) {
        return a.size() > b.size();
    }

    void printCycles(vector< vector< int > > cycles) {
        for (int i = 0; i < cycles.size(); ++i) {
            for (int j = 0; j < cycles[i].size(); ++j) {
                cout << cycles[i][j] << " --> ";
            }
            cout << cycles[i][0];
            cout << endl;
        }
    }
};

int main(int argc, char const *argv[]){
    GraphCycles graphCycles;
    graphCycles.initialize();
    graphCycles.findCycles();
    graphCycles.filterCycles();

    return 0;
}