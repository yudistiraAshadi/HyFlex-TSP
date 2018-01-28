package TSP;

import java.util.ArrayList;

public class TSPInstance
{
    private ArrayList<City> listOfCities = new ArrayList<>();

    public TSPInstance()
    {
        this(9999);
    }

    public TSPInstance(int numberOfCities)
    {
        for (int i = 0; i < numberOfCities; i++) {
            this.listOfCities.add(new City());
        }
    }

    public TSPInstance(ArrayList<City> travel)
    {
        this.listOfCities = travel;
    }

    public int getNumberOfCities()
    {
        return this.listOfCities.size();
    }

    public City getCity(int index)
    {
        return this.listOfCities.get(index);
    }

    public double getTotalDistance(int[] citiesOrder)
    {
        double totalDistance = 0.0;

        for (int i = 0; i < citiesOrder.length; i++) {
            City starting = this.getCity(citiesOrder[0]);
            City destination;

            if (i + 1 < citiesOrder.length) {
                destination = this.getCity(citiesOrder[i + 1]);
            } else {
                destination = this.getCity(citiesOrder[0]);
            }

            totalDistance += starting.distanceToCity(destination);
        }

        return totalDistance;
    }

    public String toString()
    {
        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < this.listOfCities.size(); ++i) {
            int xCoor = this.listOfCities.get(i).getX();
            int yCoor = this.listOfCities.get(i).getY();
            builder.append(xCoor + " " + yCoor + "\n");
        }

        return builder.toString();
    }
}
