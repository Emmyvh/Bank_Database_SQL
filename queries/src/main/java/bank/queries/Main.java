package bank.queries;

public class Main {
    public static void main(String[] args) {
        Account account = new Account();
        Client client = new Client();
        Wallet wallet = new Wallet();

        client.selectClient(1);
    }
}
