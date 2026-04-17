package fr.epf.restaurant.dao;

import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import fr.epf.restaurant.model.Ingredient;

@Repository
public class IngredientDao {

    private final JdbcTemplate jdbcTemplate;

    public IngredientDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<Ingredient> ingredientRowMapper = (rs, rowNum) -> {
        Ingredient ingredient = new Ingredient();
        ingredient.setId(rs.getLong("id"));
        ingredient.setNom(rs.getString("nom"));
        ingredient.setUnite(rs.getString("unite"));
        ingredient.setStockActuel(rs.getDouble("stock_actuel"));
        ingredient.setSeuilAlerte(rs.getDouble("seuil_alerte"));
        return ingredient;
    };

    public List<Ingredient> findAll() {
        String sql = "SELECT * FROM INGREDIENT";
        return jdbcTemplate.query(sql, ingredientRowMapper);
    }

    public Ingredient findById(long ingredientId) {
        String sql = "SELECT * FROM INGREDIENT WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, ingredientRowMapper, ingredientId);
    }

    public List<Ingredient> findSousAlerte() {
        String sql = "SELECT * FROM INGREDIENT WHERE stock_actuel < seuil_alerte";
        return jdbcTemplate.query(sql, ingredientRowMapper);
    }

    public List<Map<String, Object>> findPrixParIngredient(long ingredientId) {
        String sql = """
                    SELECT f.id AS fournisseurId,
                           f.nom AS fournisseurNom,
                           fi.prix_unitaire AS prixUnitaire
                    FROM FOURNISSEUR f
                    JOIN FOURNISSEUR_INGREDIENT fi ON f.id = fi.fournisseur_id
                    WHERE fi.ingredient_id = ?
                """;
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            Map<String, Object> map = new java.util.LinkedHashMap<>();
            map.put("fournisseurId", rs.getLong("fournisseurId"));
            map.put("fournisseurNom", rs.getString("fournisseurNom"));
            map.put("prixUnitaire", rs.getBigDecimal("prixUnitaire"));
            return map;
        }, ingredientId);
    }

    public void updateStock(Long ingredientId, double stockEnlever) {
        String sqlCommande = "UPDATE INGREDIENT set stock_actuel = stock_actuel + ? WHERE id = ?";
        jdbcTemplate.update(sqlCommande, stockEnlever, ingredientId);
    }

    // test
    public void update(Ingredient ingredient) {
        String sql = "UPDATE ingredient SET nom = ?, stock_actuel = ?, seuil_alerte = ?, unite = ? WHERE id = ?";

        jdbcTemplate.update(sql,
                ingredient.getNom(),
                ingredient.getStockActuel(),
                ingredient.getSeuilAlerte(),
                ingredient.getUnite(),
                ingredient.getId());
    }
}
