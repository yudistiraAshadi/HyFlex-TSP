package TSP;

public class TSPSolution implements Cloneable
{
    private int[] citiesOrder;
    private double totalDistance;

    TSPSolution(int[] citiesOrder, double totalDistance)
    {
        this.citiesOrder = citiesOrder;
        this.totalDistance = totalDistance;
    }

    public TSPSolution clone()
    {
        int[] citiesOrder = (int[]) this.citiesOrder.clone();
        return new TSPSolution(citiesOrder, this.totalDistance);
    }

    public int[] getCitiesOrder()
    {
        return (int[]) this.citiesOrder.clone();
    }

    public double getTotalDistance()
    {
        return (double) this.totalDistance;
    }

    public String toString()
    {
        StringBuilder builder = new StringBuilder();

        builder.append("Total Distance = " + this.totalDistance + "\n");
        builder.append("Cities Order =");
        for (int i = 0; i < this.citiesOrder.length; ++i) {
            builder.append(" " + this.citiesOrder[i]);
        }

        return builder.toString();
    }
}
