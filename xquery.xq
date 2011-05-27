declare namespace mem="http://www.dbai.tuwien.ac.at/education/ssd/SS11/uebung/Memory";

(: Die let-Zeile sollte eigentlich ohnehin keine Duplikate ausspucken, da ja für jede Karte geprüft wird,
ob der Schlüssel mit irgendeinem (@card1 | @card2) aus den aufgedeckten Paaren des Spielers übereinstimmt; auch 
wenn es mehrere Übereinstimmungen gibt, wird die Karte nur einmal genommen!
Trotzdem komisch, dass das folgende eine leere Sequenz ergibt:
    $distinct_cards := distinct-values($cards) :)


for $player in doc('memory.xml')/mem:game/mem:players/mem:player
let $cards := doc('memory.xml')/mem:game/mem:table/mem:card[@column=1][@key = $player/mem:uncovered-pairs/mem:pair/(@card1 | @card2)],
    (: $distinct_cards := distinct-values($cards), :) (: wieso geht das nicht??? :)
    $card_count := count($cards)
where $card_count > 1
order by $card_count descending, $player/@name ascending
return
	element player {
		attribute playing-time {$player/@spent-time},
		element name {
			text {$player/@name }
		},
		element uncovered-count-column1 {$card_count}
	}
