
Finne igjen elementer i $\Theta$ (1) tid , selv om nøkkelen ikke er egnet som tabellindex
	Sortert tabell med binærsøk gir bare O (log n)

#### Gode hashfunksjoner

- Må alltid returnere en verdi mellom 0 og tabellstørrelsen
- Går raskt å beregne
- Gir god spredning
- Gir få kollisjoner

### Hash basert på restdivisjon

m: tabellstørrelse, k: nøkkel, h: hash-funskjon

- Rest divisjon: h (k) = k mod m
	- Virker best hvis m er et primtall
	- Rest divisjon med heltall får fort
	- Dårlig hvi sm er en toerpotens. For m = 16


### Multiplikativ hash
m: tabellstørrelse

### Lenka lister
### Åpen adressering

- Når elementer kolliderer, legger vi de ekstra elementene  andre steder i tabellen. (Enkleste er "neste ledige plass")
- Probesekvensen er systematisk, så vi lett kan finne igjen elementer
- Tabellen kan ikke overfylles
- $\alpha: Fyllingsgrad$
- Tid for å sette inn eller finne igjen $\frac{1}{\alpha} ln \frac{1}{1-\alpha}$


### Dobbel hashing
Beste probe-metode!!

$h_1(k)$ : første forsøk på plassering i tabell
$h_2(k)$ : andre forsøk der man ønsker å finne hopplengde
$h_2(k)$ må ikke gi en verdi på 0, for da vil ikke indekseringen flytte seg. Den må heller ikke gi en verdi som er en faktor av tabell størrelsen, derfor velger man gjerne primtalls størrelser slik at ingen verdier er faktorer av tabellen. Eller så kan man velge en toer-potens og sjekker om det er oddetall.


- Dijkstra korteste vei algoritme
	- Sortert tabell $O(KN)$
	- Usortert tabell $O(N^{2})$
	- Heap $O((K+N) log(N))$ å