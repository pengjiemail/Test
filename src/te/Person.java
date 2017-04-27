package te;

import java.io.Serializable;
import java.util.Date;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

@XmlRootElement(name="person")
@XmlAccessorType(XmlAccessType.NONE)
public class Person implements Serializable,Cloneable{
  
  /** 
  * 参数说明
  */
  private static final long serialVersionUID = 1L;

  @XmlElement(name="name")
  private String name;
  
  @XmlElement(name="id")
  private String id;
  
  @XmlElement(name="age")
  private int age;
  
  @XmlElement(name="date")
  @XmlJavaTypeAdapter(XmlJavaDateType.class)
  private Date date;
  
  /**
   * @return name
   */
  public String getName() {
    return name;
  }
  
  /**
   * @param name
   *        set name
   */
  public void setName(String name) {
    this.name = name;
  }
  
  /**
   * @return age
   */
  public int getAge() {
    return age;
  }
  
  
  /**
   * @return id
   */
  public String getId() {
    return id;
  }

  
  /**
   * @param id set id
   */
  public void setId(String id) {
    this.id = id;
  }

  
  /**
   * @return date
   */
  public Date getDate() {
    return date;
  }

  
  /**
   * @param date set date
   */
  public void setDate(Date date) {
    this.date = date;
  }

  /**
   * @param age
   *        set age
   */
  public void setAge(int age) {
    this.age = age;
  }
  
  public void fun() {
    System.out.println("sssss");
  }
  
  private void test(){
    System.out.println("test");
  }

  @Override
  protected Person clone() throws CloneNotSupportedException {
    Person p  = null;
    return p = (Person) super.clone();
  }
  
}
