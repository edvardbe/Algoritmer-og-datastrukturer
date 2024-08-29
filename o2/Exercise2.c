#include <stdlib.h>
#include <stdio.h>
#include <time.h>

double recursive_v1(double base, int exponent){
	if (exponent == 1){
		return base;
	}
	else {
		return base * recursive_v1(base, exponent - 1);
	}
}

double recursive_v2(double base, int exponent){
	if (exponent == 1){
		return base;
	}
	else if ((exponent & 1) == 1){
		double temp = recursive_v2(base * base, (exponent - 1) / 2);
		return base * (temp);
	}
	else {
		double temp = recursive_v2(base * base, exponent / 2);
		return temp;
	}
}

int main(){
	
	printf("Jeg elsker Irja!! <3 \n");

	double base = 2.0;

	int exponents[] = {128, 256, 512, 1024, 2048, 4096, 8192, 16384};
	double result = 0;


	long start, end;
	
	for (int i = 0; i < 8; i++){
		
		start = clock();

		result = recursive_v1(base, exponents[i]);

		
		end = clock();
		
		double timeTaken = (end - start) / CLOCKS_PER_SEC;

		printf("Exponent number %f ^ %d = %f\n. Time taken: %f\n", base, exponents[i], result, timeTaken);
	}

	return 0;
}
