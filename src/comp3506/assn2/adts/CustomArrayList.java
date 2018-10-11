package comp3506.assn2.adts;

public class CustomArrayList<T> {
    private int size;
    private int pointer;
    private T[] elements;

    @SuppressWarnings("unchecked")
	public CustomArrayList(){
        this.size = 10;
        this.pointer = 0;
        this.elements = (T[]) new Object[this.size];
    }
    @SuppressWarnings("unchecked")
	public CustomArrayList(int capacity){
        this.size = capacity;
        this.pointer = 0;
        this.elements =  (T[]) new Object[capacity];
    }

    public int size(){
        return this.pointer;
    }
    
    @SuppressWarnings("unchecked")
	public T[] toArray() {
    	T[] toArray = (T[]) new Object[this.pointer+1];
    	for (int i = 0; i < this.pointer; i++) {
    		toArray[i] = this.elements[i];
    	}
    	return toArray;
    }

    public boolean isEmpty() {
        if (this.pointer == 0) {
            return true;
        }
        return false;
    }

    public void add(T object) {
        if (this.pointer == this.size/2) {
            this.resize();
        }
        this.elements[this.pointer] = object;
        this.pointer++;
    }

    public boolean contains(T object) {
        for (int i = 0; i < this.pointer; i++) {
            if (object.equals(this.elements[i])){
                return true;
            }
        }
        return false;
    }
    
    @SuppressWarnings("unchecked")
    private void resize() {
        this.size = this.size*2;
		T[] temp = (T[]) new Object[this.size];
        for (int i = 0; i < this.size/2; i++) {
            temp[i] = this.elements[i];
        }
        this.elements = temp;
    }

}
