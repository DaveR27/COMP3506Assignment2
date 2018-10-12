package comp3506.assn2.adts;

public class CustomArrayList<T> {
    private int size;
    private int pointer;
    private Object[] elements;


	public CustomArrayList(){
        this.size = 10;
        this.pointer = 0;
        this.elements = new Object[this.size];
    }
 
	public CustomArrayList(int capacity){
        this.size = capacity;
        this.pointer = 0;
        this.elements = new Object[capacity];
    }

    public int size(){
        return this.pointer;
    }
    
    
	public Object[] toArray() {
    	return this.elements;
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
    
    private void resize() {
        this.size = this.size*2;
		Object[] temp = new Object[this.size];
        for (int i = 0; i < this.size/2; i++) {
            temp[i] = this.elements[i];
        }
        this.elements = temp;
    }

}
