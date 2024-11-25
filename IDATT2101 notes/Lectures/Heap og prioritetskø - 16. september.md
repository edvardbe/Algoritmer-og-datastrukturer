Anvendelser for heap
- Sortering med heapsort
	- O(n log n)
	- Trenger ikke ekstra plass slik som flette sortering
	- Ingen værste fall som O(n^2) 
- Prioritetskøer
	- Plukke ut elementer i prioritert rekkefølge
	- Fjerne elementer, legge til, samt endre prioritet kjaåt
	- Kan brukes av os for å prioritere prosseser
	- egner ofr kottest-vei problemet
- En hear er delvis ordnet struktur

Oversikt:

Fiks heap:
	Har et komplett tre der rotas subtrær er heaper
	Ordner rotat slik at hele treet blir en korrekt heap

Lag heap:
	Har et komplett tre, lager en heap av det

Hent maks:
	fjerner max-elementet rota, omorganiserer resten så det blir en heap igjen

Ny prioritet:
	en node endrer verdi, må kanskje flyttes opp eller ned i heapen

Sett inn:
	Setter inn en ny node i heapen

Heapsort:
	sorterer ved først å lage en heap og deretter plukke vekk det største elementet til edt ikk er flere igjen

#### OBS! Tregt fordi den hopper over hele listen, noe cache ikke klarer ved større lister. Quicksort jobber i mindre seksjoner som gjør at den fungerer bedre i slike situasjoner
