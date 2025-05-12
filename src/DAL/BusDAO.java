package DAL;

import Config.DatabaseConnection;
import DTO.Bus;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BusDAO {
    private Connection connection;

    public BusDAO() {
        this.connection = DatabaseConnection.getConnection();
    }

    public boolean addBus(Bus bus) {
        String query = "INSERT INTO buses (bus_number, bus_type, capacity, status) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, bus.getBusNumber());
            stmt.setString(2, bus.getBusType());
            stmt.setInt(3, bus.getCapacity());
            stmt.setString(4, bus.getStatus());

            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        bus.setId(generatedKeys.getInt(1));
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

    public List<Bus> getAllBuses() {
        List<Bus> buses = new ArrayList<>();
        String query = "SELECT * FROM buses";

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                Bus bus = new Bus();
                bus.setId(rs.getInt("id"));
                bus.setBusNumber(rs.getString("bus_number"));
                bus.setBusType(rs.getString("bus_type"));
                bus.setCapacity(rs.getInt("capacity"));
                bus.setStatus(rs.getString("status"));

                buses.add(bus);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return buses;
    }

    public Bus getBusById(int id) {
        String query = "SELECT * FROM buses WHERE id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Bus bus = new Bus();
                    bus.setId(rs.getInt("id"));
                    bus.setBusNumber(rs.getString("bus_number"));
                    bus.setBusType(rs.getString("bus_type"));
                    bus.setCapacity(rs.getInt("capacity"));
                    bus.setStatus(rs.getString("status"));

                    return bus;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public boolean updateBus(Bus bus) {
        String query = "UPDATE buses SET bus_number = ?, bus_type = ?, capacity = ?, status = ? WHERE id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, bus.getBusNumber());
            stmt.setString(2, bus.getBusType());
            stmt.setInt(3, bus.getCapacity());
            stmt.setString(4, bus.getStatus());
            stmt.setInt(5, bus.getId());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteBus(int id) {
        String query = "DELETE FROM buses WHERE id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, id);

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}