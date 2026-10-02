import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

class Stock {
    private String symbol;
    private String name;
    private double price;

    public Stock(String symbol, String name, double price) {
        this.symbol = symbol;
        this.name = name;
        this.price = price;
    }

    public String getSymbol() { return symbol; }
    public String getName() { return name; }
    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }
}

class Portfolio {
    private double balance;
    private Map<String, Integer> holdings;

    public Portfolio(double initialBalance) {
        this.balance = initialBalance;
        this.holdings = new HashMap<>();
    }

    public double getBalance() { return balance; }

    public void buyStock(Stock stock, int quantity) {
        double totalCost = stock.getPrice() * quantity;
        if (totalCost > balance) {
            System.out.println("Insufficient funds! Required: $" + totalCost + ", Available: $" + balance);
        } else {
            balance -= totalCost;
            holdings.put(stock.getSymbol(), holdings.getOrDefault(stock.getSymbol(), 0) + quantity);
            System.out.println("Successfully bought " + quantity + " shares of " + stock.getSymbol());
        }
    }

    public void sellStock(Stock stock, int quantity) {
        String symbol = stock.getSymbol();
        int currentHoldings = holdings.getOrDefault(symbol, 0);

        if (quantity > currentHoldings) {
            System.out.println("You don't own enough shares! You currently have: " + currentHoldings);
        } else {
            double earnings = stock.getPrice() * quantity;
            balance += earnings;
            if (quantity == currentHoldings) {
                holdings.remove(symbol);
            } else {
                holdings.put(symbol, currentHoldings - quantity);
            }
            System.out.println("Successfully sold " + quantity + " shares of " + symbol);
        }
    }

    public void displayPortfolio(Map<String, Stock> marketData) {
        System.out.println("\n--- YOUR PORTFOLIO ---");
        System.out.printf("Cash Balance: $%.2f\n", balance);
        if (holdings.isEmpty()) {
            System.out.println("No stocks owned yet.");
        } else {
            System.out.println("Owned Stocks:");
            double totalValue = balance;
            for (Map.Entry<String, Integer> entry : holdings.entrySet()) {
                String symbol = entry.getKey();
                int qty = entry.getValue();
                Stock stock = marketData.get(symbol);
                double currentValue = stock.getPrice() * qty;
                totalValue += currentValue;
                System.out.printf("- %s (%s): %d shares @ $%.2f each | Total Value: $%.2f\n",
                        symbol, stock.getName(), qty, stock.getPrice(), currentValue);
            }
            System.out.printf("Total Portfolio Value: $%.2f\n", totalValue);
        }
    }
}

public class Main {
    private static Map<String, Stock> market = new HashMap<>();

    public static void main(String[] args) {
        initializeMarket();
        Portfolio myPortfolio = new Portfolio(10000.0); // Starting with $10,000 cash
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        System.out.println("=========================================");
        System.out.println("     SIMULATED STOCK TRADING PLATFORM    ");
        System.out.println("=========================================");

        while (running) {
            System.out.println("\n--- MAIN MENU ---");
            System.out.println("1. View Market Data (Stock Prices)");
            System.out.println("2. Buy Stocks");
            System.out.println("3. Sell Stocks");
            System.out.println("4. View Portfolio & Balance");
            System.out.println("5. Exit");
            System.out.print("Choose an option (1-5): ");

            int choice;
            try {
                choice = Integer.parseInt(scanner.nextLine());
            } catch (Exception e) {
                System.out.println("Invalid input! Please enter a number.");
                continue;
            }

            switch (choice) {
                case 1:
                    displayMarket();
                    break;
                case 2:
                    displayMarket();
                    System.out.print("Enter Stock Symbol to Buy (e.g. AAPL): ");
                    String buySymbol = scanner.nextLine().toUpperCase();
                    if (market.containsKey(buySymbol)) {
                        System.out.print("Enter quantity to buy: ");
                        try {
                            int buyQty = Integer.parseInt(scanner.nextLine());
                            if (buyQty > 0) {
                                myPortfolio.buyStock(market.get(buySymbol), buyQty);
                            } else {
                                System.out.println("Quantity must be greater than 0.");
                            }
                        } catch (Exception e) {
                            System.out.println("Invalid quantity!");
                        }
                    } else {
                        System.out.println("Stock symbol not found!");
                    }
                    break;
                case 3:
                    myPortfolio.displayPortfolio(market);
                    System.out.print("Enter Stock Symbol to Sell: ");
                    String sellSymbol = scanner.nextLine().toUpperCase();
                    if (market.containsKey(sellSymbol)) {
                        System.out.print("Enter quantity to sell: ");
                        try {
                            int sellQty = Integer.parseInt(scanner.nextLine());
                            if (sellQty > 0) {
                                myPortfolio.sellStock(market.get(sellSymbol), sellQty);
                            } else {
                                System.out.println("Quantity must be greater than 0.");
                            }
                        } catch (Exception e) {
                            System.out.println("Invalid quantity!");
                        }
                    } else {
                        System.out.println("Stock symbol not found!");
                    }
                    break;
                case 4:
                    myPortfolio.displayPortfolio(market);
                    break;
                case 5:
                    running = false;
                    System.out.println("\nThank you for using Stock Trading Platform!");
                    break;
                default:
                    System.out.println("Invalid choice. Please select from 1 to 5.");
            }
        }
        scanner.close();
    }

    private static void initializeMarket() {
        market.put("AAPL", new Stock("AAPL", "Apple Inc.", 180.50));
        market.put("GOOGL", new Stock("GOOGL", "Alphabet Inc.", 140.25));
        market.put("MSFT", new Stock("MSFT", "Microsoft Corp.", 400.10));
        market.put("AMZN", new Stock("AMZN", "Amazon.com Inc.", 175.80));
        market.put("TSLA", new Stock("TSLA", "Tesla Inc.", 200.00));
    }

    private static void displayMarket() {
        System.out.println("\n--- MARKET DATA ---");
        for (Stock stock : market.values()) {
            System.out.printf("Symbol: %-6s | Name: %-18s | Price: $%.2f\n",
                    stock.getSymbol(), stock.getName(), stock.getPrice());
        }
    }
}