package Minesweeper;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.*;


public class Minesweeper extends JFrame {
	
	// ----------------------- [ Main Method ] ---------------------- //
	public static void main(String[] args) { new Minesweeper("지뢰찾기"); }
	// -------------------------------------------------------------- //
	
	
	final int SIZE=9;                               // 사이즈 : 9x9
	JButton[][] mineOrNot;                          // 클릭 버튼 배열 
	String[][] mineField = new String[SIZE][SIZE];  // 지뢰 위치 배열
	String[] whatIsMine = {"◎",""};                 // 지뢰 모양
	JPanel gamePane = new JPanel();                 // 지뢰찾기 게임이 이뤄지는 Panel
	JPanel recordPane = new JPanel();               // timePanel(스톱워치), restart(상태 및 재시작), flagPanel(찾은 지뢰 수)을 부착할 Panel 		
	
	JLabel time = new JLabel();                     // 스톱워치
	JPanel timePanel = new JPanel();                // time의 배경(패널)
	public Thread timer;                            // 스톱워치 작동 Thread
	boolean gameStart;                              // true일 때부터 시간 흐름(스톱워치 시작) - 좌우 클릭 상관없이 첫 클릭 후 true로 전환
	long startTime;                                 // 게임 시작 시간 - gameStart가 true가 되면 시작
	long endTime;                                   // 게임 종료 시간 - lose 또는 win이 true가 되면 종료
	static String timeRc;                           // 게임 종료 시 시간 계산 ( (endTime-startTime)/1000 )

	JButton restart = new JButton();                // 상태 표시(평시에는 노란색, 지뢰 밟으면 검은색) & 게임 중 재시작 버튼
	boolean lose;                                   // 패배 : 지뢰 밟으면 true로 변환
	boolean win;                                    // 승리 : 게임 승리 시 true로 전환

	JLabel findEmAll = new JLabel();                // 찾은 지뢰 수(gamePane 위의 ▲(우클릭) 갯수, 초기값 10 - 10개 이상 우클릭 시 0)
	JPanel flagPanel = new JPanel();                // findEmAll의 배경(패널)
	
    static String result;                           // 게임 결과(승리 : CONGRATULATIONS!!! / 패배 : AGAIN...?)
	JButton retry = new JButton();                  // 재시작 버튼
	JButton quit = new JButton();                   // 종료 버튼
	JFrame resultPane;                              // 게임 결과 출력 패널
	
	// ---------------------------- 멤버변수 정리 ---------------------------- //
	
	
	// --------------- 생성자, 쓰레드, 이벤트 리스너&핸들러, 메서드 ------------------ //
	
	// ----------------------- [ Constructor ] ----------------------- // begin 
	Minesweeper(String title){  // 게임 실행 윈도우 생성
		super(title);
		
		recordPane.setLayout(new FlowLayout(FlowLayout.CENTER, 90, 5));
		recordPane.setBackground(Color.LIGHT_GRAY);
		
		timePanel.setBackground(Color.BLACK);
		time.setFont(new Font("MS Gothic", Font.BOLD, 30));
		time.setForeground(Color.RED);
		timeClock();
		timePanel.add(time);
		recordPane.add(timePanel);
		
		restart.setBackground(Color.LIGHT_GRAY);
		restart.setText("●");
		restart.setForeground(Color.YELLOW);
		restart.setBorder(null);
		restart.setFont(new Font("MS Gothic", Font.BOLD, 45));
		restart.addActionListener(new MyActionListener());
		recordPane.add(restart);
		
		flagPanel.setBackground(Color.BLACK);
		findEmAll.setFont(new Font("MS Gothic", Font.BOLD, 30));
		findEmAll.setForeground(Color.RED);
		findEmAll.setText("010");
		flagPanel.add(findEmAll);
		recordPane.add(flagPanel);
		
		JPanel UpperPartPane = new JPanel();
		UpperPartPane.setLayout(new BorderLayout());
		UpperPartPane.add(recordPane, BorderLayout.NORTH);
		UpperPartPane.add(new JPanel(), BorderLayout.SOUTH);

		mineInstall();
		
		gamePane.setLayout(new GridLayout(SIZE,SIZE, 2, 2));
		mineOrNot = new JButton[SIZE][SIZE];
		Border raisedbevel = BorderFactory.createRaisedBevelBorder();
		// https://docs.oracle.com/javase/tutorial/uiswing/components/border.html
		for(int i=0; i<SIZE ; i++) {
			for(int j=0; j<SIZE ; j++) {
				mineOrNot[i][j] = new JButton(" ");
				mineOrNot[i][j].setFont(new Font("MS Gothic", Font.BOLD, 40));
				mineOrNot[i][j].setBackground(Color.LIGHT_GRAY);
				mineOrNot[i][j].setBorder(raisedbevel);
				mineOrNot[i][j].addActionListener(new MyActionListener());
				mineOrNot[i][j].addMouseListener(new MyActionListener());
				gamePane.add(mineOrNot[i][j]);
			}
		}
		
		getContentPane().setBackground(Color.LIGHT_GRAY);
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(UpperPartPane, BorderLayout.NORTH);
		getContentPane().add(gamePane, BorderLayout.CENTER);
		getContentPane().add(new JPanel(), BorderLayout.EAST);
		getContentPane().add(new JPanel(), BorderLayout.WEST);
		getContentPane().add(new JPanel(), BorderLayout.SOUTH);
		
		setBounds(500,200,500,600);
		setVisible(true);
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	} // END - MineSweeper(String title){}
	// ----------------------- [ Constructor ] ----------------------- // end
	
