package noticeboard;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Scanner;

public class Main {

	static Scanner sc = new Scanner(System.in);
	static UserControl uc;
	static PostControl pc;
	static boolean login;

	public static void main(String[] args) throws FileNotFoundException, IOException, ClassNotFoundException {
		pc = new PostControl();
		uc = new UserControl();
		login = false;
		uc.initCU();
		
		loopStart();
	}

	private static void loopStart() throws IOException, ClassNotFoundException {
		ObjectInputStream postIn = null;
		ObjectInputStream userIn = null;
		try {
			postIn = new ObjectInputStream(new FileInputStream("postList.ser"));
			userIn = new ObjectInputStream(new FileInputStream("userList.ser"));
			pc.loadPostList((Post[]) postIn.readObject());
			uc.loadUserList((User[]) userIn.readObject());
		} catch (FileNotFoundException e) {
			System.out.print("");
		}
		System.out.println("--bit 게시판 (ver.0)--");
		while (true) {
			System.out.println("수행할 명령어를 입력하세요.");
			if (login) {
				System.out.println("[1]Write [2]View [3]Loggout [4]deleteAccount .. [0]Exit");
			} else {
				System.out.println("[1]Write [2]View [3]Join [4]Login .. [0]Exit");
			}
			int cmd = sc.nextInt();
			sc.nextLine();
			if (login) {
				if (cmd >= 3) {
					cmd += 2;
				}
			} else {
				if (cmd == 5 || cmd == 6) {
					System.out.println("유효한 입력이 아닙니다.");
					continue;
				}
			}
			switch (cmd) {
			case 1:
				write();
				break;
			case 2:
				view();
				break;
			case 3:
				join();
				break;
			case 4:
				login();
				break;
			case 5:
				logout();
				break;
			case 6:
				delAccount();
				break;
			case 7:
				showAllUser();
				break;
			case 0:
				exit();
			default:
				System.out.println("유효한 입력이 아닙니다.");
			}
		}
	}

	private static void showAllUser() {
		uc.printAllUser();
	}

	private static void write() {
		String title = "";
		String content = "";
		String ans;
		if (!login) {
			System.out.println("로그인이 필요합니다 이전 화면으로 이동합니다.");
		} else {
			System.out.println("글 제목을 입력하세요: ");
			title = sc.nextLine();

			System.out.println("내용을 입력하세요: ");
			content = sc.nextLine();
			// 글 저장 y/n + else처리
			while (true) {
				System.out.println("글을 저장하시겠습니까?(y/n)");
				ans = sc.nextLine();
				if (ans.equals("y") || ans.equals("Y")) {
					Post post = new Post();

					post.title = title;
					post.content = content;
					post.postidx = pc.getSize()+1;
					post.writerId = uc.getCurrentUser().id;
					post.writerName = uc.getCurrentUser().name;
					pc.add(post);
					System.out.println("글이 성공적으로 저장되었습니다.");
					break;
				} else if (ans.equals("n") || ans.equals("N")) {
					System.out.println("저장하지않고 이전화면으로 돌아갑니다");
					break;
				} else {
					System.out.println("유효하지 않은 입력입니다.");
					continue;
				}
			}
		}
	}

	private static void view() {
		int num;
		String ans;
		if (pc.getSize() == 0) {
			System.out.println("게시글이 없습니다.");
		} else {
			System.out.println("글 번호\t글 제목\t글 작성자\n" + "--------------------------------");
			for (int i = 0; i < pc.getSize(); i++) {
				pc.printNoticeBoard(i);
			}
			System.out.println("조회할 글 번호를 입력하세요.");
			num = sc.nextInt()-1;
			sc.nextLine();
			try {
				pc.printPost(num);
				if (pc.getPostId(num).equals(uc.getCurrentUser().id)) {
					while (true) {
						System.out.println("글을 삭제하시겠습니까?(y/n)");
						ans = sc.nextLine();
						if (ans.equals("y") || ans.equals("Y")) {
							pc.delete(num);
							break;
						} else if (ans.equals("n") || ans.equals("N")) {
							break;
						} else {
							System.out.println("유효한 입력이 아닙니다.");
							continue;
						}
					}
				}
			} catch (ArrayIndexOutOfBoundsException e) {
				System.out.println("없는 글번호입니다.");
			}
		}
	}

