#include <stdlib.h>
#include <stdio.h>

int main(int argv, char *args[]){
    int input = atoi(args[1]);
    printf("Left shifted integer: %i\n", (input << 1));
    return 0;
}
