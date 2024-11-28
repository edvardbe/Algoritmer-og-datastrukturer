#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <stdbool.h>

int main(int argc, char *argv[]){
	int text_len = 0;
	int search_len = 0;
    
	if (argc != 2) {
		printf("Incorrect command line arguments: %d\n", argc);
		return 1;
	}

	char *text = argv[1];

	printf("Enter a search word: ");

	char input[256];
	fgets(input, 256, stdin);

	search_len = strlen(input) - 1; // subtract new line character
    text_len = strlen(text);
    int *map[text_len][search_len];
    int start = 0;
    int end = 0;
    bool prev = false;

    printf("Input length: %i, Text length: %i\n", search_len, text_len);  
//    for (int i = 0; i < text_len; i++){
  //      for (int j = 0; j < 256; j++) {

    for (int j = 0; j < text_len; j++){
        for (int i = search_len - 1; i >= 0; i--) {
            printf("Search letter: %c, Text letter[%i]: %c\n", input[i], j + search_len,  text[j + (search_len - 1)]);
            if(input[i] == text[j + (search_len - 1)]){
                printf("Same letter!!\n");
                //map[j][i] = input[i];
                //
                
                if(i == search_len - 1){
                    for (int h = i; h >= 0; h--){

                        if(input[h] == text[j + (h)]){
                            printf("Input, text: %c, %c", input[h], text[j + (h)]);


                                    }
                             }


                } else {
                    j = j + (search_len - 1 - i);
                    i = search_len;
                    prev = false; 
                }
            }else {
                prev = false;
            }
        }
    }
	return 0;

}