	private static void join() {
		String name;
		String nameFormat = "^[가-힣]*$";
		String id = "";
		String pw;
		while (true) {
			System.out.print("회원가입을 위해 이름 입력하세요>");
			name = sc.nextLine();
			if (name.matches(nameFormat) && name != "") {
				break;
			} else {
				System.out.println("이름에 특수문자, 영어, 공란은 입력할 수 없습니다.");
				continue;
			}
		}

		boolean loop = true;
		while (loop) {
			System.out.print("회원가입을 위해 ID 입력하세요>");
			id = sc.nextLine();
			for (int i = 0; i < id.length(); i++) {
				if (Character.isLetter(id.charAt(i)) || ('0' <= id.charAt(i) && id.charAt(i) <= '9')) {
					loop = false;
				} else {
					System.out.println("ID에 특수문자와 공란은 입력할 수 없습니다.");
					loop = true;
					break;
				}
			}
			
			try {
			for (int i = 0; i < uc.getSize(); i++) {
				if (uc.getId(i).equals(id)) {
					System.out.println("중복된 ID 입니다.");
					loop = true;
					break;
				}
			}
			}catch (NullPointerException e) {
				System.out.print("");
				loop=false;
			}
		}
		while (true) {
			System.out.print("회원가입을 위해 Password 입력하세요>");
			pw = sc.nextLine();
			if (pw.length() >= 4 && pw.length() <= 10) {
				break;
			} else {
				System.out.println("패스워드는 4글자 이상 10글자 이하로만 입력가능합니다.");
				continue;
			}
		}
		// 객체생성
		User newUser = new User();
		newUser.id = id;
		newUser.password = pw;
		newUser.name = name;
		newUser.userNum = uc.getSize();
		uc.add(newUser);
		System.out.println("회원가입이 완료되었습니다.");
	}

	private static void logout() {
		login = false;
		uc.initCU();
		uc.deleteCurrentUser();
		System.out.println("로그아웃 되었습니다.");
	}

	private static void login() {
		String id, password;
		while (!login) {
			System.out.println("ID를 입력하세요");
			id = sc.nextLine();

			System.out.println("Password를 입력하세요");
			password = sc.nextLine();
			try {
				for (int i = 0; i < uc.getSize(); i++) {
					if (!id.equals(uc.getId(i)) || !password.equals(uc.getPassword(i))) {
						login = false;
						continue;
					} else {
						System.out.println("로그인 성공");
						uc.setCurrentUser(uc.getUser(i));
						login = true;
						break;
					}
				}
			} catch (NullPointerException e) {
				System.out.print("");
			}
			if (!login) {
				System.out.println("로그인에 실패했습니다. 메인으로 돌아갑니다.");
				break;
			}
		}
	}

	private static void delAccount() {
		String ans;
		System.out.println("정말로 탈퇴하시겠습니까?(y/n)");
		ans = sc.nextLine();
		if (ans.equals("y") || ans.equals("Y")) {
			uc.delete(uc.getCurrentUser().userNum);
			login = false;
			uc.initCU();
			System.out.println("회원탈퇴가 완료되었습니다.");
		} else if (ans.equals("n") || ans.equals("N")) {
			System.out.println("메인메뉴로 돌아갑니다.");
		} else {
			System.out.println("유효한 입력이 아닙니다.");
		}
	}

	private static void exit() throws FileNotFoundException, IOException {
		ObjectOutputStream userOut = new ObjectOutputStream(new FileOutputStream("userList.ser"));
		ObjectOutputStream postOut = new ObjectOutputStream(new FileOutputStream("postList.ser"));
		userOut.writeObject(uc.getUserList());
		postOut.writeObject(pc.getPostList());
		userOut.close();
		postOut.close();
		System.exit(0);
	}
}
