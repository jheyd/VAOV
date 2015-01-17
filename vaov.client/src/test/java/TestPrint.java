import java.io.FileOutputStream;
import java.io.IOException;

import org.junit.Test;

import vaov.client.account.Account;
import vaov.client.util.KeyException;
import vaov.client.util.PrintService;

public class TestPrint {

	@Test
	public void testPrintAccount() throws KeyException, IOException {
		Account a = new Account(
				"i9YMFc+vWTpO3B5hxwsg80UvHX6sGUKdhyly7RQOfg230UZfaVfN6fqHMtfb3bxydpRUEuQvgKgW450FwzrSww==");
		FileOutputStream out = new FileOutputStream("test_files/account.pdf");
		PrintService.print(out, a.getHash());
		out.close();
	}

}
