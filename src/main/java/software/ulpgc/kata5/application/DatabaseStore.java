package software.ulpgc.kata5.application;

import software.ulpgc.kata5.architecture.io.Store;
import software.ulpgc.kata5.architecture.model.Movie;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;
import java.util.stream.Stream;

public class DatabaseStore implements Store {
    private final Connection connection;

    public DatabaseStore(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Stream<Movie> movies() {
        try {
            return moviesIn(query());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private Stream<Movie> moviesIn(ResultSet rs) {
        return Stream.generate(()->movieIn(rs)).takeWhile(Objects::nonNull);
    }

    private Movie movieIn(ResultSet rs) {
        try {
            return rs.next() ? readMovieFrom(rs) : null;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private Movie readMovieFrom(ResultSet rs) throws SQLException {
        return new Movie(
                rs.getString(1),
                rs.getInt(2),
                rs.getInt(3)
        );
    }


    private ResultSet query() throws SQLException {
        return connection.createStatement().executeQuery("SELECT title, year, duration FROM movies");
    }
}
