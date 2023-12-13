package mafia;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;

public class StartPanel extends JPanel {
  private JTextField userName;

  public StartPanel(JFrame frame) {
    setBounds(100, 100, 1109, 969);
    this.setLayout(null);
    this.setBackground(Color.BLACK); //배경색 검정

    JLabel nameLabel = new JLabel("이름을 입력해 주세요");
    nameLabel.setFont(new Font("굴림", Font.PLAIN, 30));
    nameLabel.setBounds(388, 405, 306, 59);
    nameLabel.setForeground(Color.RED);
    this.add(nameLabel);

    userName = new JTextField();
    userName.setBounds(368, 474, 353, 39);
    this.add(userName);
    userName.setColumns(10);

    //게임 시작
    final ImageIcon startGameIcon = new ImageIcon("src/mafia/image/gameStart.png");
    Image startImage = startGameIcon.getImage();
    Image newStartImage = startImage.getScaledInstance(188, 39, Image.SCALE_SMOOTH);
    startGameIcon.setImage(newStartImage);

    JLabel startGameLabel = new JLabel(startGameIcon);
    startGameLabel.setBounds(458, 558, 188, 39);
    this.add(startGameLabel);

    //나가기
    final ImageIcon outIcon = new ImageIcon("src/mafia/image/out.png");
    Image outImage = outIcon.getImage();
    Image newOutImage = outImage.getScaledInstance(188, 39, Image.SCALE_SMOOTH);
    outIcon.setImage(newOutImage);

    JLabel outLabel = new JLabel(outIcon);
    outLabel.setBounds(458, 652, 188, 39);
    this.add(outLabel);

    //게임 시작 클릭 이벤트
    startGameLabel.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {
        String name = userName.getText();
        if (name == null || name.trim().isEmpty()) {
          JOptionPane.showMessageDialog(frame, "이름을 입력하세요.", "오류", JOptionPane.ERROR_MESSAGE);
        } else {
          try {
            new GameChat(frame, name);
          } catch (IOException ex) {
            throw new RuntimeException(ex);
          }
        }
      }
    });

    //나가기 클릭 이벤트
    outLabel.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {
        frame.dispose();
      }
    });

    //게임 시작 마우스 이벤트
    startGameLabel.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseEntered(MouseEvent e) {
        ImageIcon brokenStartIcon = new ImageIcon("src/mafia/image/gameStart_broken.png");
        Image brokenStartImage = brokenStartIcon.getImage();
        Image newBrokenStartImage = brokenStartImage.getScaledInstance(startGameIcon.getIconWidth(), startGameIcon.getIconHeight(), Image.SCALE_SMOOTH);
        brokenStartIcon.setImage(newBrokenStartImage);
        startGameLabel.setIcon(brokenStartIcon);
      }

      @Override
      public void mouseExited(MouseEvent e) {
        startGameLabel.setIcon(startGameIcon);
      }
    });

    //나가기 마우스 이벤트
    outLabel.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseEntered(MouseEvent e) {
        ImageIcon brokenOutIcon = new ImageIcon("src/mafia/image/out_broken.png");
        Image brokenOutImage = brokenOutIcon.getImage();
        Image newBrokenOutImage = brokenOutImage.getScaledInstance(outIcon.getIconWidth(), outIcon.getIconHeight(), Image.SCALE_SMOOTH);
        brokenOutIcon.setImage(newBrokenOutImage);
        outLabel.setIcon(brokenOutIcon);
      }

      @Override
      public void mouseExited(MouseEvent e) {
        outLabel.setIcon(outIcon);
      }
    });

    //MAFIA 로고 사진
    ImageIcon logoIcon = new ImageIcon("src/mafia/image/logo.png");
    Image logoImage = logoIcon.getImage();
    Image newLogoImage = logoImage.getScaledInstance(500, 300, Image.SCALE_SMOOTH);
    logoIcon = new ImageIcon(newLogoImage);

    JLabel logoLabel = new JLabel(logoIcon);
    logoLabel.setBounds(304, 50, 500, 300);
    this.add(logoLabel);

    //총알 사진
    ImageIcon gunIcon = new ImageIcon("src/mafia/image/gun.png");
    Image gunImage = gunIcon.getImage();
    Image newGunImage = gunImage.getScaledInstance(200, 150, Image.SCALE_SMOOTH);
    gunIcon = new ImageIcon(newGunImage);

    JLabel gunLabel = new JLabel(gunIcon);
    gunLabel.setBounds(50, 350, 200, 150);
    this.add(gunLabel);

    //깨진 유리 사진
    ImageIcon brokenIcon = new ImageIcon("src/mafia/image/broken.png");
    Image brokenImage = brokenIcon.getImage();
    Image newBrokenImage = brokenImage.getScaledInstance(200, 200, Image.SCALE_SMOOTH);
    brokenIcon = new ImageIcon(newBrokenImage);

    JLabel brokenLabel = new JLabel(brokenIcon);
    brokenLabel.setBounds(800, 400, 230, 200);
    this.add(brokenLabel);
  }
}

