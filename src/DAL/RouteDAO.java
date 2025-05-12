package DAL;

import Config.DatabaseConnection;
import DTO.Route;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RouteDAO {
    private Connection connection;

    public RouteDAO() {
        this.connection = DatabaseConnection.getConnection();
    }

    public boolean addRoute(Route route) {
        String query = "INSERT INTO routes (source, destination, distance, duration, bus_id, departure_time, base_price) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, route.getSource());
            stmt.setString(2, route.getDestination());
            stmt.setDouble(3, route.getDistance());
            stmt.setInt(4, route.getDuration());
            stmt.setInt(5, route.getBusId());
            stmt.setTime(6, route.getDepartureTime());
            stmt.setDouble(7, route.getBasePrice());

            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        route.setId(generatedKeys.getInt(1));
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

    public List<Route> getAllRoutes() {
        List<Route> routes = new ArrayList<>();
        String query = "SELECT * FROM routes";

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                Route route = new Route();
                route.setId(rs.getInt("id"));
                route.setSource(rs.getString("source"));
                route.setDestination(rs.getString("destination"));
                route.setDistance(rs.getDouble("distance"));
                route.setDuration(rs.getInt("duration"));
                route.setBusId(rs.getInt("bus_id"));
                route.setDepartureTime(rs.getTime("departure_time"));
                route.setBasePrice(rs.getDouble("base_price"));

                routes.add(route);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return routes;
    }

    public Route getRouteById(int id) {
        String query = "SELECT * FROM routes WHERE id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Route route = new Route();
                    route.setId(rs.getInt("id"));
                    route.setSource(rs.getString("source"));
                    route.setDestination(rs.getString("destination"));
                    route.setDistance(rs.getDouble("distance"));
                    route.setDuration(rs.getInt("duration"));
                    route.setBusId(rs.getInt("bus_id"));
                    route.setDepartureTime(rs.getTime("departure_time"));
                    route.setBasePrice(rs.getDouble("base_price"));

                    return route;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public boolean updateRoute(Route route) {
        String query = "UPDATE routes SET source = ?, destination = ?, distance = ?, " +
                "duration = ?, bus_id = ?, departure_time = ?, base_price = ? WHERE id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, route.getSource());
            stmt.setString(2, route.getDestination());
            stmt.setDouble(3, route.getDistance());
            stmt.setInt(4, route.getDuration());
            stmt.setInt(5, route.getBusId());
            stmt.setTime(6, route.getDepartureTime());
            stmt.setDouble(7, route.getBasePrice());
            stmt.setInt(8, route.getId());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteRoute(int id) {
        String query = "DELETE FROM routes WHERE id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, id);

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Route> searchRoutes(String source, String destination) {
        List<Route> routes = new ArrayList<>();
        String query = "SELECT * FROM routes WHERE source LIKE ? AND destination LIKE ?";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, "%" + source + "%");
            stmt.setString(2, "%" + destination + "%");

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Route route = new Route();
                    route.setId(rs.getInt("id"));
                    route.setSource(rs.getString("source"));
                    route.setDestination(rs.getString("destination"));
                    route.setDistance(rs.getDouble("distance"));
                    route.setDuration(rs.getInt("duration"));
                    route.setBusId(rs.getInt("bus_id"));
                    route.setDepartureTime(rs.getTime("departure_time"));
                    route.setBasePrice(rs.getDouble("base_price"));

                    routes.add(route);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return routes;
    }
}