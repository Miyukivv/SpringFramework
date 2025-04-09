package org.example.repositories.dblImpl;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.example.db.JdbcConnectionManager;
import org.example.models.Vehicle;
import org.example.repositories.VehicleRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class VehicleJdbcRepository implements VehicleRepository {
    private final Gson gson = new Gson();
    private Long id;
    @Override
    public List<Vehicle> findAll() {
        List<Vehicle> list = new ArrayList<>();
        String sql = "SELECT * FROM vehicle";
        try (Connection connection = JdbcConnectionManager.getInstance().getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                String attrJson = rs.getString("attributes");
                Map<String, Object> attributes = gson.fromJson(attrJson,
                        new TypeToken<Map<String, Object>>(){}.getType());
                Vehicle vehicle = Vehicle.builder()
                        .id(rs.getString("id"))
                        .category(rs.getString("category"))
                        .brand(rs.getString("brand"))
                        .model(rs.getString("model"))
                        .year(rs.getInt("year"))
                        .plate(rs.getString("plate"))
                        .price(rs.getDouble("price"))
                        .attributes(attributes != null ? attributes : new HashMap<>())
                        .build();
                list.add(vehicle);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error occurred while reading vehicles", e);
        }
        return list;
    }

    @Override
        public Optional<Vehicle> findById(String id) {
            String sql = "SELECT * FROM vehicle WHERE id = ?";
            try (Connection connection = JdbcConnectionManager.getInstance().getConnection();
                 PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.setString(1, id);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        String attrJson = rs.getString("attributes");
                        Map<String, Object> attributes = gson.fromJson(attrJson,
                                new TypeToken<Map<String, Object>>(){}.getType());
                        Vehicle vehicle = Vehicle.builder()
                                .id(rs.getString("id"))
                                .category(rs.getString("category"))
                                .brand(rs.getString("brand"))
                                .model(rs.getString("model"))
                                .year(rs.getInt("year"))
                                .plate(rs.getString("plate"))
                                .price(rs.getDouble("price"))
                                .attributes(attributes != null ? attributes : new HashMap<>())
                                .build();
                        return Optional.of(vehicle);
                    }
                }
            } catch (SQLException e) {
                throw new RuntimeException("Error occurred while reading vehicle", e);
            }
            return Optional.empty();
        }

    @Override
    public Vehicle save(Vehicle vehicle) {
        if (vehicle.getId() == null || vehicle.getId().isBlank()) {
            vehicle.setId(UUID.randomUUID().toString());
        }
        String checkSql = "SELECT COUNT(*) FROM vehicle WHERE id = ?";

        String insertSql = "INSERT INTO vehicle (id, category, brand, model, year, plate, price, attributes) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?::jsonb)";
        String updateSql = "UPDATE vehicle SET id = ?, category = ?, brand = ?, model = ?, year = ?, plate = ?, price = ?, attributes = ?::json WHERE id = ?";

        try (Connection connection = JdbcConnectionManager.getInstance().getConnection()) {
            try (PreparedStatement checkStmt = connection.prepareStatement(checkSql)) {
                checkStmt.setString(1, vehicle.getId());

                ResultSet rs = checkStmt.executeQuery();

                if (rs.next()) {
                    int count = rs.getInt(1);

                    if (count > 0) {
                        try (PreparedStatement updateStmt = connection.prepareStatement(updateSql)) {
                            updateStmt.setString(1, vehicle.getId());
                            updateStmt.setString(2, vehicle.getCategory());
                            updateStmt.setString(3, vehicle.getBrand());
                            updateStmt.setString(4, vehicle.getModel());
                            updateStmt.setInt(5, vehicle.getYear());
                            updateStmt.setString(6, vehicle.getPlate());
                            updateStmt.setDouble(7, vehicle.getPrice());
                            updateStmt.setString(8, gson.toJson(vehicle.getAttributes()));
                            updateStmt.setString(9, vehicle.getId());
                            updateStmt.executeUpdate();
                        }
                    } else {
                        try (PreparedStatement insertStmt = connection.prepareStatement(insertSql)) {
                            insertStmt.setString(1, vehicle.getId());
                            insertStmt.setString(2, vehicle.getCategory());
                            insertStmt.setString(3, vehicle.getBrand());
                            insertStmt.setString(4, vehicle.getModel());
                            insertStmt.setInt(5, vehicle.getYear());
                            insertStmt.setString(6, vehicle.getPlate());
                            insertStmt.setDouble(7, vehicle.getPrice());
                            insertStmt.setString(8, gson.toJson(vehicle.getAttributes()));
                            insertStmt.executeUpdate();
                        }
                    }
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return vehicle;
    }

    @Override
    public void deleteById(String id) {
        String sql = "DELETE FROM vehicle WHERE id = ?";
        try (Connection connection = JdbcConnectionManager.getInstance().getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error occurred while deleting vehicle", e);
        }
    }
}
