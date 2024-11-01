package com.lira.screenmatch.menu;

import com.lira.screenmatch.model.Episode;
import com.lira.screenmatch.model.EpisodeData;
import com.lira.screenmatch.model.SeasonData;
import com.lira.screenmatch.model.SeriesData;
import com.lira.screenmatch.service.ConsumeAPI;
import com.lira.screenmatch.service.ConvertData;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.Arrays.stream;

public class Menu {
    // Starting the class to read what is going to be typed.
    private Scanner read = new Scanner(System.in);

    // Starting the class that will call the API
    private ConsumeAPI consumeApi = new ConsumeAPI();

    // Starting the class of the converter that will organize my data.
    private ConvertData converter = new ConvertData();

    // Information about the API URL, should be hidden for security reasons but will be displayed here since this is only for study purposes.
    private final String URL = "https://www.omdbapi.com/?t=";
    private final String API_KEY = "&apikey=ff3c9083";

    public void showMenu() {
        // Asking for the name of the series the user wants to check.
        System.out.println("Type name of the Serie you want to look for.");
        var serieName = read.nextLine();

        // Calling the API with the name of the serie.
        var link = URL + serieName.replace(" ", "+") + API_KEY;
        var json = consumeApi.getData(link);

        // Converting the data from the API into the information that I defined and wanted.
        SeriesData seriesData = converter.getData(json, SeriesData.class);
        System.out.println(seriesData);

        // Creating list that will get all the season's information.
        List<SeasonData> seasons = new ArrayList<>();

        for (int i = 0; i < seriesData.totalSeasons(); i++) {
            link = URL + serieName.replace(" ", "+") + "&season=" + (i + 1) + API_KEY;
            json = consumeApi.getData(link);
            SeasonData dataSeason = converter.getData(json, SeasonData.class);
            seasons.add(dataSeason);
        }
        seasons.forEach(System.out::println);


//        for (int i = 0; i < seriesData.totalSeasons(); i++) {
//            List<EpisodeData> episodesSeason = seasons.get(i).episodes();
//            for (int j=0; j < episodesSeason.size(); j++) {
//                System.out.println(episodesSeason.get(j).title());
//            }
//        }

        // Same as te code above but done much simpler
        seasons.forEach(t -> t.episodes().forEach(e -> System.out.println(e.title())));

        // Allow me to do more than one
        List<EpisodeData> episodeDatas = seasons.stream()
                .flatMap(t -> t.episodes().stream())
                .collect(Collectors.toList());
        // .toList(); -> Doesn't allow the change of the list.

        //
        episodeDatas.stream()
                .filter(e -> !e.rating().equalsIgnoreCase("N/A"))
                .sorted(Comparator.comparing(EpisodeData::rating).reversed())
                .limit(5)
                .forEach(System.out::println);

        // Organizing Episode data.
        List<Episode> episodeList = seasons.stream()
                .flatMap(t -> t.episodes().stream()
                        .map(d -> new Episode(t.seasonNumber(), d))
                ).collect(Collectors.toList());
        episodeList.forEach(System.out::println);

        // Getting a list of episodes based on the year
//        System.out.println("Choose an year to filter the episodes.");
//        var year = read.nextInt();
//        read.nextLine();
//
//        LocalDate dateSearch = LocalDate.of(year, 1, 1);
//
//        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
//
//        episodeList.stream()
//                .filter(e -> e.getReleaseDate() != null && e.getReleaseDate().isAfter(dateSearch))
//                .forEach(e -> System.out.println(
//                        "Season: " + e.getSeason() +
//                                "Episode: " + e.getEpisodeNumber() +
//                                "Release Date: " + e.getReleaseDate().format(dateFormatter)
//                ));

        DoubleSummaryStatistics est = episodeList.stream()
                .filter(e -> e.getRating() > 0.0)
                .collect(Collectors.summarizingDouble(Episode::getRating));
        System.out.println(est);

    }
}
