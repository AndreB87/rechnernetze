package aufgabe1;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import aufgabe1.Port;

public class MailProperty {
	
	public static final String USERNAME = "user";
	
	public static final String EMAIL = "email";
	
	public static final String HOSTNAME = "host";
	
	public static final String PORT = "port";
	
	public static final String BODY = "body";
	
	public static final String SUBJECT = "subject";
	
	public static Properties getMailUser(String fileS) throws FileNotFoundException, IOException {
		Properties p = new Properties();
		p.load(new FileInputStream(fileS));
		return p;
	}
	
	public static void setMailUser(String fileS, String name, String mail, String host, Port port) throws FileNotFoundException, IOException {
		Properties p = new Properties();
		p.put(USERNAME, name);
		p.put(EMAIL, mail);
		p.put(HOSTNAME, host);
		p.put(PORT, port.toString());
		p.store(new FileOutputStream(fileS), "");
	}

}
