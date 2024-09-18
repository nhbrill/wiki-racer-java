/*
* AUTHOR: Nate Brill
* FILE: MaxPQ.java
* PURPOSE: To act as a queue of ladders categorizing priority as how many links
* are common between two wiki links.
*/
public class MaxPQ {
	private static final int DEFAULT_CAPACITY = 10;
    private Node[] queue;
    private int capacity;
    private int index; // Size of queue

    public MaxPQ() {
        /*
         * PURPOSE: To initialize an priority Queue by creating an array and
         * setting it's default size to 10 as well as setting the capacity to
         * 10, and the index to zero because the queue is empty.
         */
        this.queue = new Node[DEFAULT_CAPACITY];
        this.capacity = DEFAULT_CAPACITY;
        this.index = 0;
    }

    public void enqueue(String name, int priority) {
        /*
         * PURPOSE: Creates a node object and then add that node object
         * to the queue.
         * 
         * @param name, the name of the node.
         * 
         * @param priority, the priority the node has.
         */
        Node node = new Node(name, priority);
        enqueue(node);
    }

    public void enqueue(Node node) {
        /*
         * PURPOSE: Adds a node to the queue and then bubble it up to the
         * correct place in the priority queue where if it has smaller priority
         * then it's parent it is moved down.
         * 
         * @param node, the node object being added.
         */
        index += 1;
        if (this.index == capacity) {
            this.growQueue();
        }
        queue[index] = node;
        enqueueBubble(index, node);
    }

    public void enqueueBubble(Integer c, Node node) {
        /*
         * PURPOSE: To move a patient to its correct place in the priority
         * queue, where if it has a smaller priority then the parent object in
         * the queue it is moved closer to the front of the queue. Basically
         * bubbles down the node to the correct index.
         * 
         * @param c, the current index of the node.
         * 
         * @param node, the node being added.
         */
        for (int i = c; i > 0; i /= 2) {
            if (queue[i].priority < node.priority
                    || compares(queue[i], node)) {
                Node temp = queue[i];
                queue[i] = node;
                queue[c] = temp;
                c = c / 2;
            }
        }
    }

    public boolean compares(Node o, Node n) {
        /*
         * PURPOSE: Compares two nodes if they have the same priority by
         * which node is first alphabetically to determine priority between
         * two values with the same priority.
         * 
         * @param o, the node object in the queue.
         * 
         * @param n, the node object being moved.
         * 
         * @return boolean, true if it needs to be swapped false if not.
         */
        if (o.priority == n.priority) {
            if (o.name.compareTo(n.name) > 0) {
                return true;
            }
        }
        return false;
    }

    public String dequeue(){
        /*
         * PURPOSE: To remove the highest priority node in the queue and
         * then return the value of the node name.
         * 
         * @return name, the name of the node removed.
         */
        String name = queue[1].name;
        queue[1] = queue[index];
        index -= 1;
        dequeueBubble(1);
        return name;
    }

    public void dequeueBubble(Integer center) {
        /*
         * PURPOSE: Bubbles down a patient after removing a patient from the
         * queue to resort the queue by priority.
         * 
         * @param center, the index of the node needed to be sorted.
         */
        Integer left = 2 * center;
        Integer right = 2 * center + 1;

        if (left <= index) { // If at least the left node is not null.
            // Checks to see if the node needs to be swapped.
            if (queue[left].priority > queue[center].priority
                    || queue[right].priority > queue[center].priority
                    || !compares(queue[right], queue[center])
                    || !compares(queue[left], queue[center])) {
                // Swaps the patient with the left node in the binary heap
                // if it is the smallest child.
                if (queue[left].priority >= queue[right].priority) {
                    Node temp = queue[center];
                    queue[center] = queue[left];
                    queue[left] = temp;
                    center *= 2;
                    dequeueBubble(center);
                } else if (right >= index) { // If the right node exists.
                    // Swaps the node with the right node in the binary
                    // heap if it is the smallest child.
                    if (queue[right].priority > queue[left].priority) {
                        Node temp = queue[center];
                        queue[center] = queue[right];
                        queue[right] = temp;
                        center = 2 * center + 1;
                        dequeueBubble(center);
                    }
                }
            }
        }
    }

    public void changePriority(String name, int newPriority) {
        /*
         * PURPOSE: Changes the priority of a node in the queue and then
         * sorts the queue so the priorities are correct. To find the node
         * in the queue it checks all nodes.
         * 
         * @param name, the name of the node.
         * 
         * @param newPriority, the new priority of the node object.
         */
        for (int i = 1; i <= index; i++) {
            if (queue[i].name.equals(name)) {
                // Bubble down.
                if (queue[i].priority > newPriority) {
                    queue[i].priority = newPriority;
                    dequeueBubble(i);
                    // Bubble up
                } else if (queue[i].priority < newPriority) {
                    queue[i].priority = newPriority;
                    enqueueBubble(i, queue[i]);
                }
            }
        }
    }

    public String peek(){
        /*
         * PURPOSE: To return the highest priority node name in the queue.
         * 
         * @returns String, the name of the highest priority patient.
         */
        return this.queue[1].name;
    }

    public int peekPriority(){
        /*
         * PURPOSE: To return the highest priority node priority
         * in the queue.
         * 
         * @returns int, the priority of the highest priority node.
         */
        return this.queue[1].priority;
    }

    public boolean isEmpty() {
        /*
         * PURPOSE: Checks to see if the queue is empty.
         * 
         * @return boolean, true if empty false if not empty.
         */
        if (index == 0) {
            return true;
        } else {
            return false;
        }
    }

    public int size() {
        /*
         * PURPOSE: Returns the size of the queue.
         * 
         * @return index, the amount of elements in the queue.
         */
        return index;
    }

    public void clear() {
        /*
         * PURPOSE: Clears the queue of all values.
         */
        this.index = 0;
    }

    public String toString() {
        /*
         * PURPOSE: Represents the queue in string form putting all nodes
         * name and priority in a string and returning that string.
         * 
         * @return queueS, the queue as a string.
         */
        String queueS = "{";
        for (int i = 1; i <= index; i++) {
            queueS += queue[i].name + " (" + queue[i].priority + "), ";
        }
        queueS = queueS.trim();
        if (queueS.charAt(queueS.length() - 1) == ',') {
            queueS = queueS.substring(0, queueS.length() - 1);
        }
        queueS += "}";
        return queueS;
    }

    public void growQueue() {
        /*
         * PURPOSE: growQueue() grows the array if the capacity or size of the
         * array is less then the values trying to be added so more memory
         * needs to be allocated so the capacity is doubled.
         */
        Node[] newQueue = new Node[2 * this.capacity];
        for (int i = 0; i < this.index; i++) {
            newQueue[i] = this.queue[i];
        }
        this.queue = newQueue;
        this.capacity *= 2;
    }
	
	public class Node {
		/*
         * PURPOSE: A ladder object that keeps track of similarities and links.
         */
		public String name;
		// Similarity between the current link and target link.
	    public int priority;
	
	    public Node(String name, int priority) {
	        this.name = name;
	        this.priority = priority;
	    }
	}
}