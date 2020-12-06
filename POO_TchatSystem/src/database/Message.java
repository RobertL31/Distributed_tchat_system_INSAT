package database;

import java.sql.Date;
import java.sql.Time;

public class Message {
	
	private int src;
	private int dst;
	private long time;
	private String content;
	
	public Message(int src, int dst, long time, String content) {
		super();
		this.src = src;
		this.dst = dst;
		this.time = time;
		this.content = content;
	}
	
	public int getSrc() {
		return src;
	}
	public int getDst() {
		return dst;
	}
	public String getContent() {
		return content;
	}
	public long getTime() {
		return this.time;
	}

	@Override
	public String toString() {
		return "Message [from:" + src + ", to:" + dst + ", at" + new Date(time) + " " + new Time(time) + " : " + content + "]\n";
	}
	
	
	
}
