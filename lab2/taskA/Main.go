package main

import (
	"fmt"
	"math/rand"
	"sync"
	"time"
)

const N = 20
const M = 30
var isBearFound = false

type Forest struct {
	area    [M]bool
	curArea int
}

func printMatrix(n, m int, matrix[][] bool) {
	for i := 0; i < n; i++ {
		for j := 0; j < m; j++ {
			if matrix[i][j] {
				fmt.Print("☻ ")
			} else {
				fmt.Print("☺ ")
			}
		}
		fmt.Println()
	}
}

func generateForest(n, m int) [][]bool {
	forestMatrix := make([][]bool, n)
	for i := range forestMatrix {
		forestMatrix[i] = make([]bool, m)
	}

	randSource := rand.NewSource(time.Now().UnixNano())
	randGenerator := rand.New(randSource)

	forestMatrix[randGenerator.Intn(n)][randGenerator.Intn(m)] = true

	return forestMatrix
}

func divideForestInAreas(n, m int, forestMatrix[][] bool) []Forest {
	var forestAreas[]Forest
	for i := 0; i < n; i++ {
		var forestArea Forest
		forestArea.curArea = i

		for j := 0; j < m; j++ {
			forestArea.area[j] = forestMatrix[i][j]
		}

		forestAreas = append(forestAreas, forestArea)
	}

	return forestAreas
}

func searchBear(area[M] bool, flockId int, areaId int) {
	for index, i := range area {
		if i == true {
			isBearFound = true
			fmt.Println("Flock of bees #", flockId, " found Winnie The Pooh at (", areaId, ",", index,")")
			return
		}
	}
	fmt.Println("Flock of bees #", flockId, " found nothing at area #", areaId)
}

func startSearching(waitGroup *sync.WaitGroup, areas <-chan Forest, flockId int) {
	defer waitGroup.Done()

	for currentArea := range areas {
		if isBearFound { return
		} else {
			fmt.Println("Flock of bees #", flockId, " at area #", currentArea.curArea)
			searchBear(currentArea.area, flockId, currentArea.curArea)
			fmt.Println("Flock of bees #", flockId, " at HOME")
		}
	}
}

func main() {

	forestMatrix := generateForest(N, M)
	var waitGroup sync.WaitGroup
	areas := make(chan Forest, N)
	forestAreas := divideForestInAreas(N, M, forestMatrix)

	for i := 0; i < N; i++ {
		areas <- forestAreas[i]
	}

	fmt.Println("Forest")
	printMatrix(N, M, forestMatrix)

	for i := 0; i < 3; i++ {
		waitGroup.Add(1)
		go startSearching(&waitGroup, areas, i)
	}

	close(areas)
	waitGroup.Wait()
}