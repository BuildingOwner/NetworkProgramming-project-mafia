package mafia;

public class User {
  public String name;
  public String jab;
  public Boolean isDead = false;
  public Boolean enemy = false;

  public User(String name, String job){
    this.name = name;
    this.jab = job;
  }
}