	public void MinesweeperResult(){  // 게임 결과(시간, 승패, 제작자) 출력 윈도우
		resultPane = new JFrame();
		resultPane.setBackground(Color.WHITE);
		resultPane.setLayout(new BorderLayout());
		
		// 게임 종료까지 걸린 시간
		JLabel tR = new JLabel();
		tR.setText(timeRc);
		resultPane.add(tR, BorderLayout.NORTH);
		
		// 게임 결과에 대한 Comment & 재시작-종료 버튼
		JPanel gameEnd = new JPanel();
		gameEnd.setLayout(new BorderLayout());
		
		JPanel showResult = new JPanel();
		showResult.setLayout(new GridLayout(2, 1));
		showResult.setBackground(Color.WHITE);
		
		JLabel comment = new JLabel();
		comment.setFont(new Font("MS Gothic", Font.BOLD|Font.ITALIC, 17));
		comment.setForeground(Color.BLUE);
		comment.setText(result);
		comment.setHorizontalAlignment(SwingConstants.CENTER);
		comment.setVerticalAlignment(SwingConstants.BOTTOM);
		showResult.add(comment);
		
		JPanel showButtons = new JPanel();
		showButtons.setBackground(Color.WHITE);
		
		Border raisedbevel = BorderFactory.createRaisedBevelBorder();
		retry.addActionListener(new MyActionListener());
		retry.setBackground(Color.LIGHT_GRAY);
		retry.setBorder(raisedbevel);
		retry.setText("   Retry   ");
		quit.addActionListener(new MyActionListener());
		quit.setBackground(Color.LIGHT_GRAY);
		quit.setBorder(raisedbevel);
		quit.setText("   Close   ");
		showButtons.add(retry);
		showButtons.add(quit);
		showResult.add(showButtons);
		
		JPanel emptyRoom = new JPanel(); //(위치 조정용)
		emptyRoom.setBackground(Color.WHITE);
		
		gameEnd.add(showResult, BorderLayout.CENTER);
		gameEnd.add(emptyRoom, BorderLayout.SOUTH);
		resultPane.add(gameEnd, BorderLayout.CENTER);
		
		// 제작자 정보
		JLabel createdBy = new JLabel();
		createdBy.setText("created by SiHoonChris");
		createdBy.setHorizontalAlignment(SwingConstants.RIGHT);
		// https://stackoverflow.com/questions/12589494/align-text-in-jlabel-to-the-right
		resultPane.add(createdBy, BorderLayout.SOUTH);
		
		resultPane.setBounds((int)(500*1.3), (int)(200*1.6), 200, 300);
		resultPane.setResizable(false);
		resultPane.setVisible(true);
	} // END - public void MinesweeperResult()
		
	public void mineInstall(){  // 지뢰 생성(10개)
		int cnt=0;
		
		for(int i=0; i<SIZE ; i++) {
			for(int j=0; j<SIZE ; j++)  mineField[i][j]=whatIsMine[1];
		}
		
		while(cnt<10) {
			int i = (int)(Math.random()*SIZE);
			int j = (int)(Math.random()*SIZE);
			if(mineField[i][j]==whatIsMine[1]) {mineField[i][j]=whatIsMine[0]; cnt++;}
			else cnt+=0;
		}
	} // END - public void mineInstall()
	
