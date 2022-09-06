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
      
      for(int i=0; i<size1; i++)
      {
         final String recipeContent = scraper.getRecipeContent(recipes.get(i).get(0));
         recipes.get(i).add(scraper.extractPath(recipeContent));
         recipes.get(i).add(scraper.extractTitle(recipeContent));
         //recipes.get(i).add(scraper.extractPath(recipeContent));
         recipes.get(i).add(scraper.extractAuthor(recipeContent));
         recipes.get(i).add(scraper.extractServings(recipeContent));
         recipes.get(i).add(scraper.extractIngredients(recipeContent));
         
      }
        
      System.out.println(recipes.get(0).get(1));
    //  System.out.println(recipes.get(0).get(2));
     // System.out.println(recipes.get(0).get(3));
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

      private String extractRecipeUrl(String content) 
      {
         final Pattern recipeUrlRegExp = Pattern.compile("<a class=\"thumb-link\" href=\"(.*?)\" title", Pattern.DOTALL);
         final Matcher matcher = recipeUrlRegExp.matcher(content);
         matcher.find(startx);
         startx = matcher.start()+20;
         
         return matcher.group(1);
      }
      
      private String getRecipeContent(String recipe) throws IOException
      {
         try
         {
           HttpClient client = HttpClient.newHttpClient();
           String urlToScrape = recipe;
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
      
    //extract the author for each recipe
    private String extractAuthor(String content) 
    {
      //retrieve author 
      final Pattern authorRegExp = Pattern.compile("<div class=\"recipe-author\">\n(.*?)\n</div>", Pattern.DOTALL);
      final Matcher matcher = authorRegExp.matcher(content);
      matcher.find();
      return matcher.group(1);
    }
    
    private String extractTitle(String content)
    {
      //retrieve Title 
      final Pattern authorRegExp = Pattern.compile("<title>(.*?)</title>", Pattern.DOTALL);
      final Matcher matcher = authorRegExp.matcher(content);
      matcher.find();
      return matcher.group(1);
    }
    //extract the servings for each recipe 
    private String extractIngredients(String content) 
    {
      final Pattern servingsRegExp = Pattern.compile("<div class=\"recipe-details-ingredients\">\n(.*?)\n</div>", Pattern.DOTALL);
      final Matcher matcher = servingsRegExp.matcher(content);
      matcher.find();
      return matcher.group(1);
    }
    
    //retreive Procedure
    private String extractProcedure(String content)
    {
      final Pattern servingsRegExp = Pattern.compile("<div class=\"recipe-details-procedure\">\n(.*?)\n</div>", Pattern.DOTALL);
      final Matcher matcher = servingsRegExp.matcher(content);
      matcher.find();
      return matcher.group(1);
    }
    
    //retreieve Serving Size
    private String extractServings(String content)
    {
      final Pattern servingsRegExp = Pattern.compile("<div class=\"recipe-details-serves\">\n(.*?)\n</div>", Pattern.DOTALL);
      final Matcher matcher = servingsRegExp.matcher(content);
      matcher.find();
      return matcher.group(1);
    }
    
   /* private String extractPath(String content)
    {
      final Pattern servingsRegExp = Pattern.compile("\"primaryCategory\":\"(.*?)\",\"subCategory1\"", Pattern.DOTALL);
      final Matcher matcher = servingsRegExp.matcher(content);
      matcher.find();
      String path = "Home/Recipes/" +matcher.group(1) + "/";
      //servingsRegExp = Pattern.compile("\"primaryCategory\":\"(.*?)\"", Pattern.DOTALL);

      return path;
    }*/

      
      
    
}
