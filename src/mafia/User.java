/*
 * User 클래스
 * 이 클래스는 마피아 게임에서 각 플레이어 정보를 저장하는 클래스입니다.
 */
package mafia;

public class User {
  public String name; //이름
  public String jab; //역할
  public Boolean isDead = false; //생존 여부
  public Boolean enemy = false; //적 판단 여부

  public User(String name, String job) { //생성자 함수
    this.name = name;
    this.jab = job;
  }
}
