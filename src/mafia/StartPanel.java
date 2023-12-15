package mafia;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;

public class StartPanel extends JPanel {
  private JTextField userName;
  public File imageFile = new File("src/image/logo4.png");

  public StartPanel(JFrame frame) {
    setBounds(100, 100, 1109, 969);
    this.setLayout(null);
    ImageIcon backgroundImage = loadImageIcon(imageFile, 1100, 900);

    JLabel backgroundLabel = new JLabel(backgroundImage);
    backgroundLabel.setBounds(0, 0, 1100,900);

    this.add(backgroundLabel);


    JLabel nameLabel = new JLabel("이름을 입력해 주세요");
    nameLabel.setFont(new Font("굴림", Font.PLAIN, 30));
    nameLabel.setBounds(230, 405, 306, 59);
    nameLabel.setForeground(Color.RED);
    this.add(nameLabel);

    userName = new JTextField();
    userName.setBounds(200, 474, 353, 39);
    this.add(userName);
    userName.setColumns(10);

// 게임 시작
    final ImageIcon startGameIcon = new ImageIcon("src/mafia/image/gameStart.png");
    Image startImage = startGameIcon.getImage();
    Image newStartImage = startImage.getScaledInstance(188, 39, Image.SCALE_SMOOTH);
    startGameIcon.setImage(newStartImage);

    JLabel startGameLabel = new JLabel(startGameIcon);
    startGameLabel.setBounds(270, 558, 188, 39);
    this.add(startGameLabel);
// 나가기
    final ImageIcon outIcon = new ImageIcon("src/mafia/image/out.png");
    Image outImage = outIcon.getImage();
    Image newOutImage = outImage.getScaledInstance(188, 39, Image.SCALE_SMOOTH);
    outIcon.setImage(newOutImage);

    JLabel outLabel = new JLabel(outIcon);
    outLabel.setBounds(270, 652, 188, 39);
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
            new ClientGame(frame, name);
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

    //배경 화면 사진
    ImageIcon backIcon = new ImageIcon("src/mafia/image/logo4.png");
    Image backIconImage = backIcon.getImage();
    Image newBackImage = backIconImage.getScaledInstance(1280, 900, Image.SCALE_SMOOTH);
    backIcon = new ImageIcon(newBackImage);

    JLabel backLabel = new JLabel(backIcon);
    backLabel.setBounds(0, 0, 1280, 900);
    this.add(backLabel);
  }
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

