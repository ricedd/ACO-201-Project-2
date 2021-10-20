import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

class transaction {

  public int sharesPrice;
  public int numShares;

  public transaction(String parse) {
    // expected format: "buy 100 share(s) at $20 each"   <- this is the input    Do i just replace input with the whole string
    // 1.get number of shares-^ ^
    // 2.get price of shares----------------- ^
    //lots of ways to do this:
    if(parse != ""){
      parse = parse.replace("$", "");
      String[] splitter = parse.split(" "); //space between each word, every input is the same
      numShares = Integer.parseInt(splitter[1]); // num shares always going to be 2nd element of this array
      sharesPrice = Integer.parseInt(splitter[4]);
    } 
    //there we go, can you
    //output is wrong since we havent se the shares price yet, I'll let you do that
  }
}

class Main {
  // the "buy" stocks
  static Queue<transaction> boughtStocks = new LinkedList<transaction>();
  // the "Sell" of stocks
  static Queue<transaction> sellTransactions = new LinkedList<transaction>();

  public static void main(final String[] args) {
    Queue<transaction> allTransactions = new LinkedList<transaction>();
    Scanner input = new Scanner(System.in);
    //allTransactions = parseInput(input.nextLine()); //uncomment in official
    // project

    // Next 2 lines are for testing only, delete in official project
    String tempInput = "buy 100 share(s) at $20 each;buy 20 share(s) at $24 each;buy 200 share(s) at $36 each;sell 150 share(s) at $30 each;buy 50 share(s) at $25 each;sell 200 share(s) at $35 each;";
    // expected output "1070" (without quotes)
    parseInput(tempInput);
    processTransactions();
    System.out.println("Expected output is $1070");
  }
  private static void processTransactions() {
    transaction sell = new transaction("");
    transaction bought = new transaction("");
    
    int capitalGain = 0;
    // iterate through all transactions and output money
    // To do:
    // go through queue (FIFO) and apply it to a captial gain
    // ex: if you buy 100 stocks at $20 and you sell 10 of them at $30, the capital gain is: 10 * ($30-$20)=$100. 
    while(true){
      //this method is ran until the conditional statements break it
      //check if we need to poll a new sell transactions (and can)
      if(sell.numShares == 0 && sellTransactions.peek() != null)
        sell = sellTransactions.poll();
      //check if we have process all current sell transactions and iterated all sell transactions, if so finish and display
      else if(sell.numShares == 0 && sellTransactions.peek() == null)
        break;

      //check if we need to poll a new bought transactions (and can)
      if(bought.numShares == 0 && boughtStocks.peek() != null)
        bought = boughtStocks.poll();
      //check if we have finished current bought transaction and iterated through all bought transactions, if so finish and display
      else if(bought.numShares == 0 && boughtStocks.peek() == null)
        break;

      //check delta of both transactions
      int stockDiff = bought.numShares - sell.numShares;
      if(stockDiff <= 0){
        //when <= 0 all bought stocks are done
        //calculate captial gain, based off sold stocks and price delta
        capitalGain += bought.numShares * (sell.sharesPrice - bought.sharesPrice);
        //set bought stock number to 0 (this will fetch new on on next pass)
        bought.numShares = 0;
        //set sell stocks number to whats left (if 0 a new one will be polled on next pass)
        sell.numShares = -1* stockDiff; //-1 is because we have a negative number and need it positive
        //we can technically just do sell.numShares -= bought.numShares if we do it befor we set to 0, either way works
      } 
      else{
        //same thing, but this time we exhausted all the sold stocks for this transactions instead of the bought stocks.
        capitalGain += (sell.numShares) * (sell.sharesPrice - bought.sharesPrice);
        //since we still have bought stocks, we need to minus it from sold stocks
        bought.numShares -= sell.numShares;
        sell.numShares = 0; //we exhausted this sell transaction, set to 0 so next pass grabs a new one
      }
      System.out.println("current: $" + capitalGain); //Debug look at running total
    }    
    System.out.println("" + capitalGain);
  }
  private static void parseInput(String input) {
    // Take in input and add to temp transactions

    // To do:
    // 1.Seperate the transactions in the input string
    // ex transactions: "buy 100 share(s) at $20 each;"
    // all of the transactions are seperated by ";"
    // 2.add(offer) the seperated to right queue
    // Wants a transaction object temp.offer(new transaction("transaction string"));
    //  3.return the filled queue
    if(input.length() == 0){
      System.out.println("input is empty: " + input.length());
    }
    if (input.contains(";")) {
      //we use the string split method to seperate all of the operations into an array of each operation by itself
      //all transactions are seperated by a ";"
      //the contructor of transaction() needs to take in a string with this format "buy X share(s) at $Y each;"
      //  with x being number of shares and y price of that transactio
      String[] splitter = input.split(";"); //split use ", " need the space or it wil mess up operations
      for(String op : splitter){
        //figure out if its a buy or a sell transactions and add to right queue (FIFO)
        if(op.contains("buy")){
          boughtStocks.offer(new transaction(op));
          System.out.println("Stocks Bought"); //debug
        }
        else if(op.contains("sell"))
          sellTransactions.offer(new transaction(op));
          System.out.println("Stocks Sold"); //debug
                
        }
      }
  }

}