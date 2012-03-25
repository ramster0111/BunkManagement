package core;

import java.util.*;
import java.io.*;

public class SubjectAttnRecord {
	public int subject;
	public int attended;
	public int total;

	public SubjectAttnRecord() {
		subject = 0;
		attended = 0;
		total = 0;
	}

	public SubjectAttnRecord(int sub) {
		subject = sub;
		attended = 0;
		total = 0;
	}

	public double getPer() {
		return (attended * 100.0) / total;
	}

	public byte[] toByteStream() throws IOException {
		ByteArrayOutputStream bout = new ByteArrayOutputStream();
		DataOutputStream dout = new DataOutputStream( bout );

		dout.writeInt(subject);
		dout.writeInt(attended);
		dout.writeInt(total);

		dout.close();
		return bout.toByteArray();
	}

	public void fromByteStream(byte[] data) throws IOException {
		ByteArrayInputStream bin = new ByteArrayInputStream(data);
		DataInputStream din = new DataInputStream( bin );

		subject = din.readInt();
		attended = din.readInt();
		total = din.readInt();

		din.close();
	}
}
