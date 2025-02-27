import java.awt.color.ICC_ColorSpace;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Array;
import java.sql.SQLOutput;
import java.util.*;


public class MovieCollection
{
    private ArrayList<Movie> movies;
    private ArrayList<String[]> movieCast = new ArrayList<String[]>();
    private ArrayList<String []> movieGenre = new ArrayList<String[]>();
    private Scanner scanner;
    private ArrayList<Movie> topMoviesRate = new ArrayList<>();
    private ArrayList<Movie> topMoviesRev = new ArrayList<>();


    public MovieCollection(String fileName)
    {
        String[] cast;
        String[] genre;
        importMovieList(fileName);
        scanner = new Scanner(System.in);
        for (Movie m : movies){
            cast = m.getCast().split("\\|");
            genre = m.getGenres().split("\\|");
            movieCast.add(cast);
            movieGenre.add(genre);
        }
        Collections.reverse(topMoviesRate = movieSort(movies,true));
        Collections.reverse(topMoviesRev = movieSort(movies,false));
    }

    public ArrayList<Movie> movieSort(ArrayList<Movie> movies, boolean rating) {
        ArrayList<Movie> movie50 = new ArrayList<>(movies);
        if (rating) {
            Collections.sort(movie50, new Comparator<Movie>() {
                public int compare(Movie a, Movie b) {
                    return Double.compare(a.getUserRating(), b.getUserRating());
                }
            });
            return movie50;
        }
        else {
            Collections.sort(movie50, new Comparator<Movie>() {
                public int compare(Movie a, Movie b) {
                    return Double.compare(a.getRevenue(), b.getRevenue());
                }
            });
            return movie50;
        }
    }



    public ArrayList<Movie> getMovies()
    {
        return movies;
    }


    public void menu()
    {
        String menuOption = "";


        System.out.println("Welcome to the movie collection!");
        System.out.println("Total: " + movies.size() + " movies");


        while (!menuOption.equals("q"))
        {
            System.out.println("------------ Main Menu ----------");
            System.out.println("- search (t)itles");
            System.out.println("- search (k)eywords");
            System.out.println("- search (c)ast");
            System.out.println("- see all movies of a (g)enre");
            System.out.println("- list top 50 (r)ated movies");
            System.out.println("- list top 50 (h)igest revenue movies");
            System.out.println("- (q)uit");
            System.out.print("Enter choice: ");
            menuOption = scanner.nextLine();


            if (!menuOption.equals("q"))
            {
                processOption(menuOption);
            }
        }
    }


    private void processOption(String option)
    {
        if (option.equals("t"))
        {
            searchTitles();
        }
        else if (option.equals("c"))
        {
            searchCast();
        }
        else if (option.equals("k"))
        {
            searchKeywords();
        }
        else if (option.equals("g"))
        {
            listGenres();
        }
        else if (option.equals("r"))
        {
            listHighestRated();
        }
        else if (option.equals("h"))
        {
            listHighestRevenue();
        }
        else
        {
            System.out.println("Invalid choice!");
        }
    }


    private void searchTitles()
    {
        System.out.print("Enter a title search term: ");
        String searchTerm = scanner.nextLine();


        // prevent case sensitivity
        searchTerm = searchTerm.toLowerCase();


        // arraylist to hold search results
        ArrayList<Movie> results = new ArrayList<Movie>();


        // search through ALL movies in collection
        for (int i = 0; i < movies.size(); i++)
        {
            String movieTitle = movies.get(i).getTitle();
            movieTitle = movieTitle.toLowerCase();


            if (movieTitle.indexOf(searchTerm) != -1)
            {
                //add the Movie objest to the results list
                results.add(movies.get(i));
            }
        }


        // sort the results by title
        sortResults(results);


        // now, display them all to the user
        for (int i = 0; i < results.size(); i++)
        {
            String title = results.get(i).getTitle();


            // this will print index 0 as choice 1 in the results list; better for user!
            int choiceNum = i + 1;


            System.out.println("" + choiceNum + ". " + title);
        }


        System.out.println("Which movie would you like to learn more about?");
        System.out.print("Enter number: ");


        int choice = scanner.nextInt();
        scanner.nextLine();


        Movie selectedMovie = results.get(choice - 1);


        displayMovieInfo(selectedMovie);


        System.out.println("\n ** Press Enter to Return to Main Menu **");
        scanner.nextLine();
    }


