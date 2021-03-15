package noticeboard;

import java.io.Serializable;

public class PostControl implements Serializable {
	Post[] postList = new Post[0];

	void loadPostList(Post[] loadList) {
		postList = loadList;
	}

	Post[] getPostList() {
		Post[] tempList = postList;
		return tempList;// 직접적인 수정 방지
	}
	
	void add(Post post) {
		Post[] temp = new Post[postList.length + 1];
		for (int i = 0; i < postList.length; i++) {
			temp[i] = postList[i];
		}
		temp[postList.length] = post;
		postList = temp;
	}

	void delete(int idx) { // 2 //5
		for (int i = idx; i < postList.length; i++) {
			if (i + 1 == postList.length) {
				postList[i] = null;
				break;
			}
			postList[i+1].postidx=i+1;
			postList[i] = postList[i + 1];
		}
		Post[] temp=new Post[postList.length-1];
		for(int i=0; i<postList.length-1;i++) {
			temp[i]=postList[i];
		}
		postList=temp;
	}

	int getSize() {
		return postList.length;
	}

	void printNoticeBoard(int idx) {
		System.out.println(postList[idx].postidx + "\t" + postList[idx].title + "\t" + postList[idx].writerName);
	}

	void printPost(int idx) {
		System.out.println(postList[idx].content);
	}

	String getPostId(int idx) {
		return postList[idx].writerId;
	}
}