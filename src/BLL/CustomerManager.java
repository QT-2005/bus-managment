package BLL;

import DAL.CustomerDAO;
import DTO.Customer;

import java.util.List;

public class CustomerManager {
    private CustomerDAO customerDAO;

    public CustomerManager() {
        customerDAO = new CustomerDAO();
    }

    public boolean addCustomer(Customer customer) {
        // Validate customer data
        if (customer.getName() == null || customer.getName().trim().isEmpty()) {
            return false;
        }

        return customerDAO.addCustomer(customer);
    }

    public List<Customer> getAllCustomers() {
        return customerDAO.getAllCustomers();
    }

    public Customer getCustomerById(int id) {
        return customerDAO.getCustomerById(id);
    }

    public boolean updateCustomer(Customer customer) {
        // Validate customer data
        if (customer.getName() == null || customer.getName().trim().isEmpty()) {
            return false;
        }

        return customerDAO.updateCustomer(customer);
    }

    public boolean deleteCustomer(int id) {
        return customerDAO.deleteCustomer(id);
    }

    public List<Customer> searchCustomers(String keyword) {
        return customerDAO.searchCustomers(keyword);
    }
}