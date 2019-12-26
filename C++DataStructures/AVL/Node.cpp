#include "Node.h"
#include <iostream>


Node:: Node(int d) {
	data = d;
	height = 0;
	right = nullptr;
	left = nullptr;
}

int Node::getData() {
	return data;
}

int Node::getHeight() {
	return height;
}

void Node::setData(int newData) {
	data = newData;
}
void Node::setHeight(int newHeight) {
	height = newHeight;
}

void  Node::setRight(Node * n) {
	right = n;
}

void Node::setLeft(Node* n) {
	left = n;
}

Node* Node:: getRight() {
	return right;
}
Node* Node::getLeft() {
	return left;
}
