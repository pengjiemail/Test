package te;

public class BuilderTest {
  
  private String name;
  
  private String idCard;
  
  private int age;
  
  private int sex;
  
  private int weight;
  
  private BuilderTest(Builder builder) {
    this.name = builder.name;
    this.idCard = builder.idCard;
    this.age = builder.age;
    this.sex = builder.sex;
    this.weight = builder.weight;
  }
  
  /**
   * @return name
   */
  public String getName() {
    return name;
  }
  
  /**
   * @return idCard
   */
  public String getIdCard() {
    return idCard;
  }
  
  /**
   * @return age
   */
  public int getAge() {
    return age;
  }
  
  /**
   * @return sex
   */
  public int getSex() {
    return sex;
  }
  
  /**
   * @return weight
   */
  public int getWeight() {
    return weight;
  }
  
  public static class Builder {
    
    private String name;
    
    private String idCard;
    
    private int age;
    
    private int sex;
    
    private int weight;
    
    public Builder(String name, String idCard) {
      this.name = name;
      this.idCard = idCard;
    }
    
    public Builder age(int age) {
      this.age = age;
      return this;
    }
    
    public Builder sex(int sex) {
      this.sex = sex;
      return this;
    }
    
    public Builder weight(int weight) {
      this.weight = weight;
      return this;
    }
    
    public BuilderTest build() {
      return new BuilderTest(this);
    }
  }
}
