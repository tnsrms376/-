package noticeboard;

import java.io.Serializable;

public class UserControl implements Serializable{
	private User[] userList = new User[0];
	private User currentUser;
	
	void initCU() {
		User user=new User();
		user.id="";
		user.name="";
		user.password="";
		user.userNum=0;
		currentUser=user;
	}
	User getUser(int idx) {
		return userList[idx];
	}
	void loadUserList(User[] loadList) {
		userList=loadList;
	}
	
	User[] getUserList() {
		User[] tempList = userList;
		return tempList;//직접적인 수정 방지
	}
	
	void setCurrentUser(User user) {
		currentUser=user;
	}
	
	User getCurrentUser() {
		User user=currentUser;
		return user;
	}
	void deleteCurrentUser() {
		currentUser=null;
	}
	
	void add(User user) {
		User[] temp = new User[userList.length + 1];
		for (int i = 0; i < userList.length; i++) {
			temp[i] = userList[i];
		}
		temp[userList.length] = user;
		userList = temp;
	}

	void delete(int idx) { // 2 //5
		for (int i = idx; i < userList.length; i++) {
			if (i + 1 == userList.length) {
				userList[i] = null;
				break;
			}
			userList[i] = userList[i + 1];
		}
		User[] temp=new User[userList.length-1];
		for(int i=0; i<userList.length-1;i++) {
			temp[i]=userList[i];
		}
		userList=temp;
	}
	void printAllUser() {
		for (int i = 0; i < userList.length; i++) {
			System.out.println(userList[i]);
		}
	}
	int getSize() {
		return userList.length;
	}

	String getId(int idx) {
		return userList[idx].id;
	}

	String getPassword(int idx) {
		return userList[idx].password;
	}

}