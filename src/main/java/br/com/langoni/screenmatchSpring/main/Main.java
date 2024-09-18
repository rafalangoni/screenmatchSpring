package br.com.langoni.screenmatchSpring.main;

import br.com.langoni.screenmatchSpring.model.Episode;
import br.com.langoni.screenmatchSpring.model.EpisodeData;
import br.com.langoni.screenmatchSpring.model.SeasonData;
import br.com.langoni.screenmatchSpring.model.SeriesData;
import br.com.langoni.screenmatchSpring.service.ConsumeApi;
import br.com.langoni.screenmatchSpring.service.ConvertData;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Main {

    private static final String URL = "http://www.omdbapi.com/?";
    private static final String APIKEY = "apikey=eb8d937f&t=";
    private ConsumeApi consumeApi = new ConsumeApi();
    private ConvertData converter = new ConvertData();
    private Scanner read = new Scanner(System.in);

    public void showMenu() {
        System.out.println("Type the name of serie to search: ");
        var seriesName = read.nextLine();
        var json = consumeApi.getData(URL + APIKEY + seriesName.replace(" ", "+"));
        SeriesData seriesData = converter.getData(json, SeriesData.class);
        System.out.println(seriesData);

        List<SeasonData> seasons = new ArrayList<>();

        for (int i = 1; i <= seriesData.totalOfSeasons(); i++) {
            json = consumeApi.getData(URL + APIKEY + seriesName.replace(" ", "+") + "&season=" + i);
            SeasonData seasonData = converter.getData(json, SeasonData.class);
            seasons.add(seasonData);
        }
        seasons.forEach(System.out::println);

        //Using Lambdas to have more code legibility
        seasons.forEach(t -> t.episodes().forEach(e -> System.out.println(e.title())));

        List<EpisodeData> episodeData = seasons.stream()
                .flatMap(t -> t.episodes().stream())
                .collect(Collectors.toList());

        System.out.println("\nTop 5 episodes");
        episodeData.stream()
                .filter(e -> !e.rating().equalsIgnoreCase("N/A"))
                .sorted(Comparator.comparing(EpisodeData::rating).reversed())
                .limit(5)
                .forEach(System.out::println);

        List<Episode> episodes = seasons.stream()
                .flatMap(t -> t.episodes().stream()
                .map(ed -> new Episode(t.seasonNumber(), ed)))
                .collect(Collectors.toList());

        episodes.forEach(System.out::println);
    }
}
