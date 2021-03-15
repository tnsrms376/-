package noticeboard;

import java.io.Serializable;

public class User implements Serializable{

	String name;
	String id;
	String password;
	int userNum;
	@Override
	public String toString() {
		String str="회원번호 :"+userNum+", 이름 :"+name+", ID :"+id;
		return str;
	}
}
