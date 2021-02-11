package database;

import java.sql.Date;
import java.sql.Time;

public class Message {
	
	// The message sender port 
	private int src;
	// The message receiver port 
	private int dst;
	// The message time in ms (database time)
	private long time;
	// The message content
	private String content;
	
	/**
	 * Create a Message
	 * @param src the sender port number 
	 * @param dst the receiver port number 
	 * @param time the message time (in ms)
	 * @param content the message content
	 */
	public Message(int src, int dst, long time, String content) {
		super();
		this.src = src;
		this.dst = dst;
		this.time = time;
		this.content = content;
	}
	
	/**
	 * 
	 * @return the sender port number
	 */
	public int getSrc() {
		return src;
	}
	
	/**
	 * 
	 * @return the receiver port number
	 */
	public int getDst() {
		return dst;
	}
	
	/**
	 * 
	 * @return the message content
	 */
	public String getContent() {
		return content;
	}
	
	/**
	 * 
	 * @return the message time
	 */
	public long getTime() {
		return this.time;
	}

	@Override
	public String toString() {
		return "Message [from:" + src + ", to:" + dst + ", at" + new Date(time) + " " + new Time(time) + " : " + content + "]\n";
	}
	
	
	
}
