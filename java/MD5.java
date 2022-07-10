import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Util {
	//待加密的密码
	public static String password = "123456abc";
	
	public static void main(String args[]) {
		//结果字符串
		String result = "";
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(password.getBytes());
            byte b[] = md.digest();
            int i;
            StringBuffer buf = new StringBuffer("");
            for (int offset = 0; offset < b.length; offset++) {
                i = b[offset];
                if (i < 0)
                    i += 256;
                if (i < 16)
                    buf.append("0");
                buf.append(Integer.toHexString(i));
            }
            result = buf.toString();
            System.out.println("MD5(" + password + ",32小写) = " + result);
            System.out.println("MD5(" + password + ",32大写) = " + result.toUpperCase());
            System.out.println("++++++++++++++++++++++++各位大哥借过+++++++++++++++++++++++");
            System.out.println("MD5(" + password + ",16小写) = " + buf.toString().substring(8, 24));
            System.out.println("MD5(" + password + ",16大写) = " + buf.toString().substring(8, 24).toUpperCase());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
	}
}
