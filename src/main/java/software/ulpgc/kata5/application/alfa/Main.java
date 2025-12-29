package software.ulpgc.kata5.application.alfa;

import software.ulpgc.kata5.application.Deserializer;
import software.ulpgc.kata5.application.MainFrame;
import software.ulpgc.kata5.application.RemoteStore;
import software.ulpgc.kata5.architecture.tasks.HistogramBuilder;
import software.ulpgc.kata5.architecture.model.Movie;
import software.ulpgc.kata5.architecture.viewmodel.Histogram;

import java.util.stream.Stream;

public class Main {
    public static void main(String[] args) {
        MainFrame.create()
                .display(histogramOf(movies()))
                .setVisible(true);
    }

    private static Stream<Movie> movies() {
        return new RemoteStore(Deserializer::fromTsv).movies()
                .filter(m->m.year() > 1900)
                .filter(m->m.year() < 2025);

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
