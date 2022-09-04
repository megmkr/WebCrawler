import java.io.IOException;
import java.util.Scanner;
import java.net.*;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.*;
import java.util.ArrayList;

public class ReadWebPage 
{
   public static int startx = 0;
    public static void main(String[] args) throws IOException, InterruptedException 
    {
        
        ArrayList<ArrayList<String>> recipes = new ArrayList<ArrayList<String>>();
        
        int size1 = 3;
        int size2 = 7;
        
        for(int a=0; a<size1; a++)
        {
           ArrayList<String> filler = new ArrayList<String>(7);
           recipes.add(filler);
        }
        
        final ReadWebPage scraper = new ReadWebPage();
        final String htmlContent = scraper.getContent();

        for(int i=0; i<size1; i++)
        {
           recipes.get(i).add(scraper.extractRecipeUrl(htmlContent));
        }
        
       System.out.println(recipes.get(0).get(0));
       System.out.println(recipes.get(1).get(0));
       System.out.println(recipes.get(2).get(0));
    }
        
   
    
    private String getContent() throws IOException 
    {
      try
      {
            HttpClient client = HttpClient.newHttpClient();
            String urlToScrape = "https://www.surlatable.com/recipes/?srule=best-matches&start=0&sz=24";
            HttpRequest request =  HttpRequest.newBuilder().uri(URI.create(urlToScrape)).build();
            HttpResponse<String> response = client.send(request,HttpResponse.BodyHandlers.ofString());
           return response.body();

      
      }
      catch (InterruptedException e)
     {
         System.out.println(e);
     }
     return null;   
    }

    private String extractRecipeUrl(String content) {
        final Pattern recipeUrlRegExp = Pattern.compile("<a class=\"thumb-link\" href=\"(.*?)\" title", Pattern.DOTALL);
        final Matcher matcher = recipeUrlRegExp.matcher(content);
        matcher.find(startx);
        startx = matcher.start()+20;

        return matcher.group(1);
    }
}