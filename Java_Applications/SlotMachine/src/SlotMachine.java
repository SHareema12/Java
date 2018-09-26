import java.text.DecimalFormat;
import java.util.*;

///Program submitted by: Safia Hareema///

public class SlotMachine {

    public enum Symbol {
        BELLS("Bells", 10), FLOWERS("Flowers", 5), FRUITS("Fruits", 3),
        HEARTS("Hearts", 2), SPADES("Spades", 1);

        // symbol name
        private final String name;

        // payout factor (i.e. multiple of wager) when matching symbols of this type
        private final int payoutFactor;

        Symbol(String name, int payoutFactor) {
            this.name = name;
            this.payoutFactor = payoutFactor;
        }

        public String getName() {
            return name;
        }

        public int getPayoutFactor() {
            return payoutFactor;
        }

    }
    
    private int numReels;
    
    private double [] odds;
    
    private int wagerUnitValue; 
    
    private double [] cumulativeOdds;
    
    private double totalWager = 0; 
    
    private long totalPayout = 0; 
    
    private Symbol [] reelSymbols; 

    /**
     * Constructor
     * @param numReels number of reels in slot machine
     * @param odds odds for each symbol in a reel, indexed by its enum ordinal value; odds value is non-zero and sums to 1
     * @param wagerUnitValue unit value in cents of a wager
     */
    public SlotMachine(int numReels, double [] odds, int wagerUnitValue) {
    	this.numReels = numReels; 
    	this.odds = odds; 
    	this.wagerUnitValue = wagerUnitValue; 
    	this.reelSymbols = new Symbol[numReels];
    	cumulativeOdds = cumulativeOdds(odds);
    }

    /**
     * Get symbol for a reel when the user pulls slot machine lever
     * @return symbol type based on odds (use Math.random())
     */
    public Symbol getSymbolForAReel() {
    	double rnd = Math.random();
    	//System.out.println("random number: " + rnd);
    	Symbol rndSymbol = Symbol.BELLS; 
    	int result = 0;
    	for (int i = 0; i < cumulativeOdds.length; i++) {
    		if (i == 0) {
    			if (rnd < cumulativeOdds[0]) {
    				rndSymbol = Symbol.BELLS;
    				break;
    			}	
    		}
    		else if(rnd < cumulativeOdds[i]){
    			result = i;
    			break;
    		}    		
    	}
    	if (result == 1) {
    		rndSymbol = Symbol.FLOWERS;
    	}
    	else if (result == 2)
    		rndSymbol = Symbol.FRUITS;
    	else if (result == 3)
    		rndSymbol = Symbol.HEARTS;
    	else if (result == 4)
    		rndSymbol = Symbol.SPADES;
    	return rndSymbol;
    	//Symbol rndSymbol = randomSymbol(rnd, cumulativeOdds);	
    }
    
    //Converts odds array into an array of cumulative probabilities 0 = BELLS, 1 = FLOWERS, 2 = FRUITS, 3 = HEARTS, 4 = SPADES  
    private double [] cumulativeOdds(double [] odds) {
    	double [] cumulativeOdds = new double[odds.length]; 
    	for(int i = 0; i< odds.length; i++) {
    		cumulativeOdds[i] = (i == 0) ? odds[i] : cumulativeOdds[i-1] + odds[i];
    		/*
    		if(i == 0) {
    			cumulativeOdds[i] = odds[i]; 
    		}
    		else {
    			cumulativeOdds[i] = cumulativeOdds[i-1] + odds[i]; 
    		}
    		*/
    	}
    	System.out.println(); 
    	return cumulativeOdds; 
    }
    


