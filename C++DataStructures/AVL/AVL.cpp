#include "AVL.h"
#include <iostream>
#include <queue>
#include <list>

AVL::AVL() {
	root = nullptr;
}
Node* AVL::insert(Node* n, Node* newNode) {
	if (n == nullptr) {
		return newNode;
	}

	if (n->getData() > newNode->getData()) {
		n->setLeft(insert(n->getLeft(), newNode));
	}
	else {
		n->setRight(insert(n->getRight(), newNode));
	}
}



void AVL::insert(Node* n) {
	if (root == nullptr) {
		root = n;
		return;
	}
	else {
		root = insert(root, n);
	}

}
bool AVL::lookup(Node* n, int key) {
	if (n == nullptr) {
		std::cout << "KeyNotFound";
		return false;
	}
	if (n->getData() == key) {
		std::cout << "KeyFound";
		return true;
	}

	if (n->getData() > key) {
		return lookup(n->getLeft(), key);
	}
	else {
		return lookup(n->getRight(), key);
	}
}

bool AVL::lookup(int key) {
	if (key == NULL) {
		return false;
	}

	return lookup(root, key);
}


void AVL::inorder(Node* n, std::list<int>& l)
{
	if (n == nullptr) {
		return;
	}
	inorder(n->getLeft(), l);
	l.push_back(n->getData());
	inorder(n->getRight(), l);
}

void AVL::printInorder() {
	std::list<int> inorder;
	std::list<int>::iterator it;
	AVL::inorder(root, inorder);
	for (it = inorder.begin(); it != inorder.end(); it++){
		std::cout << *it << ", ";		
	}
	std::cout << std::endl;
}



int main() {
	AVL avl;
	//Node node = 10;
	//Node* p = &node;
	Node nArray[] = { 11,12,9,8,7 };
	Node* pArray = nArray;
	avl.insert(pArray);
	avl.insert(pArray + 1);
	avl.insert(pArray + 2);
	avl.insert(pArray + 3);
	avl.insert(pArray + 4);
	avl.printInorder();
	std::cout << avl.lookup(99);
}