    private void sortResults(ArrayList<Movie> listToSort)
    {
        for (int j = 1; j < listToSort.size(); j++)
        {
            Movie temp = listToSort.get(j);
            String tempTitle = temp.getTitle();


            int possibleIndex = j;
            while (possibleIndex > 0 && tempTitle.compareTo(listToSort.get(possibleIndex - 1).getTitle()) < 0)
            {
                listToSort.set(possibleIndex, listToSort.get(possibleIndex - 1));
                possibleIndex--;
            }
            listToSort.set(possibleIndex, temp);
        }
    }


    private void displayMovieInfo(Movie movie)
    {
        System.out.println();
        System.out.println("Title: " + movie.getTitle());
        System.out.println("Tagline: " + movie.getTagline());
        System.out.println("Runtime: " + movie.getRuntime() + " minutes");
        System.out.println("Year: " + movie.getYear());
        System.out.println("Directed by: " + movie.getDirector());
        System.out.println("Cast: " + movie.getCast());
        System.out.println("Overview: " + movie.getOverview());
        System.out.println("User rating: " + movie.getUserRating());
        System.out.println("Box office revenue: " + movie.getRevenue());
    }


    private void searchCast()
    {
        System.out.print("Enter a cast search term: ");
        String searchTerm = scanner.nextLine();


        // prevent case sensitivity
        searchTerm = searchTerm.toLowerCase();


        // arraylist to hold search results
        ArrayList<Movie> results = new ArrayList<Movie>();
        HashSet<String> test = new HashSet<String>();


        // search through ALL movies in collection
        for (int i = 0; i < movieCast.size(); i++)
        {
            for (int j = 0; j < movieCast.get(i).length;j++){
                String castMember = movieCast.get(i)[j];
                castMember = castMember.toLowerCase();
                if (castMember.contains(searchTerm)){
                    test.add(movieCast.get(i)[j]);
                }
            }
        }


        // sort the results by title
        ArrayList<String> list = new ArrayList<String>(test);
        Collections.sort(list);


        // now, display them all to the user
        for (int i = 0; i < list.size(); i++)
        {
            String member = list.get(i);


            // this will print index 0 as choice 1 in the results list; better for user!
            int choiceNum = i + 1;


            System.out.println("" + choiceNum + ". " + member);
        }
        System.out.println("Which cast member would you like to learn more about?");
        System.out.println("Enter number: ");

        int choice = scanner.nextInt();
        scanner.nextLine();


        String actor = list.get(choice - 1);

        for (int i = 0; i < movies.size(); i++) {
            for (int j = 0; j < movieCast.get(i).length; j++){
                String movieTitle = movies.get(i).getTitle();
                movieTitle = movieTitle.toLowerCase();


            if (movieCast.get(i)[j].contains(actor)) {
                //add the Movie object to the results list
                results.add(movies.get(i));
            }
            }
        }
        sortResults(results);
        for (int i = 0; i < results.size(); i++)
        {
            String movie = results.get(i).getTitle();


            // this will print index 0 as choice 1 in the results list; better for user!
            int choiceNum = i + 1;


            System.out.println("" + choiceNum + ". " + movie);
        }


        System.out.println("Which movie would you like to learn more about?");
        System.out.print("Enter number: ");

        choice = scanner.nextInt();
        scanner.nextLine();


        Movie selectedMovie = results.get(choice - 1);


        displayMovieInfo(selectedMovie);


        System.out.println("\n ** Press Enter to Return to Main Menu **");
        scanner.nextLine();
    }


    private void searchKeywords()
    {
        System.out.print("Enter a keyword search term: ");
        String searchTerm = scanner.nextLine();


        // prevent case sensitivity
        searchTerm = searchTerm.toLowerCase();


        // arraylist to hold search results
        ArrayList<Movie> results = new ArrayList<Movie>();


        // search through ALL movies in collection
        for (int i = 0; i < movies.size(); i++)
        {
            String keyword = movies.get(i).getKeywords();
            keyword = keyword.toLowerCase();


            if (keyword.indexOf(searchTerm) != -1)
            {
                //add the Movie objest to the results list
                results.add(movies.get(i));
            }
        }


        // sort the results by title
        sortResults(results);


        // now, display them all to the user
        for (int i = 0; i < results.size(); i++)
        {
            String title = results.get(i).getTitle();


            // this will print index 0 as choice 1 in the results list; better for user!
            int choiceNum = i + 1;


            System.out.println("" + choiceNum + ". " + title);
        }


        System.out.println("Which movie would you like to learn more about?");
        System.out.print("Enter number: ");


        int choice = scanner.nextInt();
        scanner.nextLine();


        Movie selectedMovie = results.get(choice - 1);


        displayMovieInfo(selectedMovie);


        System.out.println("\n ** Press Enter to Return to Main Menu **");
        scanner.nextLine();
    }


