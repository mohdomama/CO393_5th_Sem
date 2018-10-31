#include<bits/stdc++.h>
using namespace std;

int graph[100][100];
int vertices = 0;
int edges = 0;
vector<vector<int> > collection;

int already_visited(int x,vector<int> a) {
	for(int i=1;i<a.size()-1;i++) {
		if(x==a[i])
			return 1;
	}
	return 0;
}

void search(int finding,int current,vector<int> path) {
	int i;
	path.push_back(current);

	for(i=0;i<vertices;i++) {
		if(graph[current][i] == 1) {
			if(already_visited(i,path)!=1) {
				if(i==finding) {
					path.push_back(finding);
					collection.push_back(path);
					return;
				}
				else {
					search(finding,i,path);
				}
			}
		}
	}
}

void find_all_cycles() {
	//cout<<"Here"<<endl;
	int i;
	for(i=0;i<vertices;i++) {
		vector<int> path;
		search(i,i,path);
	}
}

void disp(vector<int> a) {
	int i;
	for(i=0;i<a.size();i++)
		cout<<a[i];
	cout<<endl;
}

int check(vector<int> a,vector<int> b) {
	int i,j,k;

	if(a.size()<b.size()) {
		for(i=0;i<a.size();i++) {
			int flag = 0;
			for(j=0;j<b.size();j++) {
				if(a[i]==b[j])
					flag=1;
			}
			if(flag==0)
				return 0;
		}
	}
	else {
		for(i=0;i<b.size();i++) {
			int flag = 0;
			for(j=0;j<a.size();j++) {
				if(b[i]==a[j])
					flag=1;
			}
			if(flag==0)
				return 0;
		}
	}
	return 1;
}

int sameSubsetFound() {
	int i,j;

	for(i=0;i<collection.size()-1;i++) {
		for(j=i+1;j<collection.size();j++) {
			if(check(collection[i],collection[j])) {
				collection.erase(collection.begin()+j);
				return 1;
			}
		}
	}
	return 0;
}

void remove_subset_cycles() {
	if(collection.size()==0)
		return;
	while(sameSubsetFound()){

	}
}

int main(void) {

	int i,from,to,j;
	vector<vector<int> > all_cycles;

	cout<<"Enter number of vertices = ";
	cin>>vertices;

	cout<<"Enter number of edges = ";
	cin>>edges;

	for(i=0;i<edges;i++) {
		cin>>from;
		cin>>to;
		graph[from][to] = 1;
	}

	/*for(i=0;i<vertices;i++) {
		for(j=0;j<vertices;j++) {
			cout<<graph[i][j]<<" ";
		}
		cout<<endl;
	}*/

	find_all_cycles();
	remove_subset_cycles();

	cout<<"Paths are = "<<endl;
	for(i=0;i<collection.size();i++) {
		for(j=0;j<collection[i].size();j++) {
			cout<<collection[i][j]<<" ";
		}
		cout<<endl;
	}
	return 0;
}