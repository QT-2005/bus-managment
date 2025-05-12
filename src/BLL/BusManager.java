package BLL;

import DAL.BusDAO;
import DTO.Bus;

import java.util.List;

public class BusManager {
    private BusDAO busDAO;

    public BusManager() {
        busDAO = new BusDAO();
    }

    public boolean addBus(Bus bus) {
        // Validate bus data
        if (bus.getBusNumber() == null || bus.getBusNumber().trim().isEmpty()) {
            return false;
        }
        if (bus.getCapacity() <= 0) {
            return false;
        }

        return busDAO.addBus(bus);
    }

    public List<Bus> getAllBuses() {
        return busDAO.getAllBuses();
    }

    public Bus getBusById(int id) {
        return busDAO.getBusById(id);
    }

    public boolean updateBus(Bus bus) {
        // Validate bus data
        if (bus.getBusNumber() == null || bus.getBusNumber().trim().isEmpty()) {
            return false;
        }
        if (bus.getCapacity() <= 0) {
            return false;
        }

        return busDAO.updateBus(bus);
    }

    public boolean deleteBus(int id) {
        return busDAO.deleteBus(id);
    }
}