	public void timeClock(){  // 스톱워치 - Thread
		// https://eating-coding.tistory.com/35
		timer = new Thread(){			
			public void run(){
				String tRecord;
				
				while(gameStart==false) {
					time.setText("000");
					if(gameStart) break;
				}
				
				// https://stackify.com/heres-how-to-calculate-elapsed-time-in-java/ 
				startTime=System.currentTimeMillis();
				
				for(int sec=0;  ; sec++) {
					try {
						if(sec!=0)sleep(1000);
						tRecord = String.format("%03d", sec);
						time.setText(tRecord);
					}
					catch(Exception e){ System.out.println(e.getMessage()); }
					if(lose || win) {break;}
				}
				
				endTime=System.currentTimeMillis();
				
				float f_timeRc = Float.valueOf(endTime-startTime);      
				timeRc = String.format("Time : %.4fsec", f_timeRc/1000);
				
				if(win==true) result="CONGRATULATIONS!!!";
				if(lose==true) result="AGAIN...?";
				
				MinesweeperResult();  // 게임 결과
			}
		};
		timer.start();
	} // END - public void timeClock(){}
	
	// ----------------------- [ Event Listener ] ----------------------- // begin
	// Methods : mousePressed(), actionPerformed(), stepOnTheMine(), stepOnTheLand(),
	//           colorOfNumber(), afterExpansion(), flagCounter(), survived()
	class MyActionListener extends MouseAdapter implements ActionListener{
		
		public void mousePressed(MouseEvent e) {
			for(int i=0 ; i<SIZE ; i++) {
				for(int j=0 ; j<SIZE ; j++) {	
					// https://imhotk.tistory.com/378
					if(e.getButton()==MouseEvent.BUTTON3 && e.getSource()==mineOrNot[i][j]) {
						gameStart=true;  // 마우스 우클릭도 게임 시작으로 처리
						
						// 이미 눌린 칸(Color.GRAY)에는 ▲ 입력 못하게 작성 
						if(mineOrNot[i][j].getText() != "▲" && mineOrNot[i][j].getBackground() != Color.GRAY){
							mineOrNot[i][j].setFont(new Font("MS Gothic", Font.BOLD, 20));
							mineOrNot[i][j].setText("▲");
							flagCounter();
						}
						else {  //  이미 ▲가 있는 칸에 우클릭하면 ▲가 없어짐
							if(mineOrNot[i][j].getText() == "▲") {								
								mineOrNot[i][j].setFont(new Font("MS Gothic", Font.BOLD, 20));
								mineOrNot[i][j].setText(" ");
								flagCounter();
							}
						}
						
					}
				}
			}
		} // END - public void mousePressed(MouseEvent e)
		
		public void actionPerformed(ActionEvent e) {
			if((JButton)e.getSource()==restart) {
				dispose();
				new Minesweeper("지뢰찾기");
			}
			
			else if((JButton)e.getSource()==retry) {
				dispose();             // 게임 윈도우를 종료
				resultPane.dispose();  // 게임 결과 윈도우를 종료
				new Minesweeper("지뢰찾기");
			}
			else if((JButton)e.getSource()==quit) {
				// https://m.blog.naver.com/PostView.naver?isHttpsRedirect=true&blogId=merkuree&logNo=130017585162
				System.exit(0);
			}
			
			else {
				gameStart=true;
				
				for(int i=0; i<SIZE; i++) {
					for(int j=0; j<SIZE; j++) {					
						if((JButton)e.getSource()==mineOrNot[i][j]) {
							mineOrNot[i][j].setFont(new Font("MS Gothic", Font.BOLD, 20));
							mineOrNot[i][j].setText(mineField[i][j]);
							
							if(mineField[i][j].equals("◎")) {
								mineOrNot[i][j].setBackground(Color.RED);
								stepOnTheMine();
								lose=true;
							}
							else {
								mineOrNot[i][j].setBackground(Color.GRAY);
								stepOnTheLand(i,j);
								afterExpansion();
							}
						}
					}
				}		
			}
		} // END - public void actionPerformed(ActionEvent e)
		
		public void stepOnTheMine(){  // 게임 종료(지뢰 위치 공개)
			restart.setForeground(Color.BLACK);
			
			for(int i=0; i<SIZE; i++){
				for(int j=0; j<SIZE; j++) {
					
					if(mineOrNot[i][j].getText()==" ") {
						mineOrNot[i][j].setText(mineField[i][j]);
						if(mineField[i][j].equals("◎")) {
							mineOrNot[i][j].setFont(new Font("MS Gothic", Font.BOLD, 20));
							mineOrNot[i][j].setBackground(new Color(250, 250, 150));
						}
						else {
							mineOrNot[i][j].setText(" ");
							mineOrNot[i][j].setFont(new Font("MS Gothic", Font.BOLD, 40));
						}
					}
					
					else if(mineOrNot[i][j].getText()=="▲") {
						if(mineField[i][j].equals("◎")) mineOrNot[i][j].setBackground(new Color(250, 250, 150));
					}
					
				}
			}
		} // END - public void stepOnTheMine()
		
