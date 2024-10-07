
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