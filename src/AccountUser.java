import java.io.*;

public class AccountUser {
    private static final String File_UserAccount="useraccount.txt";
    public static boolean checkUserPW(String user,String password){
        BufferedReader buffer = null;
        String line;
        try {
            buffer = new BufferedReader(new FileReader("useraccount.txt"));
            String[] sepLine=new String[3];
            while ((line = buffer.readLine()) != null) {
               sepLine=line.split("@");
               if (sepLine[1].equals(user)&&sepLine[2].equals(password)) return true;
            }
            buffer.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return false;
    }
    public static boolean checkExistUser(String user){
        BufferedReader buffer = null;
        String line;
        try {
            buffer = new BufferedReader(new FileReader("useraccount.txt"));
            String[] sepLine=new String[3];
            while ((line = buffer.readLine()) != null) {
                sepLine=line.split("@");
                if (sepLine[1].equals(user)) return false;
            }
            buffer.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return true;
    }
    public static void main(String[] args){
        System.out.println(checkExistUser("hphuc551"));
    }
}
