package noticeboard;

import java.io.Serializable;

public class Post implements Serializable{
	String title;
	String content;
	int postidx;
	String writerId;
	String writerName;
}