
### Vektede grafer
### Datastruktur
 - Kantene må inneholde vekt
 - I boka har vi Vkant, subklasse av Kant

### Korteste-vei problemet
 - Korteste vei mellom to punkter
	 - Korteste vei fra et sted til alle andre
	 - Alle-til-alle korteste vei
	 - Varianter med raskeste eller billigste vei
- Navigasjon med digitale veikart og evt. gps
**Alle til alle korteste vei** - kjøretid $\Theta (n^{3})$ kompleksitet for plassbruk er $\Theta (n^{2})$


### Virkemåte
- Samme "forgjengersystemer" som i BFS
	- Startnoden har avstand 0
	- I begynnelsen har alle andre $\infty$ avstand
	- Putt nodene i en prioritetskø, f. eks min-heap
	- Løkke:
		- Hent nodene