package DAL;

import Config.DatabaseConnection;
import DTO.Ticket;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TicketDAO {
    private Connection connection;

    public TicketDAO() {
        this.connection = DatabaseConnection.getConnection();
    }

    public boolean addTicket(Ticket ticket) {
        String query = "INSERT INTO tickets (route_id, customer_id, seat_number, price, booking_date, travel_date, status) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, ticket.getRouteId());
            stmt.setInt(2, ticket.getCustomerId());
            stmt.setString(3, ticket.getSeatNumber());
            stmt.setDouble(4, ticket.getPrice());
            stmt.setDate(5, new Date(ticket.getBookingDate().getTime()));
            stmt.setDate(6, new Date(ticket.getTravelDate().getTime()));
            stmt.setString(7, ticket.getStatus());

            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        ticket.setId(generatedKeys.getInt(1));
                    }
                }
                return true;
            }
            return false;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Ticket> getAllTickets() {
        List<Ticket> tickets = new ArrayList<>();
        String query = "SELECT * FROM tickets";

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                Ticket ticket = new Ticket();
                ticket.setId(rs.getInt("id"));
                ticket.setRouteId(rs.getInt("route_id"));
                ticket.setCustomerId(rs.getInt("customer_id"));
                ticket.setSeatNumber(rs.getString("seat_number"));
                ticket.setPrice(rs.getDouble("price"));
                ticket.setBookingDate(rs.getDate("booking_date"));
                ticket.setTravelDate(rs.getDate("travel_date"));
                ticket.setStatus(rs.getString("status"));

                tickets.add(ticket);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return tickets;
    }

    public Ticket getTicketById(int id) {
        String query = "SELECT * FROM tickets WHERE id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Ticket ticket = new Ticket();
                    ticket.setId(rs.getInt("id"));
                    ticket.setRouteId(rs.getInt("route_id"));
                    ticket.setCustomerId(rs.getInt("customer_id"));
                    ticket.setSeatNumber(rs.getString("seat_number"));
                    ticket.setPrice(rs.getDouble("price"));
                    ticket.setBookingDate(rs.getDate("booking_date"));
                    ticket.setTravelDate(rs.getDate("travel_date"));
                    ticket.setStatus(rs.getString("status"));

                    return ticket;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public boolean updateTicket(Ticket ticket) {
        String query = "UPDATE tickets SET route_id = ?, customer_id = ?, seat_number = ?, " +
                "price = ?, booking_date = ?, travel_date = ?, status = ? WHERE id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, ticket.getRouteId());
            stmt.setInt(2, ticket.getCustomerId());
            stmt.setString(3, ticket.getSeatNumber());
            stmt.setDouble(4, ticket.getPrice());
            stmt.setDate(5, new Date(ticket.getBookingDate().getTime()));
            stmt.setDate(6, new Date(ticket.getTravelDate().getTime()));
            stmt.setString(7, ticket.getStatus());
            stmt.setInt(8, ticket.getId());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteTicket(int id) {
        String query = "DELETE FROM tickets WHERE id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, id);

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}