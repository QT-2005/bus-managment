package BLL;

import DAL.TicketDAO;
import DTO.Ticket;

import java.util.Date;
import java.util.List;

public class TicketManager {
    private TicketDAO ticketDAO;

    public TicketManager() {
        ticketDAO = new TicketDAO();
    }

    public boolean addTicket(Ticket ticket) {
        // Validate ticket data
        if (ticket.getSeatNumber() == null || ticket.getSeatNumber().trim().isEmpty()) {
            return false;
        }
        if (ticket.getPrice() <= 0) {
            return false;
        }
        if (ticket.getBookingDate() == null) {
            ticket.setBookingDate(new Date()); // Set current date as booking date
        }
        if (ticket.getTravelDate() == null) {
            return false;
        }
        if (ticket.getStatus() == null || ticket.getStatus().trim().isEmpty()) {
            ticket.setStatus("BOOKED"); // Set default status
        }

        return ticketDAO.addTicket(ticket);
    }

    public List<Ticket> getAllTickets() {
        return ticketDAO.getAllTickets();
    }

    public Ticket getTicketById(int id) {
        return ticketDAO.getTicketById(id);
    }

    public boolean updateTicket(Ticket ticket) {
        // Validate ticket data
        if (ticket.getSeatNumber() == null || ticket.getSeatNumber().trim().isEmpty()) {
            return false;
        }
        if (ticket.getPrice() <= 0) {
            return false;
        }
        if (ticket.getBookingDate() == null) {
            return false;
        }
        if (ticket.getTravelDate() == null) {
            return false;
        }
        if (ticket.getStatus() == null || ticket.getStatus().trim().isEmpty()) {
            return false;
        }

        return ticketDAO.updateTicket(ticket);
    }

    public boolean deleteTicket(int id) {
        return ticketDAO.deleteTicket(id);
    }

    public boolean cancelTicket(int id) {
        Ticket ticket = ticketDAO.getTicketById(id);
        if (ticket != null) {
            ticket.setStatus("CANCELLED");
            return ticketDAO.updateTicket(ticket);
        }
        return false;
    }
}