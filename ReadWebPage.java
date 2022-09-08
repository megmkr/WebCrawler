/*
   WARMUP ASSIGNMENT
   ASHLEY KIM
   JANELLE BERNAL
   MEGAN MKRTCHYAN
*/
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
import java.util.*;
import java.lang.String;
import java.lang.Thread;

public class ReadWebPage 
{
   //variable to keep track of where scraping should start on main recipe page
   public static int startx = 0;
   //string for main page url
   public static String inputPageUrl;
   
   //exporting to csv, delimiter, separator
   private static final String DELIMITER = "\t";
   private static final String SEPARATOR = "\n";
    
   //File header
   private static final String HEADER = "Title\tPath\tAuthor\tServings\tIngredients\tProcedure";
       
   public static void main(String[] args) throws IOException, InterruptedException {
      //2d arraylist for recipe list and arraylist for the 4 main recipe pages
      ArrayList<ArrayList<String>> recipes = new ArrayList<ArrayList<String>>();
      ArrayList<String> pageUrls = new ArrayList<String>();

      //fill the main page arraylist
      pageUrls.add("https://www.surlatable.com/recipes/?srule=best-matches&start=0&sz=24");
      pageUrls.add("https://www.surlatable.com/recipes/?srule=best-matches&start=24&sz=24");
      pageUrls.add("https://www.surlatable.com/recipes/?srule=best-matches&start=48&sz=24");
      pageUrls.add("https://www.surlatable.com/recipes/?srule=best-matches&start=72&sz=24");
      
      
      //amount of recipes to scan per page
      int totalRecipes = 24;
      //what recipe we are currently copying to 2d arraylist
      int recipeNumber=0;
      
      //fill 2d arraylist
      for(int a=0; a<96; a++)
      {
         //arraylist to hold 96 recipes to hold components
         ArrayList<String> filler = new ArrayList<String>(7);
         //add to recipes arraylist
         recipes.add(filler);
      }
      
      //create scraper to parse web page
      final ReadWebPage scraper = new ReadWebPage();
        
        //loop through main 4 url pages - 24 recipes each
        for(int i=0; i<pageUrls.size(); i++)
        {
          //set inputPageUrl to the website we currently want to scrap
          String inputPageUrl = pageUrls.get(i);
          //put the html content of page into string
          String htmlContent = scraper.getContent(inputPageUrl);
          
          //for loop to keep track of when to go to next page
          for(; recipeNumber<totalRecipes; recipeNumber++)
          {
            //when the RN reaches last recipe on page, reset startx to avoid IllegalStateException
            if(recipeNumber==24 || recipeNumber== 48 || recipeNumber==72)
               startx=0; 
            //get first arraylist and then add recipe url to the filler arraylist 
            recipes.get(recipeNumber).add(scraper.extractRecipeUrl(htmlContent));
            //html string of current webpage 
            final String recipeContent = scraper.getRecipeContent(recipes.get(recipeNumber).get(0));
            //add title to recipes
            recipes.get(recipeNumber).add(scraper.extractTitle(recipeContent));
            //add path to recipes
            recipes.get(recipeNumber).add(scraper.extractPath(recipeContent, 0));
            //add author to recipes
            recipes.get(recipeNumber).add(scraper.extractAuthor(recipeContent));
            //add serving amounts to recipes
            recipes.get(recipeNumber).add(scraper.extractServings(recipeContent));
            //add ingreidents to recipes
            recipes.get(recipeNumber).add(scraper.extractIngredients(recipeContent));
            //add procedure to recipes
            recipes.get(recipeNumber).add(scraper.extractProcedure(recipeContent));

            //sleep method to avoid interruption exception
            Thread.sleep(10000);
          }
          //add additional page of recipes to keep the parsing to next web page
         totalRecipes+=24;
        }
      
      //declare fw
      FileWriter file = null;
      
      try
      {
        //create file - CSV
        file = new FileWriter("RecipeList.csv");
        //Add header
        file.append(HEADER);
        //Add a new line after the header
        file.append(SEPARATOR);
        //string to hold added components
        String toAdd = "";
        //loop through each recipe
        for(int a=0; a<96; a++)
        {
         //loop through each recipe component
         for(int b=1; b<7; b++){
            //Store all data into toAdd to take out html and werid char
            toAdd = recipes.get(a).get(b);
            
            //find and replace all patterns
            toAdd = toAdd.replaceAll("<li>","");
            toAdd = toAdd.replaceAll("</li>","");
            toAdd = toAdd.replaceAll("<br>","");
            toAdd = toAdd.replaceAll("<b>","");
            toAdd = toAdd.replaceAll("</b>","");
            toAdd = toAdd.replaceAll("</ul>","");
            toAdd = toAdd.replaceAll("<ul>","");
            toAdd = toAdd.replaceAll("<i>","");
            toAdd = toAdd.replaceAll("</i>","");
            toAdd = toAdd.replaceAll("&#188;","¼");
            toAdd = toAdd.replaceAll("&#189;","½");
            toAdd = toAdd.replaceAll("&#8539;","⅛");
            toAdd = toAdd.replaceAll("&#176;","°");
            toAdd = toAdd.replaceAll("&amp;","&");
            toAdd = toAdd.replaceAll("&rsquo;","'");
            toAdd = toAdd.replaceAll("<span style=\"font-size:12pt\">"," ");
            toAdd = toAdd.replaceAll("<span style=\"font-size:12.0pt\">"," ");
            toAdd = toAdd.replaceAll("&nbsp"," ");
            toAdd = toAdd.replaceAll("<span style=\"color:#000000\">"," ");
            toAdd = toAdd.replaceAll("<strong>"," ");
            toAdd = toAdd.replaceAll("&#8217;","'");
            toAdd = toAdd.replaceAll("&#190;","¾");
            toAdd = toAdd.replaceAll("&#8531;","⅓");
            toAdd = toAdd.replaceAll("<br />"," ");           
            toAdd = toAdd.replaceAll("</strong>"," ");
            toAdd = toAdd.replaceAll("</span>"," ");
            toAdd = toAdd.replaceAll("</em>"," ");        
            toAdd = toAdd.replaceAll("<em>"," ");
            toAdd = toAdd.replaceAll("<s>"," ");
            toAdd = toAdd.replaceAll("</s>"," ");        
            toAdd = toAdd.replaceAll("&#233;","e");
            toAdd = toAdd.replaceAll("&#8212;","-");    
            toAdd = toAdd.replaceAll("&deg;","°");
            toAdd = toAdd.replaceAll("<sup>"," ");            
            toAdd = toAdd.replaceAll("<sub>"," ");
            toAdd = toAdd.replaceAll("</sub>"," ");
            toAdd = toAdd.replaceAll("&frasl;","⁄");
            toAdd = toAdd.replaceAll("</sup>"," ");
           
            //add swifted data to file
            file.append(toAdd);
            //add coma
            file.append(DELIMITER);
         } 
            //new line
            file.append(SEPARATOR);            
        }
        //close file
        file.close();
   
      }
      //catch exception
      catch(Exception e)
      {
        e.printStackTrace();
      }
   }
   
