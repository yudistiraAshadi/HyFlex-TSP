package TSP;

import AbstractClasses.ProblemDomain;

public class TSP extends ProblemDomain
{
    private TSPInstance instance;
    private TSPSolution[] solutionMemory = new TSPSolution[2];
    private double shortestDistance = Double.POSITIVE_INFINITY;

    public TSP(long seed)
    {
        super(seed);
    }

    @Override
    public double applyHeuristic(int heuristicID, int solutionSourceIndex, int solutionDestinationIndex)
    {
        long startTime = System.currentTimeMillis();

        switch (heuristicID) {
            case 0:
                this.swapCities(solutionSourceIndex, solutionDestinationIndex);
                break;
            case 1:
                this.scrambleCities(solutionSourceIndex, solutionDestinationIndex);
                break;
            case 2:
                this.moveCity(solutionSourceIndex, solutionDestinationIndex);
                break;
            default:
                System.err.println("heuristic does not exist");
                System.exit(-1);
        }

        ++this.heuristicCallRecord[heuristicID];
        this.heuristicCallTimeRecord[heuristicID] = (int) ((long) this.heuristicCallTimeRecord[heuristicID]
                + (System.currentTimeMillis() - startTime));

        double currentSolutionDistance = this.solutionMemory[solutionDestinationIndex].getTotalDistance();
        this.verifyShortestDistance(currentSolutionDistance);

        return currentSolutionDistance;
    }

    @Override
    public double applyHeuristic(int heuristicID, int solutionSourceIndex1, int solutionSourceIndex2,
            int solutionDestinationIndex)
    {
        long startTime = System.currentTimeMillis();

        switch (heuristicID) {
            case 0:
                this.swapCities(solutionSourceIndex1, solutionDestinationIndex);
                break;
            case 1:
                this.scrambleCities(solutionSourceIndex1, solutionDestinationIndex);
                break;
            case 2:
                this.moveCity(solutionSourceIndex1, solutionDestinationIndex);
                break;
            default:
                System.err.println("heuristic does not exist");
                System.exit(-1);
        }

        ++this.heuristicCallRecord[heuristicID];
        this.heuristicCallTimeRecord[heuristicID] = (int) ((long) this.heuristicCallTimeRecord[heuristicID]
                + (System.currentTimeMillis() - startTime));

        double currentSolutionDistance = this.solutionMemory[solutionDestinationIndex].getTotalDistance();
        this.verifyShortestDistance(currentSolutionDistance);

        return currentSolutionDistance;
    }

    private void verifyShortestDistance(double currentSolutionDistance)
    {
        if (currentSolutionDistance < this.shortestDistance) {
            this.shortestDistance = currentSolutionDistance;
            System.out.println(this.shortestDistance);
        }
    }

    private void swapCities(int sourceIndex, int targetIndex)
    {
        // get the list of the cities and the number of the cities
        int[] citiesOrder = this.solutionMemory[sourceIndex].getCitiesOrder();
        int numberOfCities = citiesOrder.length;

        // randomly take two cities
        int randomCity1 = this.rng.nextInt(numberOfCities);
        int randomCity2 = this.rng.nextInt(numberOfCities);

        // Swap the previously selected random cities
        int tempCity = citiesOrder[randomCity1];
        citiesOrder[randomCity1] = citiesOrder[randomCity2];
        citiesOrder[randomCity2] = tempCity;

        // assign the current travel to targetIndex
        double currentTotalDistance = this.instance.getTotalDistance(citiesOrder);
        this.solutionMemory[targetIndex] = new TSPSolution(citiesOrder, currentTotalDistance);
    }

