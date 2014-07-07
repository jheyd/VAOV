import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StringWriter;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import vaov.client.frontend.control.Client;

public class TestClient {

	Client cli;
	StringWriter output;
	
	@Before
	public void before(){
		try {
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("topics.dat")));
			bw.write("{\"data\":{\"list\":[{\"tags\":[\"Gartengestaltung\"],\"inis\":[{\"text\":\"Ich finde, dass Blumen schön sind\",\"caption\":\"Blumen sind schön\"},{\"text\":\"Ich finde, dass Blumen zu viel Arbeit machen\",\"caption\":\"Blumen machen Arbeit\"}]}]}}");
			bw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		cli = new Client();
		output = new StringWriter();
		cli.setOutput(new PrintWriter(output));
	}
	
	@After
	public void after(){
		cli.shutdown();
	}
	
	@Test
	public void testListTopics(){
		String testfall = "1.0 listTopics aufrufen";
		try {
			cli.execute(new String[]{"listTopics"});
			assertEquals("TOP0: Blumen sind schön\n",output.getBuffer().toString());
		} catch (Exception e) {
			fail(testfall + " " + e);
		}
	}
	
	@Test
	public void testShowTopic(){
		String testfall = "2.0 showTopic aufrufen mit Parametern im gueltigen Format";
		try {
			cli.execute(new String[]{"showTopic","TOP0"});
			String[] buf = output.getBuffer().toString().split("\n");
			assertEquals("Ini 1: Blumen sind schön",buf[0]);
			assertEquals("Ini 2: Blumen machen Arbeit",buf[1]);
			assertEquals("Tags: Gartengestaltung",buf[2]);
		} catch (Exception e) {
			fail(testfall + " " + e);
		}
	}
	
	@Test
	public void testShotTopicBadIndex(){
		String testfall = "2.1 showTopic aufrufen mit Parametern mit unpassendem Index";
		try {
			String[] badParams_Index = new String[]{"TOP1"};
			for(int i=0; i<badParams_Index.length; i++)
				cli.execute(new String[]{"showTopic",badParams_Index[i]});
			String[] buf = output.getBuffer().toString().split("\n");			
			for(int i=0; i<badParams_Index.length; i++){
				assertEquals(buf[i],"Topic index too large!");
			}
		} catch (Exception e) {
			fail(testfall + " " + e);
		}
	}
	
	@Test
	public void testShowTopicBadFormat() {
		String testfall = "2.2 showTopic aufrufen mit Parametern in ungueltigem Format";
		try {
			String[] badParams_Format = new String[]{"0","1","TOP","foo"};
			for(int i=0; i<badParams_Format.length; i++)
				cli.execute(new String[]{"showTopic",badParams_Format[i]});
			String[] buf = output.getBuffer().toString().split("\n");			
			for(int i=0; i<badParams_Format.length; i++){
				assertEquals(buf[i],"\"" + badParams_Format[i] + "\" is not a valid Topic ID");
			}
		} catch (Exception e) {
			fail(testfall + " " + e);
		}
	}
	
	@Test
	public void testShowIni(){
		String testfall = "3.0 showIni aufrufen mit guten Parametern";
		try {
			// correct
			cli.execute(new String[]{"showIni","INI0.0"});
			cli.execute(new String[]{"showIni","INI0.1"});
			String[] buf = output.getBuffer().toString().split("\n");			
			assertEquals(buf[0], "Blumen sind schön: Ich finde, dass Blumen schön sind");
			assertEquals(buf[1], "Blumen machen Arbeit: Ich finde, dass Blumen zu viel Arbeit machen");
		} catch (Exception e) {
			fail(testfall + " " + e);
		}
	}
	
	@Test
	public void testShowIniBadIndex(){
		String testfall = "3.1 showIni aufrufen mit Parametern mit unpassendem Index";
		try {
			String[] badParams_Index = new String[]{"INI1.0","INI1.1"};
			for(int i=0; i<badParams_Index.length; i++)
				cli.execute(new String[]{"showIni",badParams_Index[i]});
			String[] buf = output.getBuffer().toString().split("\n");			
			for(int i=0; i<badParams_Index.length; i++){
				assertEquals(buf[i],"Topic or Ini index too large!");
			}
		} catch (Exception e) {
			fail(testfall + " " + e);
		}
	}
	
	@Test
	public void testShowIniBadFormat() {
		String testfall = "3.2 showIni aufrufen mit Parametern in ungueltigem Format";
		try {
			String[] badParams_Format = new String[]{"0.0","0.1","1.0","1.1",
				"foo","INI","INI0","INI0.","INI1","INI1.","INI.0","INI.1","INI.","INI."};
			for(int i=0; i<badParams_Format.length; i++)
				cli.execute(new String[]{"showIni",badParams_Format[i]});
			String[] buf = output.getBuffer().toString().split("\n");			
			for(int i=0; i<badParams_Format.length; i++){
				assertEquals(buf[i],"\"" + badParams_Format[i] + "\" is not a valid Ini ID");
			}
		} catch (Exception e) {
			fail(testfall + " " + e);
		}
	}
}
