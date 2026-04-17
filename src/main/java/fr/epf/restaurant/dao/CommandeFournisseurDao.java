package fr.epf.restaurant.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import fr.epf.restaurant.dto.CreerCommandeFournisseurRequest;
import fr.epf.restaurant.dto.CreerCommandeFournisseurRequest.LigneCommandeFournisseur;
import fr.epf.restaurant.model.CommandeFournisseur;

import java.sql.PreparedStatement;
import java.util.List;

@Repository
public class CommandeFournisseurDao {

    private final JdbcTemplate jdbcTemplate;
    private final FournisseurDao fournisseurDao;
    private final IngredientDao ingredientDao;

    public CommandeFournisseurDao(JdbcTemplate jdbcTemplate, FournisseurDao fournisseurDao,
            IngredientDao ingredientDao) {
        this.jdbcTemplate = jdbcTemplate;
        this.fournisseurDao = fournisseurDao;
        this.ingredientDao = ingredientDao;
    }

    private final RowMapper<CommandeFournisseur> getcommandeFournisseurRowMapper() {
        return (rs, rowNum) -> {
            CommandeFournisseur commandeFournisseur = new CommandeFournisseur();
            commandeFournisseur.setId(rs.getLong("id"));
            Long fournisseurId = rs.getLong("fournisseur_id");
            commandeFournisseur.setFournisseur(fournisseurDao.findById(fournisseurId));
            commandeFournisseur.setDateCommande(rs.getDate("date_commande"));
            commandeFournisseur.setStatut(rs.getString("statut"));
            return commandeFournisseur;
        };
    }

    private RowMapper<LigneCommandeFournisseur> getLigneCommandeFournisseurRowMapper() {
        return (rs, rowNum) -> {
            LigneCommandeFournisseur ligne = new LigneCommandeFournisseur();
            ligne.setQuantite(rs.getInt("quantite_commandee"));
            ligne.setPrixUnitaire(rs.getFloat("prix_unitaire"));
            long ingredientId = rs.getLong("ingredient_id");
            ligne.setIngredientId(ingredientId);
            ligne.setIngredient(ingredientDao.findById(ingredientId));
            return ligne;
        };
    }

    private void chargerLignes(CommandeFournisseur cmd) {
        String sqlLignes = """
                SELECT ingredient_id, quantite_commandee, prix_unitaire
                FROM LIGNE_COMMANDE_FOURNISSEUR WHERE commande_fournisseur_id = ?
                 """;
        List<LigneCommandeFournisseur> lignes = jdbcTemplate.query(
                sqlLignes,
                getLigneCommandeFournisseurRowMapper(),
                cmd.getId());
        cmd.setLignes(lignes);
    }

    public List<CommandeFournisseur> findByStatut(String statut) {
        String sql = "SELECT * FROM COMMANDE_FOURNISSEUR WHERE statut = ?";
        List<CommandeFournisseur> commandes = jdbcTemplate.query(sql, getcommandeFournisseurRowMapper(), statut);

        for (CommandeFournisseur cmd : commandes) {
            chargerLignes(cmd);
        }
        return commandes;
    }

    public List<CommandeFournisseur> findAll() {
        String sqlCommandes = "SELECT * FROM COMMANDE_FOURNISSEUR";
        List<CommandeFournisseur> commandes = jdbcTemplate.query(sqlCommandes, getcommandeFournisseurRowMapper());
        for (CommandeFournisseur cmd : commandes) {
            chargerLignes(cmd);
        }
        return commandes;
    }

    public CommandeFournisseur findById(Long fournisseurId) {
        String sqlCommande = "SELECT * FROM COMMANDE_FOURNISSEUR WHERE id = ?";
        List<CommandeFournisseur> commandes = jdbcTemplate.query(sqlCommande, getcommandeFournisseurRowMapper(),
                fournisseurId);

        if (commandes.isEmpty()) {
            return null;
        }

        CommandeFournisseur commandeFournisseur = commandes.get(0);

        String sqlLignes = """
                    SELECT ingredient_id, quantite_commandee, prix_unitaire
                    FROM LIGNE_COMMANDE_FOURNISSEUR
                    WHERE commande_fournisseur_id = ?
                """;

        List<LigneCommandeFournisseur> lignes = jdbcTemplate.query(sqlLignes, getLigneCommandeFournisseurRowMapper(),
                fournisseurId);

        commandeFournisseur.setLignes(lignes);

        return commandeFournisseur;
    }

    public Long createcommandeFournisseur(CreerCommandeFournisseurRequest commandeFournisseurRequest) {
        String sqlCommandeFournisseur = """
                    INSERT INTO COMMANDE_FOURNISSEUR(fournisseur_id, statut)
                    VALUES (?, ?)
                """;

        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sqlCommandeFournisseur, new String[] { "id" });
            ps.setLong(1, commandeFournisseurRequest.getFournisseurId());
            ps.setString(2, "EN_ATTENTE");
            return ps;
        }, keyHolder);

        Long commandeId = keyHolder.getKey().longValue();

        String sqlLigneCommandeFournisseur = """
                    INSERT INTO
                    LIGNE_COMMANDE_FOURNISSEUR
                    (commande_fournisseur_id, ingredient_id, quantite_commandee, prix_unitaire)
                    VALUES (?, ?, ?, ?)
                """;

        for (CreerCommandeFournisseurRequest.LigneCommandeFournisseur ligne : commandeFournisseurRequest.getLignes()) {
            jdbcTemplate.update(sqlLigneCommandeFournisseur, commandeId, ligne.getIngredientId(),
                    ligne.getQuantiteCommandee(),
                    ligne.getPrixUnitaire());
        }

        return commandeId;
    }

    public void delete(long id) {
        // supprimer lignes de commandes
        String sqlLignes = "DELETE FROM LIGNE_COMMANDE_FOURNISSEUR WHERE commande_fournisseur_id = ?";
        jdbcTemplate.update(sqlLignes, id);

        // supprimer la commande
        String sqlCommande = "DELETE FROM COMMANDE_FOURNISSEUR WHERE id = ?";
        jdbcTemplate.update(sqlCommande, id);
    }

    public void envoyer(Long fournisseurId) {
        String sqlCommande = "UPDATE COMMANDE_FOURNISSEUR set statut = 'ENVOYEE' WHERE id = ?";
        jdbcTemplate.update(sqlCommande, fournisseurId);
    }

    public void recevoir(Long fournisseurId) {
        String sqlCommande = "UPDATE COMMANDE_FOURNISSEUR set statut = 'RECUE' WHERE id = ?";
        jdbcTemplate.update(sqlCommande, fournisseurId);
    }

}