		private void stepOnTheLand(int row, int col) {  // 지뢰 없는 칸 밟았을 때
			int mineCnt=0;
			int row_min_lmt, row_max_lmt, col_min_lmt, col_max_lmt;
			if(row==0)      row_min_lmt=0;
			else            row_min_lmt=row-1;
			if(col==0)      col_min_lmt=0;
			else            col_min_lmt=col-1;
			if(row==SIZE-1) row_max_lmt=SIZE-1;
			else            row_max_lmt=row+1;
			if(col==SIZE-1) col_max_lmt=SIZE-1;
			else            col_max_lmt=col+1;
			
			for(int i=row_min_lmt; i<=row_max_lmt; i++) {
				for(int j=col_min_lmt; j<=col_max_lmt; j++) {
					if(mineField[i][j].equals("◎")) mineCnt++;
				}
			}
			
			if(mineCnt!=0) colorOfNumber(row, col, mineCnt);
			else {
				for(int i=row_min_lmt; i<=row_max_lmt; i++) {
					for(int j=col_min_lmt; j<=col_max_lmt; j++) {
						if(!mineField[i][j].equals("◎")) mineOrNot[i][j].setBackground(Color.GRAY);
						if(mineOrNot[i][j].getText()=="▲") mineOrNot[i][j].setText(" ");
					}
				}
			}
		} // END - private void stepOnTheLand(int row, int col)	
		
		private void colorOfNumber(int row, int col, int mineCnt) {  // 지뢰 개수 표시 및 색깔 구분
			mineOrNot[row][col].setText(String.valueOf(mineCnt));
			mineOrNot[row][col].setFont(new Font("MS Gothic", Font.BOLD, 25));
			
			if(mineCnt==1)      mineOrNot[row][col].setForeground(Color.BLUE);
			else if(mineCnt==2) mineOrNot[row][col].setForeground(new Color(0, 100, 0)); // GREEN
			else if(mineCnt==3) mineOrNot[row][col].setForeground(Color.RED);
			else if(mineCnt==4) mineOrNot[row][col].setForeground(new Color(0, 0, 75));  // NAVY
			else                mineOrNot[row][col].setForeground(Color.YELLOW);
		} // END - private void colorOfNumber(int row, int col, int mineCnt)

		public void afterExpansion() {  // 버튼 클릭 후 확장
			int prv_cnt=0;
			
			while(true) {
				int prs_cnt=0;
				
				for(int i=0; i<SIZE; i++) {
					for(int j=0; j<SIZE; j++) {
						if(mineOrNot[i][j].getBackground()==Color.GRAY){
							stepOnTheLand(i, j);
							prs_cnt++;
						}
					}
				}
				if(prs_cnt!=prv_cnt) prv_cnt=prs_cnt;
				else break;
			}
			
			flagCounter();
			survived();
		} // END - public void afterExpansion()
		
		public void flagCounter() {  // flag(▲, 우클릭된 칸) 갯수 카운트
			int Cnt =10;
			String flagCnt;
			
			for(int i=0; i<SIZE; i++) {
				for(int j=0; j<SIZE; j++) {
					if(mineOrNot[i][j].getText()=="▲") {
						Cnt--;
						if(Cnt<0) Cnt=0;
					}
				}
			}
			
			flagCnt = String.format("%03d", Cnt);
			findEmAll.setText(String.valueOf(flagCnt));
		} // END - public void flagCounter()
		
		public void survived() {  // 게임 종료(승리) 탐지		
			int noMinesUnderMyFoot=0;
			
			for(int i=0; i<SIZE; i++) {
				for(int j=0; j<SIZE; j++) {
					if(mineField[i][j]==whatIsMine[1] && mineOrNot[i][j].getBackground()==Color.GRAY) {
						noMinesUnderMyFoot++;
					}
				}
			}
			
			if(noMinesUnderMyFoot == SIZE*SIZE-10) win=true;
		} // END - public void survived()
		
	} // END - class MyActionListener extends MouseAdapter implements ActionListener{}
	// ----------------------- [ Event Listener ] ----------------------- // end
	
} // END - public class MineSweeper extends JFrame{}