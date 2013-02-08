package de.piratenpartei.id.test;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StringWriter;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.piratenpartei.id.frontend.control.Client;
import de.piratenpartei.id.frontend.control.WrongParameterCountException;

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
	public void testAll(){
		try {
			cli.execute(new String[]{"listTopics"});
			System.out.println(output.getBuffer().toString());
			System.out.println();

			cli.execute(new String[]{"showTopic","TOP0"});
			cli.execute(new String[]{"showTopic","TOP1"});
			cli.execute(new String[]{"showTopic","0"});
			cli.execute(new String[]{"showTopic","1"});
			cli.execute(new String[]{"showTopic","TOP"});
			cli.execute(new String[]{"showTopic","foo"});
			System.out.println(output.getBuffer().toString());
			System.out.println();

			cli.execute(new String[]{"showIni","INI0.0"});
			cli.execute(new String[]{"showIni","INI0.1"});
			cli.execute(new String[]{"showIni","INI1.0"});
			cli.execute(new String[]{"showIni","INI1.1"});
			cli.execute(new String[]{"showIni","0.0"});
			cli.execute(new String[]{"showIni","0.1"});
			cli.execute(new String[]{"showIni","1.0"});
			cli.execute(new String[]{"showIni","1.1"});
			cli.execute(new String[]{"showIni","foo"});
			cli.execute(new String[]{"showIni","INI"});
			cli.execute(new String[]{"showIni","INI0"});
			cli.execute(new String[]{"showIni","INI0."});
			cli.execute(new String[]{"showIni","INI1"});
			cli.execute(new String[]{"showIni","INI1."});
			cli.execute(new String[]{"showIni","INI.0"});
			cli.execute(new String[]{"showIni","INI.1"});
			cli.execute(new String[]{"showIni","INI."});
			cli.execute(new String[]{"showIni","INI."});
			System.out.println(output.getBuffer().toString());
			

		} catch (WrongParameterCountException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