    private void listGenres()
    {

        // arraylist to hold search results
        ArrayList<Movie> results = new ArrayList<Movie>();
        HashSet<String> test = new HashSet<String>();


        // search through ALL movies in collection
        for (int i = 0; i < movieGenre.size(); i++)
        {
            for (int j = 0; j < movieGenre.get(i).length;j++){
                test.add(movieGenre.get(i)[j]);
            }
        }


        // sort the results by title
        ArrayList<String> list = new ArrayList<String>(test);
        Collections.sort(list);


        // now, display them all to the user
        for (int i = 0; i < list.size(); i++)
        {
            String genre = list.get(i);


            // this will print index 0 as choice 1 in the results list; better for user!
            int choiceNum = i + 1;


            System.out.println("" + choiceNum + ". " + genre);
        }
        System.out.println("Which genre would you like to learn more about?");
        System.out.println("Enter number: ");

        int choice = scanner.nextInt();
        scanner.nextLine();


        String genres = list.get(choice - 1);

        for (int i = 0; i < movies.size(); i++) {
            for (int j = 0; j < movieGenre.get(i).length; j++){
                if (movieGenre.get(i)[j].contains(genres)) {
                    //add the Movie object to the results list
                    results.add(movies.get(i));
                }
            }
        }

        sortResults(results);

        for (int i = 0; i < results.size(); i++)
        {
            String movie = results.get(i).getTitle();


            // this will print index 0 as choice 1 in the results list; better for user!
            int choiceNum = i + 1;


            System.out.println("" + choiceNum + ". " + movie);
        }


        System.out.println("Which movie would you like to learn more about?");
        System.out.print("Enter number: ");

        choice = scanner.nextInt();
        scanner.nextLine();


        Movie selectedMovie = results.get(choice - 1);


        displayMovieInfo(selectedMovie);


        System.out.println("\n ** Press Enter to Return to Main Menu **");
        scanner.nextLine();

    }


    private void listHighestRated()
    {
        for (int i = 0; i < 50; i++) {
            System.out.println(i+1 + ". " + topMoviesRate.get(i).getTitle() + ": " + topMovies.get(i).getUserRating());
        }
        System.out.println("Which movie would you like to learn more about?");
        System.out.print("Enter number: ");

        int choice = scanner.nextInt();
        scanner.nextLine();


        Movie selectedMovie = topMoviesRate.get(choice - 1);


        displayMovieInfo(selectedMovie);
    }


    private void listHighestRevenue()
    {
        for (int i = 0; i < 50; i++) {
            System.out.println(i+1 + ". " + topMoviesRev.get(i).getTitle() + ": " + topMovies.get(i).getUserRating());
        }
        System.out.println("Which movie would you like to learn more about?");
        System.out.print("Enter number: ");

        int choice = scanner.nextInt();
        scanner.nextLine();


        Movie selectedMovie = topMoviesRev.get(choice - 1);


        displayMovieInfo(selectedMovie);

    }


    private void importMovieList(String fileName)
    {
        try
        {
            FileReader fileReader = new FileReader(fileName);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String line = bufferedReader.readLine();


            movies = new ArrayList<Movie>();


            while ((line = bufferedReader.readLine()) != null)
            {
                String[] movieFromCSV = line.split(",");


                String title = movieFromCSV[0];
                String cast = movieFromCSV[1];
                String director = movieFromCSV[2];
                String tagline = movieFromCSV[3];
                String keywords = movieFromCSV[4];
                String overview = movieFromCSV[5];
                int runtime = Integer.parseInt(movieFromCSV[6]);
                String genres = movieFromCSV[7];
                double userRating = Double.parseDouble(movieFromCSV[8]);
                int year = Integer.parseInt(movieFromCSV[9]);
                int revenue = Integer.parseInt(movieFromCSV[10]);


                Movie nextMovie = new Movie(title, cast, director, tagline, keywords, overview, runtime, genres, userRating, year, revenue);
                movies.add(nextMovie);
            }
            bufferedReader.close();
        }
        catch(IOException exception)
        {
            // Print out the exception that occurred
            System.out.println("Unable to access " + exception.getMessage());
        }
    }
}

