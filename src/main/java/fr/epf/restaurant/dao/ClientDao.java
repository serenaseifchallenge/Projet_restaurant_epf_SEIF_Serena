package fr.epf.restaurant.dao;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import fr.epf.restaurant.model.Client;

import java.sql.PreparedStatement;
import java.util.List;

@Repository
public class ClientDao {

    private final JdbcTemplate jdbcTemplate;

    public ClientDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<Client> clientRowMapper = (rs, rowNum) -> {
        Client client = new Client();
        client.setId(rs.getLong("id"));
        client.setNom(rs.getString("nom"));
        client.setPrenom(rs.getString("prenom"));
        client.setEmail(rs.getString("email"));
        client.setTelephone(rs.getLong("telephone"));
        return client;
    };

    public List<Client> findAll() {
        String sql = "SELECT * FROM CLIENT";
        return jdbcTemplate.query(sql, clientRowMapper);
    }

    public Client findById(long id) {
        try {
            String sql = "SELECT * FROM CLIENT WHERE id = ?";
            return jdbcTemplate.queryForObject(sql, clientRowMapper, id);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public Client create(Client client) {
        String sql = "INSERT INTO CLIENT(nom, prenom, email, telephone) VALUES(?, ?, ?, ?)";
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[] { "id" });
            ps.setString(1, client.getNom());
            ps.setString(2, client.getPrenom());
            ps.setString(3, client.getEmail());
            ps.setLong(4, client.getTelephone());
            return ps;
        }, keyHolder);

        client.setId(keyHolder.getKey().longValue());
        return client;
    }
}