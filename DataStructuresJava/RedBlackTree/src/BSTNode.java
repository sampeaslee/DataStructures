

class BSTNode<K,V> {
	enum Color {
		//Enum to keep track of the color of a node. 
		RED, BLACK;
	}
	    //Private fields of a BSTNode
		K key;
	    V value;
	    BSTNode<K,V> left;
	    BSTNode<K,V> right;
	    BSTNode<K,V> parent;
	    Color color;
	    
	    BSTNode(){
	    	
	    }

	    /*
	     * Constructor to create a new BSTNode
	     */
	    BSTNode(K key, V value, Color color,BSTNode<K,V>  leftChild, BSTNode<K,V> rightChild, BSTNode<K,V> parent) {
	        this.key = key;
	        this.value = value;
	        this.color = color;
	        this.left = leftChild;
	        this.right = rightChild;
	        this.parent = parent;
	    }
		/**
		 * Left rotate around a BSTNode
		 * @param node- rotated around
		 * @return reference to updated node 
		 */
		 BSTNode<K,V> leftRotate() throws KeyNotFoundException{
			BSTNode<K,V> temp = this.right;
			this.right = temp.left;
			temp.left = this;
			//Structure of the node changes so need to update the parent references 
			temp.parent = this.parent;
			this.parent = temp;
			if(this.right != null) {
			this.right.parent = this;
			}
			return temp;
		}	
		/**
		 * Right rotate around a BSTNode
		 * @param node- rotated around
		 * @return reference to updated node 
		 */	
		  BSTNode<K,V> rightRotate() throws KeyNotFoundException{
			BSTNode<K,V> temp = this.left;
			this.left = temp.right;
			temp.right = this;
			//Structure of the node changes so need to update the parent references 
			temp.parent = this.parent;
			this.parent= temp;	
			if(this.right != null) {
			this.right.parent = this;
			}
			return temp;
		} 

	}