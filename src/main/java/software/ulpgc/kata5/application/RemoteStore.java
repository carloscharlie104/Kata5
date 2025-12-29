package software.ulpgc.kata5.application;

import software.ulpgc.kata5.architecture.io.Store;
import software.ulpgc.kata5.architecture.model.Movie;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.function.Function;
import java.util.stream.Stream;
import java.util.zip.GZIPInputStream;

public class RemoteStore implements Store {
    private final Function<String, Movie> deserialize;

    public RemoteStore(Function<String, Movie> deserialize) {
        this.deserialize = deserialize;
    }

    @Override
    public Stream<Movie> movies() {
        try {
            return moviesFrom(new URL("https://datasets.imdbws.com/title.basics.tsv.gz"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private Stream<Movie> moviesFrom(URL url) throws IOException {
        return moviesFrom(url.openConnection());
    }

    private Stream<Movie> moviesFrom(URLConnection connection) throws IOException {
        return moviesFrom(unzip(connection.getInputStream()));
    }

    private Stream<Movie> moviesFrom(InputStream inputStream) throws IOException {
        return moviesFrom(toReader(inputStream)).onClose(()->close(inputStream));
    }

    private void close(InputStream inputStream) {
        try {
            inputStream.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private Stream<Movie> moviesFrom(BufferedReader reader) throws IOException {
        return reader.lines().skip(1).map(deserialize);
    }


    private BufferedReader toReader(InputStream inputStream) {
        return new BufferedReader(new InputStreamReader(inputStream));
    }

    private InputStream unzip(InputStream inputStream) throws IOException {
        return new GZIPInputStream(new BufferedInputStream(inputStream));
    }
}
