package org.example.repositories.dblImpl;

import org.example.db.JdbcConnectionManager;
import org.example.models.Rental;
import org.example.repositories.RentalRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class RentalJdbcRepository implements RentalRepository {
    @Override
    public List<Rental> findAll() {
        List<Rental> list = new ArrayList<>();

        String sql = "SELECT * FROM rental";
        Connection connection = JdbcConnectionManager.getInstance().getConnection();
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    Rental rental = Rental.builder()
                            .id(rs.getString("id"))
                            .vehicleId(rs.getString("vehicle_id"))
                            .userId(rs.getString("user_id"))
                            .rentDateTime(rs.getString("rent_date"))
                            .returnDateTime(rs.getString("return_date"))
                            .build();
                    list.add(rental);
                }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return list;
    }

    @Override
    public Optional<Rental> findById(String id) {
        String sql = "SELECT * FROM rental WHERE id = ?";
        Connection connection = JdbcConnectionManager.getInstance().getConnection();
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1,id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()){
                Rental rental = Rental.builder()
                        .id(rs.getString("id"))
                        .vehicleId(rs.getString("vehicle_id"))
                        .userId(rs.getString("user_id"))
                        .rentDateTime(rs.getString("rent_date"))
                        .returnDateTime(rs.getString("return_date"))
                        .build();
                return Optional.of(rental);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return Optional.empty();
    }

    @Override
    public Optional<Rental> findByVehicleId(String vehicleId) {
        String sql = "SELECT * FROM rental WHERE vehicle_id = ?";
        Connection connection = JdbcConnectionManager.getInstance().getConnection();
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1,vehicleId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()){
                Rental rental = Rental.builder()
                        .id(rs.getString("id"))
                        .vehicleId(rs.getString("vehicle_id"))
                        .userId(rs.getString("user_id"))
                        .rentDateTime(rs.getString("rent_date"))
                        .returnDateTime(rs.getString("return_date"))
                        .build();
                return Optional.of(rental);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return Optional.empty();
    }

    @Override
    public Optional<Rental> findByUserId(String userId) {
        String sql = "SELECT * FROM rental WHERE user_id = ?";
        Connection connection = JdbcConnectionManager.getInstance().getConnection();
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1,userId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()){
                Rental rental = Rental.builder()
                        .id(rs.getString("id"))
                        .vehicleId(rs.getString("vehicle_id"))
                        .userId(rs.getString("user_id"))
                        .rentDateTime(rs.getString("rent_date"))
                        .returnDateTime(rs.getString("return_date"))
                        .build();
                return Optional.of(rental);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return Optional.empty();
    }

    @Override
    public Rental save(Rental rental) {
       if (rental.getId() ==null || rental.getId().isBlank()){
           rental.setId(UUID.randomUUID().toString());
       } else {
           deleteById(rental.getId());
       }

       String sql = "INSERT INTO rental (id, vehicle_id, user_id, rent_date, return_date) VALUES ? ? ? ? ?)";

       Connection connection = JdbcConnectionManager.getInstance().getConnection();
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1,rental.getId());
            stmt.setString(2,rental.getVehicleId());
            stmt.setString(3, rental.getUserId());
            stmt.setString(4, rental.getRentDateTime());
            stmt.setString(5, rental.getReturnDateTime());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return rental;
    }

    @Override
    public void deleteById(String id) {
        String sql = "DELETE FROM rental WHERE id = ?";

        Connection connection = JdbcConnectionManager.getInstance().getConnection();
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1,id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error occurred while deleting rental",e);
        }
    }

    @Override
    public Optional<Rental> findByVehicleIdAndReturnDateIsNull(String vehicleId) {
        String sql = "SELECT * FROM rental WHERE vehicle_id = ? AND return_date IS NULL";
        Connection connection = JdbcConnectionManager.getInstance().getConnection();;
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1,vehicleId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Rental rental = Rental.builder().id(rs.getString("id"))
                        .vehicleId(rs.getString("vehicle_id"))
                        .userId(rs.getString("user_id"))
                        .rentDateTime(rs.getString("rent_date"))
                        .returnDateTime("return_date")
                        .build();

                return Optional.of(rental);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return Optional.empty();
    }
}
