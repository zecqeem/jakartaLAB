package org.example.jakartalabs.repository;

import org.example.jakartalabs.db.ConnectionManager;
import org.example.jakartalabs.model.Apartment;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ApartmentRepositoryJdbc implements ApartmentRepository {

    private static final String TABLE = "apartments";

    @Override
    public List<Apartment> findAll() {
        List<Apartment> result = new ArrayList<>();
        String sql = "SELECT id, title, rooms, price, description FROM " + TABLE;
        try (Connection con = ConnectionManager.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                result.add(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    @Override
    public Optional<Apartment> findById(int id) {
        String sql = "SELECT id, title, rooms, price, description FROM " + TABLE + " WHERE id = ?";
        try (Connection con = ConnectionManager.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return Optional.empty();
    }

    @Override
    public List<Apartment> findByCriteria(Integer rooms, Integer maxPrice) {
        List<Apartment> result = new ArrayList<>();
        List<Object> params = new ArrayList<>();

        StringBuilder sql = new StringBuilder(
                "SELECT id, title, rooms, price, description FROM " + TABLE + " WHERE 1=1");

        if (rooms != null) {
            sql.append(" AND rooms = ?");
            params.add(rooms);
        }
        if (maxPrice != null) {
            sql.append(" AND price <= ?");
            params.add(maxPrice);
        }

        try (Connection con = ConnectionManager.getConnection();
             PreparedStatement ps = con.prepareStatement(sql.toString())) {
            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    result.add(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    @Override
    public Apartment save(Apartment apt) {
        String sql = "INSERT INTO " + TABLE +
                " (title, rooms, price, description) VALUES (?, ?, ?, ?)";
        try (Connection con = ConnectionManager.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, apt.getTitle());
            ps.setInt(2, apt.getRooms());
            ps.setInt(3, apt.getPrice());
            ps.setString(4, apt.getDescription());
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    apt.setId(keys.getInt(1));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return apt;
    }

    @Override
    public Apartment update(Apartment apt) {
        String sql = "UPDATE " + TABLE +
                " SET title = ?, rooms = ?, price = ?, description = ? WHERE id = ?";
        try (Connection con = ConnectionManager.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, apt.getTitle());
            ps.setInt(2, apt.getRooms());
            ps.setInt(3, apt.getPrice());
            ps.setString(4, apt.getDescription());
            ps.setInt(5, apt.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return apt;
    }

    @Override
    public boolean deleteById(int id) {
        String sql = "DELETE FROM " + TABLE + " WHERE id = ?";
        try (Connection con = ConnectionManager.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private Apartment mapRow(ResultSet rs) throws SQLException {
        return new Apartment(
                rs.getInt("id"),
                rs.getString("title"),
                rs.getInt("rooms"),
                rs.getInt("price"),
                rs.getString("description")
        );
    }
}
