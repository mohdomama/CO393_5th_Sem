#include <iostream>
#include <vector>
#include <bitset>
#include <boost/dynamic_bitset.hpp>

using namespace std;


class Tabulation
{
private:
	vector<int> minInt; // min terms in decimal
 	vector<boost::dynamic_bitset<> > minBin; // min terms in binary
 	int nBits;  // no of variables
 	vector<vector<boost::dynamic_bitset<> > > table;
 

public:
	void initialise() {
		cout << "Enter number of bits:" << endl;
		cin >> nBits;

		int nMin;
		cout << "Enter number of min terms:" << endl;
		cin >> nMin;

		cout << "Enter min terms:" << endl;
		for (int i = 0; i < nMin; ++i){
			int x;
			cin >> x;
			minInt.push_back(x);
			minBin.push_back(boost::dynamic_bitset<> (nBits, x));
		}

		/*
		for (int i = 0; i < nMin; ++i){
			cout<<minBin[i] << endl;
		}
		cout << minBin[0].size() << endl; // number of bits
		cout << minBin[0].count() << endl; // number of 1s
		*/

		for (int i = 0; i < nBits; ++i){
			vector<boost::dynamic_bitset<> > x;
			table.push_back(x);
		}
		table[0].push_back(minBin[0]);
		for (int i = 0; i < nBits; ++i){
			for (int j = 0; j < table[i].size(); ++j){
				cout << table[i][j] << endl;
			}
		}

	}	
};


int main()  {
	Tabulation tab;
	tab.initialise();
	return 0;
}