package mainline;

public class Driver {
	public static void main(String[] argv) {
        try {
        	WindowManager.getInstance().setVisible(true);
        } catch (Exception exception) {
        	System.out.println(exception.getStackTrace());
        }
    }
}