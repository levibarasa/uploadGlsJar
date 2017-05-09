
import org.upl.main.Branches;
import org.upl.main.Customer;
import org.upl.main.DemandGeneration;
import org.upl.main.Loans;
import org.upl.main.Roles;
import org.upl.main.Users;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Levi
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
    Loans.uploadFiles();
    Loans.uploadFiles();
    DemandGeneration.uploadFiles();
    Customer.uploadFiles();
    Roles.uploadFiles();
     Branches.uploadFiles();
    Users.uploadFiles();
        
    }
    
}
