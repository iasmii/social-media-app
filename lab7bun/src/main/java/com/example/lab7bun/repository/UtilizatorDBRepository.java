package com.example.lab7bun.repository;

import com.example.lab7bun.domain.Utilizator;
import com.example.lab7bun.domain.validators.UtilizatorValidator;

import java.sql.*;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class UtilizatorDBRepository implements Repository<Long, Utilizator> {
    private String url;
    private String username;
    private String password;
    private UtilizatorValidator utilizatorValidator;

    public UtilizatorDBRepository(String url, String username, String password, UtilizatorValidator utilizatorValidator){
        this.url=url;
        this.username=username;
        this.password=password;
        this.utilizatorValidator=utilizatorValidator;
    }

    @Override
    public Optional<Utilizator> findOne(Long id) {
        try(Connection connection = DriverManager.getConnection(url, username, password);
            PreparedStatement statement = connection.prepareStatement("select * from utilizator " +
                    "where id = ?");

        ) {
            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();
            if(resultSet.next()) {
                String nume = resultSet.getString("nume");
                String prenume = resultSet.getString("prenume");
                String parola=resultSet.getString("parola");
                Utilizator u = new Utilizator(nume,prenume,parola);
                u.setId(id);
                return Optional.ofNullable(u);
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Iterable<Utilizator> findAll() {
        Set<Utilizator> users = new HashSet<>();

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("select * from utilizator");
             ResultSet resultSet = statement.executeQuery()
        ) {

            while (resultSet.next())
            {
                Long id= resultSet.getLong("id");
                String nume=resultSet.getString("nume");
                String prenume=resultSet.getString("prenume");
                String parola=resultSet.getString("parola");
                Utilizator user=new Utilizator(nume,prenume,parola);
                user.setId(id);
                users.add(user);

            }
            return users;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<Utilizator> save(Utilizator entity) {
        utilizatorValidator.validate(entity);
        String insertSQL="insert into utilizator (id,nume,prenume,parola) values(?,?,?,?)";
        try (var connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement=connection.prepareStatement(insertSQL);)
        {
            statement.setLong(1,entity.getId());
            statement.setString(2,entity.getNume());
            statement.setString(3,entity.getPrenume());
            statement.setString(4,entity.getParola());
            int response=statement.executeUpdate();
            return response==0 ? Optional.of(entity) : Optional.empty();
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<Utilizator> delete(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Id cannot be null");
        }

        String deleteSQL="delete from utilizator where id=?";
        try (var connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(deleteSQL);
        ) {
            statement.setLong(1, id);

            Optional<Utilizator> foundUser = findOne(id);

            int response = 0;
            if (foundUser.isPresent()) {
                response = statement.executeUpdate();
            }

            return response == 0 ? Optional.empty() : foundUser;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<Utilizator> update(Utilizator entity) {
        utilizatorValidator.validate(entity);
        if(entity==null)
        {
            throw new IllegalArgumentException("Entity cannot be null!");
        }
        String updateSQL="update utilizator set nume=?,prenume=?,parola=? where id=?";
        try(var connection= DriverManager.getConnection(url, username, password);
            PreparedStatement statement=connection.prepareStatement(updateSQL);)
        {
            statement.setString(1,entity.getNume());
            statement.setString(2,entity.getPrenume());
            statement.setString(3,entity.getParola());
            statement.setLong(4,entity.getId());

            int response= statement.executeUpdate();
            return response==0 ? Optional.of(entity) : Optional.empty();
        }
        catch (SQLException e)
        {
            throw new RuntimeException(e);
        }
    }

    public Iterable<Utilizator> paginare(int inregistariPePagina, int paginaCurenta) throws Exception{
        Set<Utilizator> users = new HashSet<>();
        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            // numarare inregistrari din tabel
            Statement countStatement = connection.createStatement();
            ResultSet countResultSet = countStatement.executeQuery("SELECT COUNT(*) AS total_count FROM utilizator");
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
            String query = "SELECT * FROM utilizator ORDER BY id LIMIT ? OFFSET ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, inregistariPePagina);
            statement.setInt(2, offset);

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Long id= resultSet.getLong("id");
                String nume=resultSet.getString("nume");
                String prenume=resultSet.getString("prenume");
                String parola=resultSet.getString("parola");
                Utilizator user=new Utilizator(nume,prenume,parola);
                user.setId(id);
                users.add(user);
            }

            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }
}