   //function to get content from website     
   private String getContent(String url) throws IOException 
   { 
      try
      {
         //create client
         HttpClient client = HttpClient.newHttpClient();
         //set our desired website to a string to plug in
         String urlToScrape = url;
         //create request to parse through set url
         HttpRequest request =  HttpRequest.newBuilder().uri(URI.create(urlToScrape)).build();
         //send request to stream html content of webpage
         HttpResponse<String> response = client.send(request,HttpResponse.BodyHandlers.ofString());
         //return the html code
         return response.body();
      }
      //catch exception
      catch (InterruptedException e)
      {
         System.out.println(e);
      }
      //else return null
      return null;   
   }

   //get url  - take in string
   private String extractRecipeUrl(String content) 
   {
      //create pattern to pass in reggex 
      final Pattern recipeUrlRegExp = Pattern.compile("<a class=\"thumb-link\" href=\"(.*?)\" title", Pattern.DOTALL);
      //sets pattern to match with html content
      final Matcher matcher = recipeUrlRegExp.matcher(content);
      //find the next at specified index of x
      matcher.find(startx);
      //execute
      startx = matcher.start()+20;
      //return the match
      return matcher.group(1);
   }
   
   //get recipe url
   private String getRecipeContent(String recipe) throws IOException
   {
      try
      {
        //create client
        HttpClient client = HttpClient.newHttpClient();
        //string of url to recipe
        String urlToScrape = recipe;
        //create request to parse through the page
        HttpRequest request =  HttpRequest.newBuilder().uri(URI.create(urlToScrape)).build();
        //send request in response
        HttpResponse<String> response = client.send(request,HttpResponse.BodyHandlers.ofString());
        //return code
        return response.body();      
      }
      //catch IE
      catch (InterruptedException e)
      {
        System.out.println(e);
      }
      //else return null
      return null;   
   }
      
