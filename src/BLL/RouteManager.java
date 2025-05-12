package BLL;

import DAL.RouteDAO;
import DTO.Route;

import java.util.List;

public class RouteManager {
    private RouteDAO routeDAO;

    public RouteManager() {
        routeDAO = new RouteDAO();
    }

    public boolean addRoute(Route route) {
        // Validate route data
        if (route.getSource() == null || route.getSource().trim().isEmpty()) {
            return false;
        }
        if (route.getDestination() == null || route.getDestination().trim().isEmpty()) {
            return false;
        }
        if (route.getDistance() <= 0) {
            return false;
        }
        if (route.getDuration() <= 0) {
            return false;
        }
        if (route.getBasePrice() <= 0) {
            return false;
        }

        return routeDAO.addRoute(route);
    }

    public List<Route> getAllRoutes() {
        return routeDAO.getAllRoutes();
    }

    public Route getRouteById(int id) {
        return routeDAO.getRouteById(id);
    }

    public boolean updateRoute(Route route) {
        // Validate route data
        if (route.getSource() == null || route.getSource().trim().isEmpty()) {
            return false;
        }
        if (route.getDestination() == null || route.getDestination().trim().isEmpty()) {
            return false;
        }
        if (route.getDistance() <= 0) {
            return false;
        }
        if (route.getDuration() <= 0) {
            return false;
        }
        if (route.getBasePrice() <= 0) {
            return false;
        }

        return routeDAO.updateRoute(route);
    }

    public boolean deleteRoute(int id) {
        return routeDAO.deleteRoute(id);
    }

    public List<Route> searchRoutes(String source, String destination) {
        return routeDAO.searchRoutes(source, destination);
    }
}