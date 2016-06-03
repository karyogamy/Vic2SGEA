package main;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.stream.DoubleStream;

/**
 * @author nash
 *         This is static-alike class for report
 */
public class Goods {
    String name = "";

    public String getName() {
        return name;
    }

    public float getPrice() {
        return price;
    }

    public long getConsumption() {
        long result = 0;
        if (supply >= consumption && consumption <= affordable)
            if (supply <= affordable)
                result = (long) supply;
            else
                result = (long) affordable;
        else
            result = (long) consumption;
        return result;
        //return (long)consumption;

    }

    public long getRealSupply() {
        return (long) supply;
    }

    public long getAffordable() {
        return (long) affordable;
    }

    public long getMaxDemand() {
        return (long) maxDemand;
    }

    public long getActualBought() {
        return (long) actualBought;
    }

    public float getBasePrice() {
        return basePrice;
    }

    public float getMinPrice() {
        return basePrice / 5;
    }

    public float getMaxPrice() {
        return basePrice * 5;
    }

    public float getInflation() {
        return price / basePrice * 100;
    }

    public String getTrend() {
        String result = null;
        if (trend < 0) result = "↓";
        if (trend > 0) result = "↑";
        if (trend == 0) result = "-";
        return result;
    }

    public static final Comparator<Goods> nameSort = new Comparator<Goods>() {
        @Override
        public int compare(Goods first, Goods second) {
            //int result=0;
            return first.name.compareTo(second.name);

            //return result;
        }
    };

    public float getOverproduced() {
        float result = 0;
        //if (consumption==0)result=Float.NaN;
        //else result=100-realSupply/consumption*100;
        result = supply / affordable * 100;
        return result;
    }

    public float getFluctuation() {
        if (price_histories.size() <= 1) {
            return 0.0f;
        }

        final List<Float> priceDifferences = new ArrayList<>(price_histories.size());
        for (int i = 1; i < price_histories.size(); i++) {
            final float percentageChange = price_histories.get(i) / price_histories.get(i - 1) * 100;
            priceDifferences.add(percentageChange);
        }

        return stdDev(priceDifferences);
    }

    private float stdDev(final Collection<Float> floats) {
        final double sampleSize = (double) floats.size();

        final double mean = floats.stream().mapToDouble(Float::floatValue).sum() / sampleSize;
        final double variance = floats.stream()
                .mapToDouble(Float::floatValue)
                .map(num -> Math.pow(num - mean, 2))
                .sum() / sampleSize;

        return (float) Math.sqrt(variance);
    }

    /**
     * global
     */
    public float price = 0;
    /**
     * global, in pieces
     */
    public float consumption;
    /**
     * global
     */
    public float supply;
    /**
     * global, in pieces
     */
    public float affordable;
    /**
     * global, in pieces
     */
    public float maxDemand;
    /**
     * global, in pieces
     */
    public float basePrice;
    /**
     * global
     */
    public float trend;
    /**
     * global
     */
    public float actualBought;
    /**
     * global, in pieces, how much was throwed to global market
     */
    public float worldmarketPool;
    /**
     * global, in pieces
     */
    public float actualSoldWorld;

    public List<Float> price_histories;


    //float minPrice;
    //float maxPrice;
    // trend & OVERPRoduced make like function
    public Goods(String iname) {
        super();
        this.name = iname;
        this.price_histories = new ArrayList<>(40);
    }
}
