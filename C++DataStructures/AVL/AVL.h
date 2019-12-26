#pragma once
#include "Node.h"
#include <list>
class AVL{
	Node* insert(Node*, Node*);
	void inorder(Node* n, std::list <int>& l);
	bool lookup(Node*, int);
public:
	Node* root;
	AVL();
	void insert(Node*);
	void printInorder();
	bool lookup(int);
};

