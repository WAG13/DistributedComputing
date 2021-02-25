package main

import (
	"fmt"
	"math/rand"
	"time"
)

const SmokersNum = 3

func smoker(component int, semaphoreForSmoking chan bool, semaphoreForDealing chan bool, table* []bool){
	for{
		<-semaphoreForSmoking
		fmt.Println("Smoker #", component, "Сhecking table")
		if !(*table)[component]{
			fmt.Println("Smoker #", component, "Smoking 🚬")
			time.Sleep(time.Second)
			for i := range *table{
				(*table)[i] = false
			}
			semaphoreForDealing<-true
		}
	}
}

func middle(semaphoreForSmoking chan bool, semaphoreForDealing chan bool, table* []bool){
	for {
		<- semaphoreForDealing
		var first, second= getCigaretteStuff()
		fmt.Println("New items",first,"&",second)
		(*table)[first] = true
		(*table)[second] = true
		for i := 0; i < SmokersNum; i++ {
			semaphoreForSmoking <- true
		}
	}
}

func getCigaretteStuff() (int, int) {
	stuff1 := rand.Intn(SmokersNum)
	stuff2 := rand.Intn(SmokersNum)
	for stuff2 == stuff1 {
		stuff2 = rand.Intn(SmokersNum)
	}

	return stuff1, stuff2
}

func main(){

	var table = make([]bool, SmokersNum, SmokersNum)
	var semaphoreForSmoking = make(chan bool, SmokersNum)
	var semaphoreForDealing = make(chan bool, 1)
	semaphoreForDealing <- true
	go middle(semaphoreForSmoking, semaphoreForDealing, &table)
	for i := 0; i < SmokersNum; i++{
		go smoker(i, semaphoreForSmoking, semaphoreForDealing, &table)
	}
	_, _ = fmt.Scanln()
}