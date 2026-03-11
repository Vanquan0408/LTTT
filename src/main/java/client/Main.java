package client;

public class Main {

    public static void main(String[] args){

        Client.connect();

        LoginForm login = new LoginForm();
        login.setVisible(true);

    }

}