    /**
     * Calculate the payout for reel symbols based on the following rules:
     * 1. If more than half but not all of the reels have the same symbol then payout factor is same as payout factor of the symbol
     * 2. If all of the reels have the same symbol then payout factor is twice the payout factor of the symbol
     * 3. Otherwise payout factor is 0
     * Payout is then calculated as wagerValue multiplied by payout factor
     * @param reelSymbols array of symbols one for each reel
     * @param wagerValue value of wager given by the user
     * @return calculated payout
     */
    public long calcPayout(Symbol[] reelSymbols, int wagerValue) {
    	Symbol symbol, maxSymbol;
    	int max = 0, payoutFactor = 0;
    	long payout = 0; 
    	// BELLS = 0, FLOWERS = 1, FRUITS = 2, HEARTS - 3, SPADES = 4
    	int [] symbolCount = new int[Symbol.values().length];
    	for (int i = 0; i < symbolCount.length; i++) {
    		symbolCount[i] = 0;
    	}
    	for (int i = 0; i < reelSymbols.length; i++) {       	
    		symbol = reelSymbols[i]; 
    		switch (symbol) {
    		case BELLS:
    			symbolCount[Symbol.BELLS.ordinal()]++;
    			continue;
    		case FLOWERS:
    			symbolCount[Symbol.FLOWERS.ordinal()]++;
    			continue;
    		case FRUITS:
    			symbolCount[Symbol.FRUITS.ordinal()]++;
    			continue;
    		case SPADES:
    			symbolCount[Symbol.SPADES.ordinal()]++;
    			continue;
    		case HEARTS:
    			symbolCount[Symbol.HEARTS.ordinal()]++;
    			continue;
    		}
    		
        }
    	//find max amount that any symbol appears
    	for (int i = 0; i < symbolCount.length;i++) {
			max = (symbolCount[i] > max) ? symbolCount[i] : max; 
		}
    	//check if all symbols are the same in the reel, then pay double the amount for the symbol
    	if (max == reelSymbols.length) {
    		maxSymbol = reelSymbols[0]; 
    		payoutFactor = maxSymbol.getPayoutFactor();
    		for (int i = 0; i < reelSymbols.length; i++) {
    			payout += 2*(payoutFactor*wagerValue);
    		}
    	}
    	//check if more than half are the same symbol, then pay normal amount for each symbol
    	else if ((double)max > ((double)reelSymbols.length)/2){
    		for (int i = 0; i < reelSymbols.length; i++) {
    			maxSymbol = reelSymbols[i]; 
        		payoutFactor = maxSymbol.getPayoutFactor();
    			payout += payoutFactor*wagerValue;
    		}
    	}
    	else {
    		payout = 0;
    	}
    	return payout;
    }
     

    
    /**
     * Called when the user pulls the lever after putting wager tokens
     * 1. Get symbols for the reels using getSymbolForAReel()
     * 2. Calculate payout using calcPayout()
     * 3. Display the symbols, e.g. Bells Flowers Flowers..
     * 4. Display the payout in dollars and cents e.g. $2.50
     * 5. Keep track of total payout and total receipts from wagers
     * @param numWagerUnits number of wager units given by the user
     */
    public void pullLever(int numWagerUnits) {
    	this.totalWager += numWagerUnits*this.wagerUnitValue;
    	System.out.println("Wager for this pull:" + numWagerUnits*this.wagerUnitValue);
    	System.out.println("Total wager for this pull: " + this.totalWager);
    	for (int i = 0; i < reelSymbols.length; i++) {
    		reelSymbols[i] = getSymbolForAReel();
    		System.out.print(reelSymbols[i].getName() + " ");
    	}
    	long payout = calcPayout(reelSymbols, this.wagerUnitValue*numWagerUnits);
    	this.totalPayout += payout; 
    	System.out.println();
    	double pullPayout = ((double)(payout)/100);
    	DecimalFormat dFormat = new DecimalFormat("#.00");
    	System.out.println("$" + dFormat.format(pullPayout));
    	//System.out.println("payout: $" + ((double)(payout)/100));
    	
    }

    /**
     * Get total payout to the user as percent of total wager value
     * @return e.g. 85.5
     */
    public double getPayoutPercent() {
        double result = (this.totalPayout/this.totalWager)*100;
    	return result;
    }

    /**
     * Clear the total payout and wager value
     */
    public void reset() {
    	this.totalPayout = 0;
    	this.totalWager = 0;
    }

    public static void main(String [] args) {
    	//int a = 2, b = 3; 
    	//System.out.println((double)a/(double)b);
        double [] odds = new double[Symbol.values().length];
        // sum of odds array values must equal 1.0
        odds[Symbol.HEARTS.ordinal()] = 0.3;
        odds[Symbol.SPADES.ordinal()] = 0.25;
        odds[Symbol.BELLS.ordinal()] = 0.05;
        odds[Symbol.FLOWERS.ordinal()] = 0.2;
        odds[Symbol.FRUITS.ordinal()] = 0.2;
        
        /*
        for(int i = 0; i < odds.length; i++) {
        	System.out.println(odds[i]);
        	
        }
        */
        
        SlotMachine sm = new SlotMachine(3, odds, 25); // quarter slot machine
        sm.reset();
        sm.pullLever(2);
        sm.pullLever(1);
        sm.pullLever(3);
        System.out.println("Pay out percent to user = " + sm.getPayoutPercent() + "%");
        
        sm.reset();
        sm.pullLever(4);
        sm.pullLever(1);
        sm.pullLever(1);
        sm.pullLever(2);
        System.out.println("Pay out percent to user = " + sm.getPayoutPercent() + "%");
        
    }



}
