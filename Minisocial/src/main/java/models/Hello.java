package models;
import javax.ejb.Stateless;

@Stateless
public class Hello {
	public String helloworld() {
		return "Hello world";
	}
}
