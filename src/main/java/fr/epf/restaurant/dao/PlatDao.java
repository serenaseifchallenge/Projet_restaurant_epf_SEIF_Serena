package fr.epf.restaurant.dao;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import fr.epf.restaurant.exception.ResourceNotFoundException;
import fr.epf.restaurant.model.Ingredient;
import fr.epf.restaurant.model.Plat;
import fr.epf.restaurant.model.PlatIngredient;

import java.sql.PreparedStatement;
import java.util.List;

@Repository
public class PlatDao {

    private final JdbcTemplate jdbcTemplate;

    public PlatDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<Plat> platRowMapper = (rs, rowNum) -> {
        Plat plat = new Plat();
        plat.setId(rs.getLong("id"));
        plat.setNom(rs.getString("nom"));
        plat.setDescription(rs.getString("description"));
        plat.setPrix(rs.getFloat("prix"));
        return plat;
    };

    private final RowMapper<PlatIngredient> platIngredientRowMapper = (rs, rowNum) -> {
        PlatIngredient pi = new PlatIngredient();
        pi.setQuantiteRequise(rs.getDouble("quantite_requise"));
        Ingredient ing = new Ingredient();
        ing.setId(rs.getLong("id"));
        ing.setNom(rs.getString("nom"));
        ing.setUnite(rs.getString("unite"));
        ing.setStockActuel(rs.getDouble("stock_actuel"));
        ing.setSeuilAlerte(rs.getDouble("seuil_alerte"));
        pi.setIngredient(ing);
        return pi;
    };

    public List<Plat> findAll() {
        String sql = "SELECT * FROM PLAT";
        List<Plat> plats = jdbcTemplate.query(sql, platRowMapper);
        for (Plat plat : plats) {
            String sqlIngredients = """
                        SELECT pi.quantite_requise, i.nom, i.unite
                        FROM PLAT_INGREDIENT pi
                        JOIN INGREDIENT i ON pi.ingredient_id = i.id
                        WHERE pi.plat_id = ?
                    """;

            List<PlatIngredient> ingredients = jdbcTemplate.query(sqlIngredients, (rs, rowNum) -> {
                PlatIngredient pi = new PlatIngredient();
                pi.setQuantiteRequise(rs.getDouble("quantite_requise"));
                Ingredient ing = new Ingredient();
                ing.setNom(rs.getString("nom"));
                ing.setUnite(rs.getString("unite"));
                pi.setIngredient(ing);

                return pi;
            }, plat.getId());

            plat.setIngredients(ingredients);
        }
        return plats;
    }

    public Plat findById(long id) throws ResourceNotFoundException {
        try {
            String sqlPlat = "SELECT * FROM PLAT WHERE id = ?";
            Plat plats = jdbcTemplate.queryForObject(sqlPlat, platRowMapper, id);

            if (plats == null) {
                return null;
            }

            String sqlIngredients = """
                        SELECT i.*, pi.quantite_requise
                        FROM INGREDIENT i
                        JOIN PLAT_INGREDIENT pi ON i.id = pi.ingredient_id
                        WHERE pi.plat_id = ?
                    """;

            List<PlatIngredient> ingredients = jdbcTemplate.query(sqlIngredients, platIngredientRowMapper, id);
            plats.setIngredients(ingredients);

            return plats;

        } catch (DataAccessException e) {
            throw new RuntimeException("Erreur technique lors de la récupération des details d'un plat", e);
        }
    }

public Plat create(Plat plat) {
    String sqlPlat = "INSERT INTO PLAT(nom, description, prix) VALUES(?, ?, ?)";
    String sqlIngPlat = "INSERT INTO PLAT_INGREDIENT(plat_id, ingredient_id, quantite_requise) VALUES(?, ?, ?)";

    GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();

    jdbcTemplate.update(connection -> {
        PreparedStatement ps = connection.prepareStatement(sqlPlat, new String[] { "id" });
        ps.setString(1, plat.getNom());
        ps.setString(2, plat.getDescription());
        ps.setFloat(3, plat.getPrix());
        return ps;
    }, keyHolder);

    Long platId = keyHolder.getKey().longValue();
    plat.setId(platId);


    for (PlatIngredient pi : plat.getIngredients()) {
        jdbcTemplate.update(sqlIngPlat,platId,pi.getIngredient().getId(),pi.getQuantiteRequise());
    }

    return plat;
}
}