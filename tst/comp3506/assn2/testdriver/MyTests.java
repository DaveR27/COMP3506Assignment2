package comp3506.assn2.testdriver;


import java.io.FileNotFoundException;
import org.junit.Test;
import comp3506.assn2.application.AutoTester;

public class MyTests {
	
	public MyTests() {
	}
		
		@Test
		public void testSetUp() {
			try {
				AutoTester searchApplication = new AutoTester("files\\shakespeare.txt", "files\\shakespeare-index.txt", "files\\stop-words.txt");
				System.out.println(java.time.LocalTime.now());
			} catch (FileNotFoundException | IllegalArgumentException e) {
				System.out.println("Opening files failed!");
				e.printStackTrace();
			}
		}

}
