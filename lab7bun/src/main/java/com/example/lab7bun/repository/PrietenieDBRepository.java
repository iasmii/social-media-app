package com.example.lab7bun.repository;

import com.example.lab7bun.domain.Mesaj;
import com.example.lab7bun.domain.Utilizator;
import com.example.lab7bun.domain.validators.PrietenieValidator;
import com.example.lab7bun.domain.Prietenie;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public class PrietenieDBRepository implements Repository<Long, Prietenie>{
    private String url;
    private String username;
    private String password;
    private PrietenieValidator prietenieValidator;
    private UtilizatorDBRepository utilizatorDBRepository;

    public PrietenieDBRepository(String url, String username, String password, PrietenieValidator prietenieValidator, UtilizatorDBRepository utilizatorDBRepository){
        this.url=url;
        this.username=username;
        this.password=password;
        this.prietenieValidator=prietenieValidator;
        this.utilizatorDBRepository=utilizatorDBRepository;
    }

    @Override
    public Optional<Prietenie> findOne(Long id) {
        try(Connection connection = DriverManager.getConnection(url, username, password);
            PreparedStatement statement = connection.prepareStatement("select * from prietenie " +
                    "where id = ?");

        ) {
            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();
            if(resultSet.next()) {
                Long id_ = resultSet.getLong("id");
                Long id1 = resultSet.getLong("id1");
                Long id2 = resultSet.getLong("id2");
                java.sql.Timestamp timestamp = resultSet.getTimestamp("din");
                LocalDateTime din = timestamp.toLocalDateTime();

                Optional<Utilizator> u1 = utilizatorDBRepository.findOne(id1);
                Optional<Utilizator> u2 = utilizatorDBRepository.findOne(id2);
                Prietenie prietenie = new Prietenie(u1, u2, din);
                prietenie.setId(id_);
                return Optional.ofNullable(prietenie);
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Iterable<Prietenie> findAll() {
        Set<Prietenie> prietenii = new HashSet<>();

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("select * from prietenie");
             ResultSet resultSet = statement.executeQuery()
        ) {

            while (resultSet.next())
            {
                Long id_ = resultSet.getLong("id");
                Long id1 = resultSet.getLong("id1");
                Long id2 = resultSet.getLong("id2");
                java.sql.Timestamp timestamp = resultSet.getTimestamp("din");
                LocalDateTime din = timestamp.toLocalDateTime();
                Optional<Utilizator> u1 = utilizatorDBRepository.findOne(id1);
                Optional<Utilizator> u2 = utilizatorDBRepository.findOne(id2);
                if (u1.isPresent() && u2.isPresent()) {
                    Prietenie prietenie = new Prietenie(u1, u2, din);
                    prietenie.setId(id_);
                    prietenii.add(prietenie);
                }

            }
            return prietenii;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<Prietenie> save(Prietenie entity) {
        prietenieValidator.validate(entity);
        String insertSQL="insert into prietenie (id,din,id1,id2) values(?,?,?,?)";
        try (var connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement=connection.prepareStatement(insertSQL);)
        {
            statement.setLong(1,entity.getId());
            //DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            //statement.setString(2, entity.getData().toString());
            java.sql.Timestamp timestamp = java.sql.Timestamp.valueOf(entity.getData());
            statement.setTimestamp(2,timestamp);
            statement.setLong(3,entity.getUtilizator1().getId());
            statement.setLong(4,entity.getUtilizator2().getId());
            int response=statement.executeUpdate();
            return response==0 ? Optional.of(entity) : Optional.empty();
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<Prietenie> delete(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Id cannot be null");
        }

        String deleteSQL="delete from prietenie where id=?";
        try (var connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(deleteSQL);
        ) {
            statement.setLong(1, id);

            Optional<Prietenie> prietenie = findOne(id);

            int response = 0;
            if (prietenie.isPresent()) {
                response = statement.executeUpdate();
            }

            return response == 0 ? Optional.empty() : prietenie;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<Prietenie> update(Prietenie entity) {
        prietenieValidator.validate(entity);
        if(entity==null)
        {
            throw new IllegalArgumentException("Entity cannot be null!");
        }
        String updateSQL="update prietenie set id1=?,id2=? where id=?";
        try(var connection= DriverManager.getConnection(url, username, password);
            PreparedStatement statement=connection.prepareStatement(updateSQL);)
        {
            statement.setLong(1,entity.getUtilizator1().getId());
            statement.setLong(2,entity.getUtilizator2().getId());
            statement.setLong(3,entity.getId());

            int response= statement.executeUpdate();
            return response==0 ? Optional.of(entity) : Optional.empty();
        }
        catch (SQLException e)
        {
            throw new RuntimeException(e);
        }
    }

    public void updateAll(List<Prietenie> entities) {
        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            connection.setAutoCommit(false); // Start a transaction

            try (PreparedStatement statement = connection.prepareStatement("UPDATE prietenie SET id1 = ?, id2 = ? WHERE id = ?")) {
                for (Prietenie entity : entities) {
                    statement.setString(1, entity.getUtilizator1().getId().toString());
                    statement.setString(2, entity.getUtilizator2().getId().toString());
                    statement.setString(3, entity.toString());
                    statement.addBatch();
                }

                statement.executeBatch();
                connection.commit(); // Commit the transaction
            } catch (SQLException e) {
                connection.rollback(); // Rollback the transaction if an error occurs
                System.out.println("Error while updating friendships: " + e);
            }
        } catch (SQLException e) {
            System.out.println("Error while establishing database connection: " + e);
        }
    }

    public Iterable<Prietenie> paginareVeche(int inregistariPePagina, int paginaCurenta) throws Exception{
        Set<Prietenie> prietenii = new HashSet<>();
        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            // numarare inregistrari din tabel
            Statement countStatement = connection.createStatement();
            ResultSet countResultSet = countStatement.executeQuery("SELECT COUNT(*) AS total_count FROM prietenie");
            int totalCount = 0;
            if (countResultSet.next()) {
                totalCount = countResultSet.getInt("total_count");
            }
            countResultSet.close();
            countStatement.close();

            // calcul nr total pagini
            int totalPages = (int) Math.ceil((double) totalCount / inregistariPePagina);

            if(paginaCurenta>totalPages){
                throw new Exception("Nu exista pagina dorita!");
            }

            // calcul offset bazat pe pagina curenta
            int offset = (paginaCurenta - 1) * inregistariPePagina;

            // extragere inregistrari din pagina curenta
            String query = "SELECT * FROM prietenie ORDER BY id LIMIT ? OFFSET ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, inregistariPePagina);
            statement.setInt(2, offset);

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Long id_ = resultSet.getLong("id");
                Long id1 = resultSet.getLong("id1");
                Long id2 = resultSet.getLong("id2");
                java.sql.Timestamp timestamp = resultSet.getTimestamp("din");
                LocalDateTime din = timestamp.toLocalDateTime();
                Optional<Utilizator> u1 = utilizatorDBRepository.findOne(id1);
                Optional<Utilizator> u2 = utilizatorDBRepository.findOne(id2);
                if (u1.isPresent() && u2.isPresent()) {
                    Prietenie prietenie = new Prietenie(u1, u2, din);
                    prietenie.setId(id_);
                    prietenii.add(prietenie);
                }
            }

            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return prietenii;
    }
    public Iterable<Prietenie> paginare(int inregistariPePagina, int paginaCurenta, Long userID) throws Exception {
        Set<Prietenie> prietenii = new HashSet<>();
        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            // numarare inregistrari din tabel care sunt legate de utilizatorul specificat
            PreparedStatement countStatement = connection.prepareStatement("SELECT COUNT(*) AS total_count FROM prietenie WHERE id1 = ? OR id2 = ?");
            countStatement.setLong(1, userID);
            countStatement.setLong(2, userID);

            ResultSet countResultSet = countStatement.executeQuery();
            int totalCount = 0;
            if (countResultSet.next()) {
                totalCount = countResultSet.getInt("total_count");
            }
            countResultSet.close();
            countStatement.close();

            // calcul nr total pagini
            int totalPages = (int) Math.ceil((double) totalCount / inregistariPePagina);

            if (paginaCurenta > totalPages && totalCount!=0) {
                throw new Exception("Nu exista pagina dorita!");
            }

            // calcul offset bazat pe pagina curenta
            int offset = (paginaCurenta - 1) * inregistariPePagina;

            // extragere inregistrari din pagina curenta, filtrate dupa userID
            String query = "SELECT * FROM prietenie WHERE id1 = ? OR id2 = ? ORDER BY id LIMIT ? OFFSET ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setLong(1, userID);
            statement.setLong(2, userID);
            statement.setInt(3, inregistariPePagina);
            statement.setInt(4, offset);

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Long id_ = resultSet.getLong("id");
                Long id1 = resultSet.getLong("id1");
                Long id2 = resultSet.getLong("id2");
                java.sql.Timestamp timestamp = resultSet.getTimestamp("din");
                LocalDateTime din = timestamp.toLocalDateTime();
                Optional<Utilizator> u1 = utilizatorDBRepository.findOne(id1);
                Optional<Utilizator> u2 = utilizatorDBRepository.findOne(id2);
                if (u1.isPresent() && u2.isPresent() && (id1.equals(userID) || id2.equals(userID))) {
                    Prietenie prietenie = new Prietenie(u1, u2, din);
                    prietenie.setId(id_);
                    prietenii.add(prietenie);
                }
            }

            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return prietenii;
    }

}
