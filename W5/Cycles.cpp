#include <iostream>
#include <vector>

using namespace std;

class GraphCycles {
private:
    int **matrix; 
    int nNodes;
    vector< vector< int > > cycles;
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
        printCycles();        
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

    void printCycles() {
        cout << "\n\nCycles are:" << endl;
        for (int i = 0; i < cycles.size(); ++i) {
            for (int j = 0; j < cycles[i].size(); ++j) {
                cout << cycles[i][j] << "  ";
            }
            cout << endl;
        }
    }
};

int main(int argc, char const *argv[]){
    GraphCycles graphCycles;
    graphCycles.initialize();
    graphCycles.findCycles();
    return 0;
}