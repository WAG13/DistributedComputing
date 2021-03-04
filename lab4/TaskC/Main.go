package main

import (
	"bytes"
	"fmt"
	"math/rand"
	"sync"
	"time"
)


var seed = rand.NewSource(time.Now().UnixNano())
var random = rand.New(seed)

var nameGen = [...]string {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P"}

type journeyT struct {
	from int
	to int
	price [] int
	sync.RWMutex
}

type journeysT struct {
	list []journeyT
	cities [] string
	sync.RWMutex
}

func dfs(j*journeysT, fromCity int, toCity int) ([] int, int) {
	return dfsRecur(j, fromCity, toCity, map[int]bool{}, make([]int, 0), 0)
}

func dfsRecur(j*journeysT, cur int, toCity int, v map[int]bool, path [] int, summary int) ([] int, int) {
	v[cur] = true

	if cur == toCity {
		return path, summary
	}

	for journeyId := range j.list {

		//fmt.Printf("Journey from %d: \n %+v \n\n", j.list[journey_id].from, j.list[journey_id])

		if j.list[journeyId].from == cur && !v[j.list[journeyId].to] {

			min := j.list[journeyId].price[0]
			for i := range j.list[journeyId].price {
				if j.list[journeyId].price[i] < min {
					min = j.list[journeyId].price[i]
				}
			}

			return dfsRecur(j, j.list[journeyId].to, toCity, v, append(path, j.list[journeyId].to), summary + min)
		}
	}

	return make([]int, 0), -1
}

func randomizeCity(j*journeysT) string {
	return j.cities[random.Intn(len(j.cities))]
}

func randomizeName() string {
	var name bytes.Buffer

	for i := 1; i <= 8; i++ {
		name.WriteString(nameGen[random.Intn(len(nameGen))])
	}

	return name.String()
}

func getCityByName(j*journeysT, name string) int {
	for id, city := range j.cities {
		if city == name {
			return id
		}
	}

	return -1
}

func addCity(j*journeysT, name string) {
	j.cities = append(j.cities, name)
}

func removeCity(j*journeysT, name string) {

	id := getCityByName(j, name)

	if id < 0 {
		return
	}

	for journeyId := 0; journeyId < len(j.list); journeyId++ {
		if id == j.list[journeyId].from || id == j.list[journeyId].to {
			j.list = append(j.list[:journeyId], j.list[journeyId+1:]...)
			journeyId--
		}
	}

	j.cities = append(j.cities[:id], j.cities[id+1:]...)
}

func getPath(j*journeysT, cityFrom int, cityTo int) int {

	for id, journey := range j.list {
		if cityFrom == journey.from && cityTo == journey.to {
			return id
		}
	}
	return -1
}

func addPath(j*journeysT, cityFrom int, cityTo int, price int) {

	pathId := getPath(j, cityFrom, cityTo)

	if pathId < 0 {
		j.list = append(j.list, journeyT{
			from:  cityFrom,
			to:    cityTo,
			price: []int{price},
		})
	} else {
		j.list[pathId].price = append(j.list[pathId].price, price)

	}
}

func removePath(j*journeysT, cityFrom int, cityTo int, price int) {

	pathId := getPath(j, cityFrom, cityTo)

	if pathId < 0 {
		return
	}

	if len(j.list[pathId].price) < 2 {
		j.list = append(j.list[:pathId], j.list[pathId+1:]...)
		return
	}

	for i, priceItem := range j.list[pathId].price {
		if priceItem == price {
			j.list[pathId].price = append(j.list[pathId].price[:i], j.list[pathId].price[i+1:]...)
			return
		}
	}
}

func findPath(j*journeysT, fromCity string, toCity string) ([] int, int) {
	return dfs(j, getCityByName(j, fromCity), getCityByName(j, toCity))
}

func getPrices(j*journeysT, cityFrom int, cityTo int) []int {

	journeyId := getPath(j, cityFrom, cityTo)
	if journeyId > 0 {
		return j.list[journeyId].price
	}

	return []int{}
}

func changePrice(j*journeysT, cityFrom int, cityTo int, price int, endPrice int) {
	journeyId := getPath(j, cityFrom, cityTo)
	if journeyId > 0 {
		for i, priceItem := range j.list[journeyId].price {
			if priceItem == price {
				j.list[journeyId].price[i] = endPrice
			}
		}
	}
}

/////////////////////////////////////////////////////////////////////////////////////

func changeTicketPrice(j*journeysT) {
	for {
		j.Lock()

		from := randomizeCity(j)
		to := randomizeCity(j)

		if from == to {
			j.Unlock()
			continue
		}

		prices := getPrices(j, getCityByName(j, from), getCityByName(j, to))
		if len(prices) == 0 {
			j.Unlock()
			continue
		}
		price := prices[random.Intn(len(prices))]
		endPrice := random.Intn(10)
		changePrice(j, getCityByName(j, from), getCityByName(j, to), price, endPrice)

		fmt.Printf("Change price for %s to %s: \n price: %d to %d \n\n", from, to, price, endPrice)

		j.Unlock()
		time.Sleep(400 * time.Millisecond)
	}
}

func changeJourneys(j*journeysT) {
	for {
		j.Lock()

		from := randomizeCity(j)
		time.Sleep(10 * time.Millisecond)
		to := randomizeCity(j)

		if from == to {
			j.Unlock()
			continue
		}

		if random.Intn(10) > 4 {
			//add
			price := random.Intn(9) + 1
			addPath(j, getCityByName(j, from), getCityByName(j, to), price)
			fmt.Printf("Add Journey from %s to %s: \n price: %d \n\n", from, to, price)
		} else {
			//remove
			prices := getPrices(j, getCityByName(j, from), getCityByName(j, to))
			if len(prices) == 0 {
				j.Unlock()
				continue
			}
			price := prices[random.Intn(len(prices))]
			removePath(j, getCityByName(j, from), getCityByName(j, to), price)
			fmt.Printf("Remove Journey from %s to %s: \n price: %d \n\n", from, to, price)
		}
		j.Unlock()

		time.Sleep(500 * time.Millisecond)
	}
}

func changeCities(j*journeysT) {
	for {
		j.Lock()

		oldCity := randomizeCity(j)
		newCity := randomizeName()

		removeCity(j, oldCity)
		addCity(j, newCity)

		fmt.Printf("Removed %s and added %s \n\n", oldCity, newCity)

		j.Unlock()

		time.Sleep(6000 * time.Millisecond)
	}
}

func checkJourney(j*journeysT) {
	for {
		j.Lock()

		from := randomizeCity(j)
		to := randomizeCity(j)
		if from == to {
			j.Unlock()
			continue
		}
		path, sum := findPath(j, from, to)

		fmt.Printf("Check Journey from %s to %s: \n sum: %d \n path: %v \n\n", from, to, sum, path)

		j.Unlock()

		time.Sleep(2000 * time.Millisecond)
	}
}

func main() {
	j := &journeysT{
		list: make([]journeyT, 0),
		cities: make([]string, 0),
	}


	addCity(j, "A")
	addCity(j, "B")
	addCity(j, "C")
	addCity(j, "D")

	addPath(j, getCityByName(j, "A"), getCityByName(j, "B"), 10)
	addPath(j, getCityByName(j, "A"), getCityByName(j, "B"), 5)
	addPath(j, getCityByName(j, "B"), getCityByName(j, "C"), 10)
	addPath(j, getCityByName(j, "C"), getCityByName(j, "D"), 10)
	addPath(j, getCityByName(j, "D"), getCityByName(j, "A"), 5)
	addPath(j, getCityByName(j, "C"), getCityByName(j, "A"), 8)

	fmt.Printf("j: %+v\n", j)

	go changeCities(j)
	go changeJourneys(j)
	go changeTicketPrice(j)
	go checkJourney(j)

	_, _ = fmt.Scanln()
}