    private void moveCity(int sourceIndex, int targetIndex)
    {
        int[] citiesOrder = (int[]) this.solutionMemory[sourceIndex].getCitiesOrder();

        // Take two random integer as the city and the moving index
        int randomCity = this.rng.nextInt(citiesOrder.length);
        int movingIndex;
        while ((movingIndex = this.rng.nextInt(citiesOrder.length)) == randomCity) {
            ;
        }

        // create new cities order and put the randomly chosen city to the moving index
        int[] newCitiesOrder = new int[citiesOrder.length];
        newCitiesOrder[movingIndex] = citiesOrder[randomCity];

        int currentIndex;
        int index;
        if (randomCity < movingIndex) {
            currentIndex = 0;

            for (index = 0; index < citiesOrder.length; ++index) {
                if (currentIndex == randomCity) {
                    ++index;
                }

                if (currentIndex == movingIndex) {
                    --index;
                } else {
                    newCitiesOrder[currentIndex] = citiesOrder[index];
                }

                ++currentIndex;
            }
        } else {
            currentIndex = 0;

            for (index = 0; index < citiesOrder.length; ++index) {
                if (currentIndex == movingIndex) {
                    --index;
                } else {
                    newCitiesOrder[currentIndex] = citiesOrder[index];
                    if (currentIndex == randomCity) {
                        ++index;
                    }
                }

                ++currentIndex;
            }
        }

        double currentTotalDistance = this.instance.getTotalDistance(newCitiesOrder);
        this.solutionMemory[targetIndex] = new TSPSolution(newCitiesOrder, currentTotalDistance);
    }

    private void scrambleCities(int sourceIndex, int targetIndex)
    {
        // get the list of the cities
        int[] citiesOrder = this.solutionMemory[sourceIndex].getCitiesOrder();

        // scramble the cities
        int tempIndex;
        int tempCity;
        for (int i = citiesOrder.length; i > 1; citiesOrder[tempIndex] = tempCity) {
            tempIndex = rng.nextInt(i);
            --i;
            tempCity = citiesOrder[i];
            citiesOrder[i] = citiesOrder[tempIndex];
        }

        // assign the current travel to targetIndex
        double currentTotalDistance = this.instance.getTotalDistance(citiesOrder);
        this.solutionMemory[targetIndex] = new TSPSolution(citiesOrder, currentTotalDistance);
    }

    @Override
    public String bestSolutionToString()
    {
        return "Shortest Distance = " + this.shortestDistance;
    }

    @Override
    public boolean compareSolutions(int solutionIndex1, int solutionIndex2)
    {
        int[] solution1 = this.solutionMemory[solutionIndex1].getCitiesOrder();
        int[] solution2 = this.solutionMemory[solutionIndex2].getCitiesOrder();

        int numberOfCities = this.instance.getNumberOfCities();
        for (int i = 0; i < numberOfCities; ++i) {
            if (solution1[i] != solution2[i]) {
                return false;
            }
        }

        return true;
    }

    @Override
    public void copySolution(int solutionSourceIndex, int solutionDestinationIndex)
    {
        this.solutionMemory[solutionDestinationIndex] = this.solutionMemory[solutionSourceIndex].clone();
    }

    @Override
    public double getBestSolutionValue()
    {
        return this.shortestDistance;
    }

    @Override
    public double getFunctionValue(int solutionIndex)
    {
        return this.solutionMemory[solutionIndex].getTotalDistance();
    }

    @Override
    public int[] getHeuristicsOfType(HeuristicType arg0)
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public int[] getHeuristicsThatUseDepthOfSearch()
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public int[] getHeuristicsThatUseIntensityOfMutation()
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public int getNumberOfHeuristics()
    {
        return 3;
    }

    @Override
    public int getNumberOfInstances()
    {
        return 1;
    }

    @Override
    public void initialiseSolution(int solutionIndex)
    {
        int[] initialSolution = new int[this.instance.getNumberOfCities()];

        for (int i = 0; i < initialSolution.length; i++) {
            initialSolution[i] = i;
        }

        double totalDistance = this.instance.getTotalDistance(initialSolution);
        this.solutionMemory[solutionIndex] = new TSPSolution(initialSolution, totalDistance);
        this.verifyShortestDistance(totalDistance);
    }

    @Override
    public void loadInstance(int index)
    {
        this.instance = new TSPInstance();
    }

    @Override
    public void setMemorySize(int size)
    {
        TSPSolution[] newSolutionMemory = new TSPSolution[size];

        if (this.solutionMemory != null) {
            for (int i = 0; i < this.solutionMemory.length; ++i) {
                if (i < size) {
                    newSolutionMemory[i] = this.solutionMemory[i];
                }
            }
        }

        this.solutionMemory = newSolutionMemory;
    }

    @Override
    public String solutionToString(int solutionIndex)
    {
        return this.solutionMemory[solutionIndex].toString();
    }

    @Override
    public String toString()
    {
        return this.instance.toString();
    }

}
