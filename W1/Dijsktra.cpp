#include <iostream>
#include <algorithm>

#include <bits/stdc++.h>

using namespace std;

class Dijsktra {
private:
	int **matrix;
	int size, source, dest;
	int *min_dis;
	bool *fixed;

public:
	void input() {
		cout << "Enter size" << endl;
		cin >> size;
		
		initialize();
		make_adj_mat();

		cout << "Enter source and destination:" << endl;
		cin >> source >> dest;
	}

	void calc_shortest(int point) {
		fixed[point] = true;

		if(point == dest) return;

		else {
			if(point == source) min_dis[point] = 0;
			
			for (int i = 0; i < size; ++i) {
				if (matrix[point][i] != 0 && !fixed[i]) {
					if (min_dis[i] != -1){
						min_dis[i] = min(min_dis[i], min_dis[point] + matrix[point][i]);
					}
					else {
						min_dis[i] = min_dis[point] + matrix[point][i];
					}
				}
				
			}
			point = get_next();
			calc_shortest(point);

		}

	}

	int get_next() {
		int min = 9999;
		int point = -1;
		for (int i = 0; i < size; ++i) {
			if (!fixed[i] && min_dis[i] != -1) {
				if (min_dis[i] < min ) {
					min = min_dis[i];
					point = i;
				}
			}
		}
		return point;
	}

	void make_adj_mat() {
		for (int i = 0; i < size; i++) {
			cout << "Enter connections of node " << i <<
			" (vert, weight). Put -1 at end." << endl;

			while(1) {
				int vert, weight;
				cin >> vert;
				if (vert == -1) break;

				cin >> weight;
				matrix[i][vert] = weight;
			}
		}
	}

	void initialize() {
		min_dis = new int[size];
		for (int i = 0; i < size; i++){
			min_dis[i] = -1;
		}

		fixed = new bool[10];
		for (int i = 0; i < size; ++i){
			fixed[i] = false;
		}

		matrix = new int *[size];
		for (int i = 0; i < size; i++){
			matrix[i] = new int[10];
		}

		for(int i = 0; i < size; i++) {
			for(int j = 0; j < size; j++) {
				matrix[i][j] = 0;
			}
		}
	}

	void print_min() {
		for (int i = 0; i < size; ++i) {
			cout << "Min dis of " << i << 
			" from source is " << min_dis[i] << endl;
		}
	}

	void print_mat() {
		for(int i = 0; i < size; i++) {
			for(int j = 0; j < size; j++) {
				cout << matrix[i][j] << "\t";
			}
			cout << endl;
		}
	}

	void print_path() {
		stack<int> path;
		int point = dest;

		path.push(point);
		while(point != source) {
			for (int i = 0; i < size; ++i){
				int inter_pt_dis = matrix[i][point];
				if (inter_pt_dis != 0 && min_dis[i] + inter_pt_dis == min_dis[point]){
					point = i;
					path.push(point);	
					break;
				}
			}
		}
		while(!path.empty()) {
			cout << path.top() << "  ";
			path.pop();
		}
		cout << endl;
	}

	int get_source() {
		return source;
	}

	~Dijsktra() {
		delete(matrix);
	}
	
};

int main() {
	Dijsktra dijsktra;
	dijsktra.input();

	cout << "\nThe adjacency matrix is:" << endl;
	dijsktra.print_mat();
	
	dijsktra.calc_shortest(dijsktra.get_source());
	
	cout << "\nThe minimum distaces:" << endl;
	dijsktra.print_min();

	cout << "\nThe path is:" << endl;
	dijsktra.print_path();

	return 0;
}