#include <iostream>
#include <iomanip>
#include <chrono>
#include <ctime>
#include <stdlib.h>
#include <intrin.h>
#include <conio.h>
#include <math.h>
#include <omp.h>

using namespace std;
const int N = 1000;


int main() {
	srand(time(NULL));
	int i, j, k;

	int* A = new int[N * N];
	int* B = new int[N * N];
	int* C = new int[N * N];

	for (int i = 0; i < N; i++)
		for (int j = 0; j < N; j++) {
			A[i * N + j] = rand() % 5;
			B[i * N + j] = rand() % 5;
			C[i * N + j] = 0;
		}

	clock_t begin_time = clock();
	#pragma omp parallel for private(j,k) shared(A,B,C)
	for (i = 0; i < N; i++)
		for (k = 0; k < N; k++) {
			for (j = 0; j < N; j++)
				C[i * N + k] += A[i * N + j] * B[j * N + k];
		}
	clock_t end_time = clock();

	std::cout << N << "x" << N << ": " << int(end_time - begin_time) << "\n";

	delete[] A;
	delete[] B;
	delete[] C;

	system("pause");
	return 0;
}