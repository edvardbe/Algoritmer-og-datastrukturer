
### Oppgave 1
**a)**
```
int prog_a(int a, int b, int c) {
	int sum = 0; 
	for (int i=1; i<a; ++i){ // O(a) || a
	
		for (int j=1; j<c; ++j) { // O(c) || a*c
		
			for (int k=a; k>0; --k) { //
				sum += i*j-k; // i * j - a
				if (sum > b) return; 
			} 
		} 
	} 
	return sum; 
}
```

$\Omega (1)$ og $O(n^3)$ 

**b)**
```
int prog_b(int q, int r, int p) {
	int sum = 0; 
	for (int i=0; i<q; i += 1){
		sum += i * r;
		if (sum > p){
			sum -= p; 
		}
	} 
}
```
$\Theta(q)$ 

**c)**

```
int prog_c(int n, int[] tab) {
	int sum = 0; 
	if(n > 0)
		sum += 4 * prog_c(n/2, tab); 
		for (int i=0; i<n; ++i) sum += tab[i]; 
		tab[0]--; 
		sum += 4 * prog_c(n/2, tab);
	} 
	return sum;
}
```

Der $a$ er antall rekursive kall, $b$ er i hvilken grad $n$ påvirkes for hvert rekursive kall, $c$ er en konstant foran løkkene og $k$ er antall nøstede løkker vil den generelle formen for å finne tidskompleksiteten være:

$$T(n) = aT(n/b) + cn^k$$
$b^k < a \rightarrow T(n) \in \Theta(n^{log_b(a)})$
$b^k = a \rightarrow T(n) \in \Theta(n^k * log(n))$
$b^k > a \rightarrow T(n) \in \Theta(n^k)$ 

for dette tilfellet vil det se slik ut:

$$
T(n) = 2T(n/2) + n^1
$$
der $a = 2$, $b = 2$ and $k = 1$. Altså er $b^k = 2^1 = a = 2$ så $T(n) \rightarrow \Theta(n * log(n)$ 


**d)**

```
double prog_d(int n, float x) {
	if (n == 0) return 0.0; 
	else return x + prog_d(n - 1, x); 
}
```

$$
T(n) = T(n-1)
$$
Dette er en lineær rekursiv funksjon som har som kjører $\Theta(n)$ ganger.

**e)**
```
int prog_e(int a, int b, int c) {
int sum = 1; 
if (a < b) { 
	for (int i = 1; i < a; ++i) sum *= i; 
	for (int j = a; j < b; ++j) sum += j; 
}
return sum;
```

Her er nedre grense $\Omega(1)$ dersom $a$ er større enn $b$. For den øvre grensen går den første løkken går itererer $a$ ganger, mens den andre løkken itererer fra $a$ til $b$, med andre ord $b-a$ ganger. Den totale øvre grensen blir da $O(a + (b - a))$ eller $O(b)$.

### Oppgave 2
**a)** <ins>Jeg bruker en heap som prioritetskø. Den inneholder n elementer. Hva blir kjøretidene for disse tre operasjonene: å sette inn et element til, å ta ut elementet på toppen, å endre prioritet for et element.</ins>

Kjøretider:
- **Sette inn et element: $O(n log(n))$ og $\Omega(1)$**
Denne operasjonen vil ha en øvre grense på $O(n log(n))$ ettersom dette er tiden det tar å sette inn et element der hele heapen må endres. Men for operasjonens nedre grense vil det være $\Omega(1)$ ettersom det kan ta konstant tid hvis prioritetskøen er tom fra før.

- **Ta ut et element på toppen: $O(n log(n))$ og $\Omega(1)$**
Den øvre grensen vil være $O(n log(n))$ hvis man fjerner et element fordi dette kan kreve heapen må sorteres på nytt. Det øverste elementet har resten av elementene under seg og det må lages en ny heap ut fra disse. Ved nedre grense vil det være $\Omega(1)$ ettersom det kan være kun et element i heapen, og da trengs det ikke å lage ny heap.

- **Endre prioritet for et element: $O(n log(n))$ og $\Omega(1)$**
Å endre prioritet for et element vil ha en øvre grense på $O(n log(n))$ fordi heapen må lages på nytt. Dersom det kun er et element i heapen vil det ikke trengs å lage en ny heap.
**b)**


### Oppgave 3
**a)**
Lempel-ziv
**b)** <ins>A*-algoritmen er en videreutvikling av Dijkstras algoritme. Hva er det A* gjør annerledes? Forklar.</ins>
Det A*-algoritmen gjør annerledes i forhold til Dijkstra algoritmen er at den prioriterer utifra et estimat. Det er viktig at estimatet ikke overestimerer, altså hvis den faktiske avstanden mellom Lerkendal og Nidarosdomen er 2 km vil et estimat på 3 km gjøre at den blir prioritert lenger ned enn den bør. Dersom vi ønsker å finne korteste vei kan vi finne estimatet ved å utregne avstanden fra start noden til mål noden i luftlinje. Om det er raskeste vei vi er ute etter kan vi estimere tiden ved å ta avstanden i luftlinje delt på den høyeste mulige fartsgrensen.

### Oppgave 4

**a)** Finn maksimal flyt fra K til S. Nytt flytøkende veier, og skriv opp hver vei og hvor mye flyt du legger til langs veien. 

**b)** Sorter grafen topologisk, eller forklar hvorfor det ikke er mulig. 

**c)** Se bort fra retningen på kantene, og finn et minimalt spenntre for grafen. Skriv opp hvilke kanter som blir med, og total vekt på spenntreet. 

**d)** Finn et annet minimalt spenntre i denne grafen, eller forklar hvorfor det ikke er mulig.
