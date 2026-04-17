package fr.epf.restaurant.dao;

import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import fr.epf.restaurant.model.Fournisseur;

@Repository
public class FournisseurDao {

    private final JdbcTemplate jdbcTemplate;

    public FournisseurDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<Fournisseur> fournisseurRowMapper = (rs, rowNum) -> {
        Fournisseur fournisseur = new Fournisseur();
        fournisseur.setId(rs.getLong("id"));
        fournisseur.setNom(rs.getString("nom"));
        fournisseur.setContact(rs.getString("contact"));
        fournisseur.setEmail(rs.getString("email"));
        return fournisseur;
    };

    public List<Fournisseur> findAll() {
        String sql = "SELECT * FROM FOURNISSEUR";
        return jdbcTemplate.query(sql, fournisseurRowMapper);
    }

    public List<Map<String, Object>> findCatalogueByFournisseurId(long fournisseurId) {
        String sql = """
                    SELECT i.id AS ingredientId,
                           i.nom AS ingredientNom,
                           i.unite AS ingredientUnite,
                           fi.prix_unitaire AS prixUnitaire
                    FROM FOURNISSEUR_INGREDIENT fi
                    JOIN INGREDIENT i ON i.id = fi.ingredient_id
                    WHERE fi.fournisseur_id = ?
                """;

        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            Map<String, Object> map = new java.util.LinkedHashMap<>();
            map.put("ingredientId", rs.getLong("ingredientId"));
            map.put("ingredientNom", rs.getString("ingredientNom"));
            map.put("ingredientUnite", rs.getString("ingredientUnite"));
            map.put("prixUnitaire", rs.getBigDecimal("prixUnitaire"));
            return map;
        }, fournisseurId);
    }

    public Fournisseur findById(long id) {
        String sql = "SELECT * FROM FOURNISSEUR WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, fournisseurRowMapper, id);
    }
}
