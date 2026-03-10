import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

public class StockTradingPlatform extends JFrame {
    // Data Storage (OOP based)
    private double accountBalance = 10000.00; // Starting with $10,000
    private HashMap<String, Double> marketStocks = new HashMap<>();
    private HashMap<String, Integer> userPortfolio = new HashMap<>();

    // GUI Components
    private JTextArea marketArea;
    private JTextArea portfolioArea;
    private JLabel balanceLabel;
    private JTextField symbolField;
    private JTextField quantityField;

    public StockTradingPlatform() {
        // 1. Initialize Market Data
        marketStocks.put("TCS", 3500.0);
        marketStocks.put("RELIANCE", 2400.0);
        marketStocks.put("INFY", 1500.0);
        marketStocks.put("HDFC", 1600.0);

        // 2. Setup Main Window
        setTitle("Stock Trading Platform - CodeAlpha");
        setSize(650, 450);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // 3. Top Panel (Balance)
        JPanel topPanel = new JPanel();
        balanceLabel = new JLabel("Account Balance: $" + String.format("%.2f", accountBalance));
        balanceLabel.setFont(new Font("Arial", Font.BOLD, 16));
        topPanel.add(balanceLabel);
        add(topPanel, BorderLayout.NORTH);

        // 4. Center Panel (Market & Portfolio Display)
        JPanel centerPanel = new JPanel(new GridLayout(1, 2, 10, 10));
        
        marketArea = new JTextArea();
        marketArea.setEditable(false);
        marketArea.setBorder(BorderFactory.createTitledBorder("Market Data"));
        
        portfolioArea = new JTextArea();
        portfolioArea.setEditable(false);
        portfolioArea.setBorder(BorderFactory.createTitledBorder("Your Portfolio"));
        
        centerPanel.add(new JScrollPane(marketArea));
        centerPanel.add(new JScrollPane(portfolioArea));
        add(centerPanel, BorderLayout.CENTER);

        // 5. Bottom Panel (Buy/Sell Controls)
        JPanel bottomPanel = new JPanel(new FlowLayout());
        
        bottomPanel.add(new JLabel("Stock Symbol:"));
        symbolField = new JTextField(7);
        bottomPanel.add(symbolField);
        
        bottomPanel.add(new JLabel("Quantity:"));
        quantityField = new JTextField(5);
        bottomPanel.add(quantityField);
        
        JButton buyButton = new JButton("Buy");
        JButton sellButton = new JButton("Sell");
        
        bottomPanel.add(buyButton);
        bottomPanel.add(sellButton);
        add(bottomPanel, BorderLayout.SOUTH);

        // 6. Button Actions
        buyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                processTrade(true);
            }
        });

        sellButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                processTrade(false);
            }
        });

        // Initial Display Update
        updateDisplays();
    }

    // Method to handle Buy/Sell logic
    private void processTrade(boolean isBuy) {
        String symbol = symbolField.getText().toUpperCase();
        String qtyText = quantityField.getText();

        if (!marketStocks.containsKey(symbol)) {
            JOptionPane.showMessageDialog(this, "Invalid Stock Symbol! Try TCS, RELIANCE, INFY, or HDFC.");
            return;
        }

        try {
            int quantity = Integer.parseInt(qtyText);
            if (quantity <= 0) throw new NumberFormatException();

            double price = marketStocks.get(symbol);
            double totalCost = price * quantity;

            if (isBuy) {
                if (accountBalance >= totalCost) {
                    accountBalance -= totalCost;
                    userPortfolio.put(symbol, userPortfolio.getOrDefault(symbol, 0) + quantity);
                    JOptionPane.showMessageDialog(this, "Successfully bought " + quantity + " shares of " + symbol);
                } else {
                    JOptionPane.showMessageDialog(this, "Insufficient Balance!");
                }
            } else {
                int currentOwned = userPortfolio.getOrDefault(symbol, 0);
                if (currentOwned >= quantity) {
                    accountBalance += totalCost;
                    userPortfolio.put(symbol, currentOwned - quantity);
                    if (userPortfolio.get(symbol) == 0) {
                        userPortfolio.remove(symbol);
                    }
                    JOptionPane.showMessageDialog(this, "Successfully sold " + quantity + " shares of " + symbol);
                } else {
                    JOptionPane.showMessageDialog(this, "You don't own enough shares to sell!");
                }
            }
            updateDisplays();
            
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter a valid valid quantity (whole number).");
        }
        
        symbolField.setText("");
        quantityField.setText("");
    }

    // Method to refresh text areas
    private void updateDisplays() {
        balanceLabel.setText("Account Balance: $" + String.format("%.2f", accountBalance));
        
        StringBuilder marketBuilder = new StringBuilder();
        for (Map.Entry<String, Double> entry : marketStocks.entrySet()) {
            marketBuilder.append(entry.getKey()).append(" : $").append(entry.getValue()).append("\n");
        }
        marketArea.setText(marketBuilder.toString());

        StringBuilder portfolioBuilder = new StringBuilder();
        double totalPortfolioValue = 0;
        
        if (userPortfolio.isEmpty()) {
            portfolioBuilder.append("No stocks owned.");
        } else {
            for (Map.Entry<String, Integer> entry : userPortfolio.entrySet()) {
                String symbol = entry.getKey();
                int qty = entry.getValue();
                double currentValue = qty * marketStocks.get(symbol);
                totalPortfolioValue += currentValue;
                portfolioBuilder.append(symbol).append(" : ").append(qty).append(" shares ($")
                              .append(String.format("%.2f", currentValue)).append(")\n");
            }
        }
        portfolioBuilder.append("\nTotal Stock Value: $").append(String.format("%.2f", totalPortfolioValue));
        portfolioArea.setText(portfolioBuilder.toString());
    }

    public static void main(String[] args) {
        // Run the GUI
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new StockTradingPlatform().setVisible(true);
            }
        });
    }
}