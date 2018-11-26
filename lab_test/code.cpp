#include <iostream>

using namespace std;


int printArray(int array[], int n) {
    for (int i = 0; i < n; ++i) {
        cout << array[i] << " ";
    }
    cout << endl;
}

int main(int argc, char const *argv[]) {
    
    int n;
    cout << "Enter the size of Array" << endl;
    cin >> n;
    int array[n];

    cout << "Enter the array:" << endl;
    for (int i = 0; i < n; ++i) {
        cin >> array[i];
    }

    int min_loc = 0;
    int max_loc = n-1;

    while (min_loc < max_loc) {
        while(array[min_loc] == 0) {
            min_loc++;
        }
        while(array[max_loc] != 0) {
            max_loc --;
        }

        if (min_loc < max_loc) {
            int temp = array[min_loc];
            array[min_loc] = array[max_loc];
            array[max_loc] = temp;
        }
    }

    cout << "After First Iteration:" << endl; 
    printArray(array, n);



    // Second Iteration

    max_loc = n-1;

    while (min_loc < max_loc) {
        while(array[min_loc] == 1) {
            min_loc++;
        }
        while(array[max_loc] != 1) {
            max_loc --;
        }

        if (min_loc < max_loc) {
            int temp = array[min_loc];
            array[min_loc] = array[max_loc];
            array[max_loc] = temp;
        }
    }

    cout << "After Second Iteration:" << endl; 
    printArray(array, n);




    return 0;
}