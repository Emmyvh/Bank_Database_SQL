package bank.queries;

import java.sql.SQLException;

public class Main {
    public static void main(String[] args) throws SQLException {
        Account account = new Account();
        Client client = new Client();
        Wallet wallet = new Wallet();
        Transactions transactions = new Transactions();
        Loan loan = new Loan();

        loan.createLoan(27, 26, 2022, 4, 27, 10, 2, 1);
    }

}
