package aufgabe1;

public enum Port {
	SMTP, SMTPS;
	
	public int getPortNumber() {
		switch(this) {
		case SMTP:
			return 25;
		case SMTPS:
			return 465;
		default:
			return -1;
		}
	}
}
