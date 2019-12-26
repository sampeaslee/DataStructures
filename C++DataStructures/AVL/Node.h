#pragma once
class Node
{
		int data, height; //Data about the node
		Node * right, * left;//Pointers to the children of the node
	public:
		Node(int);
		int getData();
		int getHeight();

		void setData(int);
		void setHeight(int);

		void setRight(Node *);
		void setLeft(Node *);

		Node* getRight();
		Node* getLeft();
		
};

