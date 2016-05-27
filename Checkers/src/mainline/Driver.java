package mainline;

public class Driver {
	public static void main(String[] argv) {
        try {
        	WindowInstance.getInstance().setVisible(true);
        } catch (Exception exception) {
        	System.out.println(exception.getStackTrace());
        }
    }
}