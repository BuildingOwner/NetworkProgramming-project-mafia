/*
 * GamePanel 클래스
 * 이 클래스는 마피아 게임의 메인 화면을 나타내는 패널입니다.
 */
package mafia;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GamePanel extends JPanel {
  public JTextField chat; //채팅 입력하는 창
  public JScrollPane scrollPane; //스크롤바 생성
  public JTextArea chatArea; //채팅창
  public JButton startBtn; //시작 버튼
  public JTextField vote; //투표 입력하는 창
  public JLabel member; //참여 인원
  public JLabel time; //남은 시간
  public JLabel ment; //진행 상황 멘트
  public JLabel roleImageLabel; //우측 하단에 역할 사진

  public GamePanel(JFrame frame, ClientGame clientGame) { //생성자 함수
    this.setLayout(null);
    this.setBackground(new Color(24, 24, 40)); //배경색

    //현재 진행 상황을 멘트로 알려주는 코드
    ment = new JLabel("<html>** 현재 진행 상황 **</html>");
    ment.setForeground(Color.WHITE);
    ment.setFont(new Font("굴림", Font.PLAIN, 15));
    ment.setBounds(800, 500, 500, 200);

    //오른쪽 하단에 진행 상황을 알려주는 이미지
    roleImageLabel = new JLabel();
    roleImageLabel.setBounds(800, 650, 600, 200);

    //채팅
    JLabel chatable = new JLabel("채팅");
    chatable.setForeground(Color.WHITE);
    chatable.setFont(new Font("굴림", Font.PLAIN, 25));
    chatable.setBounds(20, 530, 73, 53);

    //채팅 입력
    chat = new JTextField(30);
    chat.setBounds(90, 542, 694, 30);
    chat.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) { //사용자가 채팅 입력 후 엔터 키를 눌렀을 때 동작하는 리스너
        String s = chat.getText(); //사용자가 입력한 채팅 메시지를 가져옴

        if (clientGame.isDead) { //플레이어가 사망했다면
          clientGame.sendMessage(clientGame.name + "/chat/" + s + "/death"); //채팅 메시지와 플레이어의 사망 상태를 서버로 전송
          chatArea.append(clientGame.name + " (사망자) : " + s + "\n"); //채팅 영역에 사망자로 표시 & 채팅 내용 추가
        } else {
          clientGame.sendMessage(clientGame.name + "/chat/" + s + "/alive"); //채팅 메시지와 플레이어의 생존 상태를 서버로 전송
          chatArea.append(clientGame.name + " (생존자) : " + s + "\n"); //채팅 영역에 생존자로 표시 & 채팅 내용 추가
        }
        chat.setText(""); //채팅 입력창을 초기화
        chatArea.setCaretPosition(chatArea.getDocument().getLength()); //채팅 영역을 맨 아래로 스크롤
      }
    });

    //채팅창
    chatArea = new JTextArea(10, 30);
    chatArea.setBackground(new Color(224, 224, 224));
    scrollPane = new JScrollPane(chatArea);
    scrollPane.setBounds(20, 39, 762, 493);
    this.add(scrollPane); //스크롤바 생성
    chatArea.setEditable(false);

    //투표
    JLabel votelable = new JLabel("투표");
    votelable.setFont(new Font("굴림", Font.PLAIN, 25));
    votelable.setForeground(Color.WHITE);
    votelable.setBounds(20, 593, 73, 53);

    //투표 입력
    vote = new JTextField();
    vote.setBounds(90, 608, 694, 30);
    vote.setColumns(10);

    vote.addActionListener(new ActionListener() { //사용자가 투표 입력 후 엔터 키를 눌렀을 때 동작하는 리스너
      @Override
      public void actionPerformed(ActionEvent e) {
        String s = vote.getText(); //사용자가 입력한 투표 정보를 가져옴
        String[] strs = s.split("#"); //이름#숫자 이런 형태이므로 이름과 숫자를 추출
        boolean nameCheck = false;

        List<String> registeredNames = new ArrayList<>(); //등록된 플레이어 이름을 확인하기 위한 리스트
        for(String name : clientGame.userNames){ //등록된 플레이어들의 이름을 리스트에 추가하고, 동시에 입력된 이름이 올바른지 체크하는 코드
          registeredNames.add(name.split("#")[0]);
          if(strs.length>1){
            if(s.equals(name)){
              nameCheck = true;
            }
          }else {
            if(strs[0].equals(name.split("#")[0])){
              nameCheck = true;
            }
          }
        }

        //이름이 중복된 경우 예외 처리
        int count = 0;
        for(String rn : registeredNames){
          if(strs.length<2){
            if(strs[0].equals(rn)){
              count++;
            }
            if(count>1){
              nameCheck = false;
              break;
            }
          }
        }

        //올바른 이름이 입력되지 않았을 경우 오류창 띄우게 예외 처리
        if(!nameCheck){
          JOptionPane.showMessageDialog(frame, "정확한 이름을 입력하세요.\n ex)(이름) or (이름)#(숫자)", "오류", JOptionPane.ERROR_MESSAGE);
          return;
        }

        if (clientGame.now == DayNight.NIGHT) { //현재 상태가 밤이라면
          clientGame.sendMessage(clientGame.name + "/kill/" + s); //죽음 투표
        } else if (clientGame.now == DayNight.HEAL) {
          clientGame.sendMessage(clientGame.name + "/heal/" + s); //치료 투표
        } else if (clientGame.now == DayNight.POLICE) {
          clientGame.sendMessage(clientGame.name + "/police/" + s); //조사 투표
        } else {
          clientGame.sendMessage(clientGame.name + "/vote/" + s); //낮인 경우 투표를 서버로 전송
        }
        vote.setText("");
        chatArea.setCaretPosition(chatArea.getDocument().getLength());
      }
    });

    JLabel[] roleLabels = new JLabel[10]; //10개 배열 생성

    //배열에 사진명 넣으면 해당 사진이 화면 출력됨
    String[] roles = {"citizen","citizen","police"};
    for (int i = 0; i < roles.length; i++) {
      roleLabels[i + 2] = createRoleLabel("src/mafia/image/" + roles[i] + ".png", 790 + i * 150, 39);
      this.add(roleLabels[i + 2]);
    }

    String[] roles2 = {"mafia","mafia","doctor"};
    for (int i = 0; i < roles2.length; i++) {
      roleLabels[i + 2] = createRoleLabel("src/mafia/image/" + roles2[i] + ".png", 790 + i* 150, 300);
      this.add(roleLabels[i + 2]);
    }

    //참여 인원 정보
    member = new JLabel("참여인원");
    member.setForeground(Color.WHITE);
    member.setFont(new Font("굴림", Font.PLAIN, 20));
    member.setBounds(790, 230, 763, 85);

    //시작 버튼
    startBtn = new JButton("start");
    startBtn.setForeground(Color.WHITE);
    startBtn.setFont(new Font("굴림", Font.PLAIN, 15));
    Color burgundyColor = new Color(119, 27, 35);
    startBtn.setBackground(burgundyColor);
    startBtn.setFocusPainted(false);
    startBtn.setBounds(20, 680, 80, 45);

    startBtn.addActionListener(new ActionListener() { //시작 버튼 클릭 시 이벤트 리스너
      @Override
      public void actionPerformed(ActionEvent e) {
        clientGame.sendMessage(clientGame.name + "/gameStart");
      }
    });

    String[] processingTag = clientGame.name.split("#"); //플레이어 이름에서 #을 기준으로 분리하여 배열에 저장
    String tag = processingTag[processingTag.length - 1]; //이름#1 마지막 요소를 태그로 사용
    if (!tag.equals("1")) { //1이 아니면 -> 즉, 첫 번째 플레이어가 아니면
      startBtn.setEnabled(false); //시작 버튼 비활성화
    }

    //타이머
    time = new JLabel("30");
    time.setBounds(150, 680, 80, 45);
    time.setFont(new Font("굴림", Font.PLAIN, 30));
    time.setForeground(Color.WHITE);
    time.setHorizontalAlignment(SwingConstants.CENTER);
    time.setVerticalAlignment(SwingConstants.CENTER);

    this.add(startBtn);
    this.add(chat);
    this.add(scrollPane);
    this.add(votelable);
    this.add(chatable);
    this.add(member);
    this.add(vote);
    this.add(time);
    this.add(ment);
    this.add(roleImageLabel);

    setVisible(true);
  }

  private String extractName(String fullName) { //이름에서 #을 추출하는 함수
    String[] parts = fullName.split("#");
    return parts[0];
  }

  //오른쪽 상단에 역할 사진 보여주는 함수
  private JLabel createRoleLabel(String imagePath, int x, int y) {
    ImageIcon icon = new ImageIcon(imagePath);
    Image image = icon.getImage();
    Image newImage = image.getScaledInstance(150, 200, Image.SCALE_SMOOTH);
    icon = new ImageIcon(newImage);

    JLabel label = new JLabel(icon);
    label.setBounds(x, y, 150, 200);

    return label;
  }

  //오른쪽 하단에 진행 상황 사진 보여주는 함수
  private ImageIcon loadImageIcon(File file, int width, int height) {
    try {
      Image image = new ImageIcon(file.toURI().toURL()).getImage();
      Image scaledImage = image.getScaledInstance(width, height, Image.SCALE_SMOOTH);
      return new ImageIcon(scaledImage);
    } catch (IOException e) {
      e.printStackTrace();
      return new ImageIcon();
    }
  }
}