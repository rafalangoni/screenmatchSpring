package br.com.langoni.screenmatchSpring.main;

import br.com.langoni.screenmatchSpring.model.Episode;
import br.com.langoni.screenmatchSpring.model.EpisodeData;
import br.com.langoni.screenmatchSpring.model.SeasonData;
import br.com.langoni.screenmatchSpring.model.SeriesData;
import br.com.langoni.screenmatchSpring.service.ConsumeApi;
import br.com.langoni.screenmatchSpring.service.ConvertData;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
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

        System.out.println("\nTop 10 episodes");
        episodeData.stream()
                .filter(e -> !e.rating().equalsIgnoreCase("N/A"))
                .peek(e -> System.out.println("filter N/A " + e))
                .sorted(Comparator.comparing(EpisodeData::rating).reversed())
                .peek(e -> System.out.println("order " + e))
                .limit(10)
                .peek(e -> System.out.println("limit " + e))
                .map(e -> e.title().toUpperCase())
                .peek(e -> System.out.println("map " + e))
                .forEach(System.out::println);

        List<Episode> episodes = seasons.stream()
                .flatMap(t -> t.episodes().stream()
                        .map(ed -> new Episode(t.seasonNumber(), ed)))
                .collect(Collectors.toList());

        episodes.forEach(System.out::println);

//        System.out.println("Please type an excerpt of episode");
//        var excerptTitle = read.nextLine();
//
//        //Optional: the object could exist or not
//        Optional<Episode> searchedEpisode = episodes.stream()
//                .filter(e -> e.getTitle().toLowerCase().contains(excerptTitle.toLowerCase()))
//                .findFirst();
//        if(searchedEpisode.isPresent()){
//            System.out.println("Find episode!");
//            System.out.println("Season: "+searchedEpisode.get().getSeason());
//        }else{
//            System.out.println("Episode not found!");
//        }

//        System.out.println("Which year do you want to see the episodes?");
//        var year = read.nextInt();
//        read.nextLine();
//
//        //Searching for specific date, formatting date first
//        LocalDate searchDate = LocalDate.of(year, 1, 1);
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
//
//        episodes.stream()
//                .filter(e -> e.getLaunchData() != null && e.getLaunchData().isAfter(searchDate))
//                .forEach(e -> System.out.println(
//                        "Season: "+e.getSeason() +
//                                "Episode: "+e.getTitle()+
//                                "Launch data: "+e.getLaunchData().format(formatter)
//                ));

        Map<Integer, Double> ratingBySeason = episodes.stream()
                .filter(e -> e.getRating() > 0.0)
                .collect(Collectors.groupingBy(Episode::getSeason,
                        Collectors.averagingDouble(Episode::getRating)));
        System.out.println(ratingBySeason);

        DoubleSummaryStatistics est = episodes.stream()
                .filter(e-> e.getRating() > 0)
                .collect(Collectors.summarizingDouble(Episode::getRating));

        System.out.println("Average: "+est.getAverage());
        System.out.println("Best episode: "+est.getMax());
        System.out.println("Worst episode: "+est.getMin());
    }
}
