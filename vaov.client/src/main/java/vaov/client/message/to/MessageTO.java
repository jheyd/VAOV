package vaov.client.message.to;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class MessageTO {

	private String author;

	private String digest;

	private String signature;

	private MessageContentTO content;

	public MessageTO() {
	}

	public MessageTO(String author, String digest, String signature, MessageContentTO content) {
		super();
		this.author = author;
		this.digest = digest;
		this.signature = signature;
		this.content = content;
	}

	public String getAuthor() {
		return author;
	}

	public MessageContentTO getContent() {
		return content;
	}

	public String getDigest() {
		return digest;
	}

	public String getSignature() {
		return signature;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public void setContent(MessageContentTO content) {
		this.content = content;
	}

	public void setDigest(String digest) {
		this.digest = digest;
	}

	public void setSignature(String signature) {
		this.signature = signature;
	}
}
