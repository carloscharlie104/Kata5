package software.ulpgc.kata5.application.beta;

import software.ulpgc.kata5.application.*;
import software.ulpgc.kata5.architecture.model.Movie;
import software.ulpgc.kata5.architecture.tasks.HistogramBuilder;
import software.ulpgc.kata5.architecture.viewmodel.Histogram;

import java.io.File;
import java.sql.*;
import java.util.stream.Stream;

public class Main {
    private static final File database = new File("movies.db");
    public static void main(String[] args) throws SQLException {
        try (Connection connection = openConnection()) {
            importDatabaseFromRemoteIfRequiredWith(connection);
            Stream<Movie> movies = new DatabaseStore(connection).movies()
                    .filter(m->m.year() > 1900)
                    .filter(m->m.year() < 2025);
            MainFrame.create()
                    .display(histogramOf(movies))
                    .setVisible(true);
        }
    }

    private static void importDatabaseFromRemoteIfRequiredWith(Connection connection) throws SQLException {
        if (database.length() > 0) return;
        new DatabaseRecorder(connection).record(new RemoteStore(Deserializer::fromTsv).movies());
    }

    private static Connection openConnection() throws SQLException {
        Connection connection = DriverManager.getConnection("jdbc:sqlite:" + database.getAbsolutePath());
        connection.setAutoCommit(false);
        return connection;
    }

    private static Histogram histogramOf(Stream<Movie> movies) {
        return HistogramBuilder.with(movies)
                .title("Movies per year")
                .x("Year")
                .y("Count")
                .legend("Movies")
                .build(Movie::year);
    }

}