    //get title
    private String extractTitle(String content)
    {
      //find this pattern within the html code for the actual recipe  
      final Pattern authorRegExp = Pattern.compile("<title>(.*?)</title>", Pattern.DOTALL);
      //create matcher to compile against pattern request
      final Matcher matcher = authorRegExp.matcher(content);
      try
      {
         //find the macthes
         matcher.find();
         //return while found
         return matcher.group(1);
      }
      //catch exception - if recipe doesn't have a title
      catch(IllegalStateException ISE)
      {
         return "";
      }
    }
    
   //extract the author for each recipe
   private String extractAuthor(String content) 
   {
      //locate pattern in recipe html 
      final Pattern authorRegExp = Pattern.compile("<div class=\"recipe-author\">\n(.*?)\n</div>", Pattern.DOTALL);
      //create matcher to compile pattern vs html
      final Matcher matcher = authorRegExp.matcher(content);
      try
      {
         //find matches
         matcher.find();
         //return matches while found
         return matcher.group(1);
      }
      //catch IE - if there's no author
      catch(IllegalStateException ISE)
      {
         return "";
      }
    }
    
    //extract the ingredients for each recipe 
    private String extractIngredients(String content) 
    {
      //string to remove random lines
      String removeLines; 
      //pattern to find in html
      final Pattern servingsRegExp = Pattern.compile("<div class=\"recipe-details-ingredients\">(.*?)\n</div>", Pattern.DOTALL);
      //create matcher of pattern vs content
      final Matcher matcher = servingsRegExp.matcher(content);
      try
      {  
         //find matches
         matcher.find();
         //return found ingredients into string
         removeLines = matcher.group(1);
         //find all empty lines and remove
         removeLines = removeLines.replaceAll("\r", "").replaceAll("\n", "");
         //return string
         return removeLines;
      }
      //catche ISE - in case there's no ingredients
      catch(IllegalStateException ISE)
      {
         return "";
      }
    }
    
    //retreive Procedure
    private String extractProcedure(String content)
    {
      //string to hold data
      String removeLines2; 
      //pattern we want in order to get procdures
      final Pattern servingsRegExp = Pattern.compile("<div class=\"recipe-details-procedure\">\n(.*?)\n</div>", Pattern.DOTALL);
      //create matcher to compare pattern to content
      final Matcher matcher = servingsRegExp.matcher(content);
      
      try
      {
         //find matches
         matcher.find();
         //add resulsts to string
         removeLines2 = matcher.group(1);
         //remove all extra space from data
         removeLines2 = removeLines2.replaceAll("\r", "").replaceAll("\n", "");
         //return data
         return removeLines2;
      }
      //catch ISE - in case there are no proceudres
      catch(IllegalStateException ISE)
      {
         return "";
      }
    }
    
    //retreieve Serving Size
    private String extractServings(String content)
    {
      //locate pattern for servings
      final Pattern servingsRegExp = Pattern.compile("<div class=\"recipe-details-serves\">\n(.*?)\n", Pattern.DOTALL);
      //create matcher for pattern vs content
      final Matcher matcher = servingsRegExp.matcher(content);
      try
      {
         //find matches
         matcher.find();
         //return servings
         return matcher.group(1);
      }
      //catch ISE - in case there are no servings
      catch(IllegalStateException ISE)
      {
         return "";
      }
    }
    
    //extract path
    private String extractPath(String content, int startz)
    {
      try
      {
         //pattern for path
         Pattern pathRegExp = Pattern.compile("<a class=\"breadcrumb-element\" href=\".*?\" title=\".*?\">(.*?)</a>", Pattern.DOTALL);
         //strings for additional paths after home/recipes/...
         String group1 = "";
         String group2 = "";
         String path = "";
         //create matcher
         Matcher matcher = pathRegExp.matcher(content);
         
         //loop to get each pathway
         for(int i=0; i<4; i++)
         {
            //find wanted content
            matcher.find(startz);
            //add to string with slash
            group1 += matcher.group(1) + "/";
            //move on to next link
            startz = matcher.start()+2;
         }
         //add to string
         path += group1;
         //resetting values for next path
         pathRegExp = Pattern.compile("<span class=\"breadcrumb-element\">(.*?)</span>", Pattern.DOTALL);
         matcher = pathRegExp.matcher(content);
         matcher.find();
         //find title of next path and add to string
         group2 = matcher.group(1);
         //add string to pathway string
         path += group2 ;  
         //return   
         return path;
      }
      //catch ISE - in case there is no path
      catch(IllegalStateException ISE)
      {
         return "";
      }
    }
   
      
      
    
}
