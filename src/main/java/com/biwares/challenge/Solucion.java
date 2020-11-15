package com.biwares.challenge;

import org.apache.commons.lang.SerializationUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Solucion {
    private static List<Path> allPaths = new ArrayList<>();

    /**
     * En relación a la consigna, en la matriz n * m , N es rows y M columnas
     * @param matrix
     * @param rows
     * @param columns
     * @return
     */
    public static int solucion(int[][] matrix, int rows, int columns) {
        allPaths = new ArrayList<>();

        for (int row=0;row<rows;row++) {
            for (int column = 0; column < columns; column++) {
                System.out.println("insert row " + row + " and column: " + column);
                addCurrentPositionAloneAsPath(row,column);

                List<Path> pathsToTheRigth = findPathsToTheRigth(row,column,columns);
                List<Path> pathsToTheLeft = findPathsToTheLeft(row,column);
                List<Path> pathsUp = findPathsUp(row,column);
                List<Path> pathsDown = findPathsDown(row,column,rows);

                addPathsTurningUpAndDown(Solucion.concatPaths(pathsToTheLeft,pathsToTheRigth), rows);
                addPathsTurningRigthOrLeft(Solucion.concatPaths(pathsUp,pathsDown), columns);
            }
        }
        List<Integer> multiplicationValues = mapPathsToMultiplicationValues(matrix);
        List<Integer> zerosInEachPath = mapMultiplicationValuesToAmountOfZeros(multiplicationValues);
        int maxValue = getMax(zerosInEachPath);
        return maxValue;
    }

    private static void addCurrentPositionAloneAsPath(int row, int column) {
        allPaths.add(new Path(Arrays.asList(new Position(row, column))));
    }

    private static List<Path> findPathsToTheRigth(int row, int column, int maxColumnSize) {
        List<Path> paths = new ArrayList<>();
        List<Position> positions = new ArrayList<>();
        positions.add(new Position(row, column));
        for (int i = column + 1;i < maxColumnSize - 1;i++) {
            positions.add(new Position(row, i));
            paths.add(new Path(copyList(positions)));
        }
        return  paths;
    }

    private static List<Path> findPathsToTheLeft(int row, int column) {
        List<Path> paths = new ArrayList<>();
        List<Position> positions = new ArrayList<>();
        positions.add(new Position(row, column));
        for (int i = column - 1;i >= 0;i--) {
            positions.add(new Position(row, i));
            paths.add(new Path(copyList(positions)));
        }
        return  paths;
    }

    private static List<Path> findPathsUp(int row, int column) {
        List<Path> paths = new ArrayList<>();
        List<Position> positions = new ArrayList<>();
        positions.add(new Position(row, column));
        for (int i = row - 1;i >= 0;i--) {
            positions.add(new Position(i, column));
            paths.add(new Path(copyList(positions)));
        }
        return  paths;
    }

    private static List<Path> findPathsDown(int row, int column, int rows) {
        List<Path> paths = new ArrayList<>();
        List<Position> positions = new ArrayList<>();
        positions.add(new Position(row, column));
        for (int i = row + 1;i < rows - 1;i++) {
            positions.add(new Position(i, column));
            paths.add(new Path(copyList(positions)));
        }
        return  paths;
    }

    private static List<Path> concatPaths(List<Path> pathsToTheLeft, List<Path> pathsToTheRigth) {
        pathsToTheLeft.addAll(pathsToTheRigth);
        return pathsToTheLeft;
    }

    private static void addPathsTurningUpAndDown(List<Path> paths, int rows) {
        List<Path> pathsUp = new ArrayList<>();
        List<Path> pathsDown = new ArrayList<>();
        for(Path path : paths) {
            Position lastPositionFromOriginalPath = path.getPositions().get(path.getPositions().size()-1);
            pathsUp = findPathsUp(lastPositionFromOriginalPath.getRow(),lastPositionFromOriginalPath.getColumn());
            pathsDown = findPathsDown(lastPositionFromOriginalPath.getRow(),lastPositionFromOriginalPath.getColumn(), rows);
            pathsUp = addStartingPositionsToAllPaths(path, pathsUp);
            pathsDown = addStartingPositionsToAllPaths(path, pathsDown);
        }
        allPaths.addAll(paths);
        allPaths.addAll(pathsUp);
        allPaths.addAll(pathsDown);
    }

    private static void addPathsTurningRigthOrLeft(List<Path> paths, int columns) {
        List<Path> pathsRigth = new ArrayList<>();
        List<Path> pathsLeft = new ArrayList<>();
        for(Path path : paths) {
            Position lastPositionFromOriginalPath = path.getPositions().get(path.getPositions().size()-1);
            pathsRigth = findPathsToTheRigth(lastPositionFromOriginalPath.getRow(),lastPositionFromOriginalPath.getColumn(), columns);
            pathsLeft = findPathsToTheLeft(lastPositionFromOriginalPath.getRow(),lastPositionFromOriginalPath.getColumn());
            pathsRigth = addStartingPositionsToAllPaths(path, pathsRigth);
            pathsLeft = addStartingPositionsToAllPaths(path, pathsLeft);
        }
        allPaths.addAll(paths);
        allPaths.addAll(pathsRigth);
        allPaths.addAll(pathsLeft);
    }

    private static List<Integer> mapPathsToMultiplicationValues(int[][] matrix) {
        return allPaths.stream().map(path -> multiplicateEachValueInMatrixFromPath(matrix, path)).collect(Collectors.toList());
    }

    private static Integer multiplicateEachValueInMatrixFromPath(int[][] matrix, Path path) {
        int acumulative = 1;
        for(Position position : path.getPositions()) {
            acumulative = acumulative*matrix[position.getRow()][position.getColumn()];
        };
        return acumulative;
    }

    private static List<Integer> mapMultiplicationValuesToAmountOfZeros(List<Integer> multiplicationValues) {
        return multiplicationValues.stream().map(value -> findZerosInNumber(value)).collect(Collectors.toList());
    }

    private static int findZerosInNumber(int number) {
        char[] numArray = String.valueOf(number).toCharArray();
        int counter=0;
        for(int i=0;i<numArray.length;i++) {
            if(numArray[i]=='0') {
                counter++;
            }

        }
        return counter;
    }

    private static List<Position> copyList(List<Position> positions) {
        List<Position> newList = new ArrayList<>();
        for(Position position: positions) {
            newList.add((Position) SerializationUtils.clone(position));
        }
        return newList;
    }

    private static int getMax(List<Integer> values) {
        return values.stream().max(Integer::compare).get();
    }

    private static List<Path> addStartingPositionsToAllPaths(Path path, List<Path> paths) {
        for (Path pathWithNoStart:paths) {
            pathWithNoStart.setPositions(concatPositions(copyList(path.getPositions()),copyList(pathWithNoStart.getPositions())));
        }
        return paths;
    }

    private static List<Position> concatPositions(List<Position> positions, List<Position> positions1) {
        positions.addAll(positions1);
        List<Position> noDuplicates = positions.stream().distinct().collect(Collectors.toList());
        return noDuplicates;
    }
}
