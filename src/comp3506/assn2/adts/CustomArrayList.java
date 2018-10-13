package comp3506.assn2.adts;

/**
 * This Class is an Array based list, that will hold objects of any particular
 * type.
 * 
 * Memory Usage:
 * This Data Structures space complexity comes down to how many big the elements array is.
 * Since this is still a list structure it can be said that the space complexity is n where
 * n is the amount of elements stored. But since this array uses a doubling strategy to resize
 * when it becomes half full, elements maybe significantly bigger then the amount of objects stored
 * in it, but this will still give the structure a space complexity of n. So the O value for the space
 * of this structure is:
 * 
 * O(n)
 * 
 * @author David Riddell
 *
 * @param <T> The object type to be stored
 */
public class CustomArrayList<T> {
    private int size;
    private int pointer;
    private Object[] elements;

    
    /**
     * Creates a new ArrayList
     * 
     * RunTime:
     * 3 primitive operations(constant time) occur so it can be said that this
     * method has a runtime efficiency of:
     * 
     * O(1)
     */
	public CustomArrayList(){
        this.size = 10; //1
        this.pointer = 0; //1
        this.elements = new Object[this.size]; //1
    }
	
	/**
	 * Creates an ArrayList with a specified size so that if the
	 * size of what is being stored is know resizing of the list
	 * can be avoided.
	 * 
	 * Run-Time:
	 * Since only 3 constant primitive operations occur it can
	 * be said that this method has a run-time efficiency of:
	 * 
	 * O(1)
	 * 
	 * @param capacity Size to make the internal array.
	 */
	public CustomArrayList(int capacity){
        this.size = capacity; // 1
        this.pointer = 0;//1
        this.elements = new Object[capacity];//1
    }
	
	/**
	 * Returns the amount of things that are currently being stored
	 * within the list.
	 * 
	 * RunTime:
	 * Only 1 primitive operation occurs so this method runs in constant
	 * time giving it a O value of:
	 * 
	 * O(1)
	 * 
	 * @return the amount of things that are being stored within
	 * 			the list;
	 */
    public int size(){
        return this.pointer; //1
    }
    
    /**
     * Returns an Object[] with all the current elements within the list
     * if nothing is stored at an index of the list null will be returned
     * since it is an Object[]. Note if this method is to be used knowing what
     * type is being stored within the array is recommended since casting the value
     * that is returned from an index within the array maybe useful.
     * 
     * RunTime:
     * Only one primitive operation occurs and that is returning the array
     * so there is a constant runtime for this method in the worst case giving
     * the method a Big-O value of:
     * 
     * O(1)
     * 
     * @return Object[] of all the elements stored within the list.
     */
	public Object[] toArray() {
    	return this.elements;//1
    }
	
	/**
	 * Checks to see if the List is storing any elements currently
	 * 
	 * RunTime:
	 * Since in the worst case of this method running only 2 primitive
	 * operations occur a constant runtime of this method happens, therefore
	 * the O value for this methods runtime is:
	 * 
	 * O(1)
	 * 
	 * @return
	 */
    public boolean isEmpty() {
        if (this.pointer == 0) { //1
            return true; //1
        }
        return false;//1
    }
    
    /**
     * Adds a new element to the list.
     * 
     * RunTime:
     * In the worst case of add resize has to be called which has
     * and O(n) runtime so in the worst case of this method call 
     * there is an O(n) runtime, but in cases where there is no
     * need to resize the array there is just a constant runtime.
     * this method in the worst case has a big-O value of:
     * 
     * O(n)
     * 
     * @param object Element to be added to the array.
     */
    public void add(T object) {
        if (this.pointer == this.size/2) { //2
            this.resize(); //1 for the function call + O(n) runtime of resize
        }
        this.elements[this.pointer] = object; //2 index and assignment
        this.pointer++; //2 increment and assignment
    }

    
    /**
     * Checks to see if the List is storing a particular element by
     * checking the .equals of all the items, if they match true is
     * returned.
     * 
     * @param object Element to be checked to see if the List is already
     * 		storing it.
     * @return True if object is already being stored in the List
     * 		Otherwise false.
     */
    public boolean contains(T object) {
        for (int i = 0; i < this.pointer; i++) {
            if (object.equals(this.elements[i])){
                return true;
            }
        }
        return false;
    }
    
    /**
     * Utility class to resize the background array when to many elements
     * are added. Allows the DataStructure to seem like there is no
     * maximum on the things it can store.
     * 
     * RuntTime:
     * O(n)
     */
    private void resize() {
        this.size = this.size*2; //2
		Object[] temp = new Object[this.size]; //2
        for (int i = 0; i < this.size/2; i++) { // i
            temp[i] = this.elements[i]; 
        }
        this.elements = temp; //1
    }

}
