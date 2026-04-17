package fr.epf.restaurant.dao;

import java.sql.PreparedStatement;
import java.util.List;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import fr.epf.restaurant.dto.CreerCommandeClientRequest;
import fr.epf.restaurant.dto.CreerCommandeClientRequest.LigneCommandeClient;
import fr.epf.restaurant.model.CommandeClient;

@Repository
public class CommandeClientDao {

    private final JdbcTemplate jdbcTemplate;
    private final ClientDao clientDao;
    private final PlatDao platDao;

    public CommandeClientDao(JdbcTemplate jdbcTemplate, ClientDao clientDao, PlatDao platDao) {
        this.jdbcTemplate = jdbcTemplate;
        this.clientDao = clientDao;
        this.platDao = platDao;
    }

    private RowMapper<CommandeClient> getCommandeClientRowMapper() {
        return (rs, rowNum) -> {
            CommandeClient commandeClient = new CommandeClient();
            commandeClient.setId(rs.getLong("id"));
            Long clientId = rs.getLong("client_id");
            commandeClient.setClient(clientDao.findById(clientId));
            commandeClient.setDateCommande(rs.getDate("date_commande"));
            commandeClient.setStatut(rs.getString("statut"));
            return commandeClient;
        };
    }

    private RowMapper<LigneCommandeClient> getLigneCommandeClientRowMapper() {
        return (rs, rowNum) -> {
            LigneCommandeClient ligne = new LigneCommandeClient();
            ligne.setQuantite(rs.getInt("quantite"));
            long platId = rs.getLong("plat_id");
            ligne.setPlatId(platId);
            ligne.setPlat(platDao.findById(platId));
            return ligne;
        };
    }

    public List<CommandeClient> findAll() {
        String sql = "SELECT * FROM COMMANDE_CLIENT";
        List<CommandeClient> commandes = jdbcTemplate.query(sql, getCommandeClientRowMapper());

        for (CommandeClient cmd : commandes) {
            String sqlLignes = "SELECT plat_id, quantite FROM LIGNE_COMMANDE_CLIENT WHERE commande_client_id = ?";
            List<LigneCommandeClient> lignes = jdbcTemplate.query(
                sqlLignes, getLigneCommandeClientRowMapper(),
                    cmd.getId());
            cmd.setLignes(lignes);
        }
        return commandes;
    }

    public void delete(long id) {
        // supprimer lignes de commandes
        String sqlLignes = "DELETE FROM LIGNE_COMMANDE_CLIENT WHERE commande_client_id = ?";
        jdbcTemplate.update(sqlLignes, id);

        // supprimer la commande
        String sqlCommande = "DELETE FROM COMMANDE_CLIENT WHERE id = ?";
        jdbcTemplate.update(sqlCommande, id);
    }

    public Long createCommandeClient(CreerCommandeClientRequest commandeClientRequest) {
        String sqlCommandeClient = """
                    INSERT INTO COMMANDE_CLIENT(client_id, statut)
                    VALUES (?, ?)
                """;

        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sqlCommandeClient, new String[] { "id" });
            ps.setLong(1, commandeClientRequest.getClientId());
            ps.setString(2, "EN_ATTENTE");
            return ps;
        }, keyHolder);

        Long commandeId = keyHolder.getKey().longValue();

        String sqlLigneCommandeClient = """
                    INSERT INTO LIGNE_COMMANDE_CLIENT(commande_client_id, plat_id, quantite)
                    VALUES (?, ?, ?)
                """;

        for (CreerCommandeClientRequest.LigneCommandeClient ligne : commandeClientRequest.getLignes()) {
            jdbcTemplate.update(sqlLigneCommandeClient, commandeId, ligne.getPlatId(), ligne.getQuantite());
        }

        return commandeId;
    }

    public List<CommandeClient> findByStatut(String statut) {
        String sql = "SELECT * FROM COMMANDE_CLIENT WHERE statut = ?";
        List<CommandeClient> commandes = jdbcTemplate.query(sql, getCommandeClientRowMapper(), statut);

        for (CommandeClient cmd : commandes) {
            String sqlLignes = "SELECT plat_id, quantite FROM LIGNE_COMMANDE_CLIENT WHERE commande_client_id = ?";
            List<LigneCommandeClient> lignes = jdbcTemplate.query(
                sqlLignes, getLigneCommandeClientRowMapper(),
                    cmd.getId());
            cmd.setLignes(lignes);
        }

        return commandes;
    }

    public CommandeClient findById(Long commandeId) {
        String sqlCommande = "SELECT * FROM COMMANDE_CLIENT WHERE id = ?";
        List<CommandeClient> commandes = jdbcTemplate.query(sqlCommande, getCommandeClientRowMapper(), commandeId);

        if (commandes.isEmpty()) {
            return null;
        }

        CommandeClient commandeClient = commandes.get(0);

        String sqlLignes = """
                    SELECT plat_id, quantite
                    FROM LIGNE_COMMANDE_CLIENT
                    WHERE commande_client_id = ?
                """;

        List<LigneCommandeClient> lignes = jdbcTemplate.query(
            sqlLignes, getLigneCommandeClientRowMapper(), commandeId);

        commandeClient.setLignes(lignes);

        return commandeClient;
    }

    public void preparer(Long commandeId) {
        String sqlCommande = "UPDATE COMMANDE_CLIENT set statut = 'EN_PREPARATION' WHERE id = ?";
        jdbcTemplate.update(sqlCommande, commandeId);
    }

    public void servir(Long commandeId) {
        String sqlCommande = "UPDATE COMMANDE_CLIENT set statut = 'SERVIE' WHERE id = ?";
        jdbcTemplate.update(sqlCommande, commandeId);
    }

}