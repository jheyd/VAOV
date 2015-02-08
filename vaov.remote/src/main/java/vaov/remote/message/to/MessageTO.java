package vaov.remote.message.to;

import javax.xml.bind.annotation.XmlRootElement;

import vaov.remote.services.KeyId;

@XmlRootElement
public class MessageTO {

	private KeyId author;

	private String digest;

	private String signature;

	private MessageContentTO content;

	public MessageTO() {
	}

	public MessageTO(KeyId author, String digest, String signature, MessageContentTO content) {
		super();
		this.author = author;
		this.digest = digest;
		this.signature = signature;
		this.content = content;
	}

	public KeyId getAuthor() {
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

	public void setAuthor(KeyId author) {
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

	@Override
	public String toString() {
		return "author: " + author + ", content: " + content + ", digest: " + digest + ", signature: " + signature;
	}
}
